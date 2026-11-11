/*
 * Zalith Launcher 2
 * Copyright (C) 2025-2026 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.movtery.zalithlauncher.game.version.resource_pack

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.movtery.zalithlauncher.game.version.mod.meta.PackMcMeta
import com.movtery.zalithlauncher.utils.GSON
import com.movtery.zalithlauncher.utils.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile

private const val TAG = "ResourcePackUtils"
private const val MAX_PACK_ICON_BYTES = 4 * 1024 * 1024
private const val MAX_PACK_ICON_SOURCE_DIMENSION = 8192
private const val PACK_ICON_PREVIEW_DIMENSION = 128

/**
 * Resource-pack icons are launcher previews only. Keep a small thumbnail in memory
 * instead of retaining potentially huge source PNGs for every installed pack.
 */
private fun readPackIcon(input: InputStream): ByteArray? = runCatching {
    val source = ByteArrayOutputStream()
    val buffer = ByteArray(8192)
    var totalBytes = 0

    while (true) {
        val count = input.read(buffer)
        if (count < 0) break
        totalBytes += count
        if (totalBytes > MAX_PACK_ICON_BYTES) return@runCatching null
        source.write(buffer, 0, count)
    }

    val sourceBytes = source.toByteArray()
    if (sourceBytes.isEmpty()) return@runCatching null

    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(sourceBytes, 0, sourceBytes.size, bounds)
    val sourceWidth = bounds.outWidth
    val sourceHeight = bounds.outHeight
    if (sourceWidth <= 0 || sourceHeight <= 0 ||
        sourceWidth > MAX_PACK_ICON_SOURCE_DIMENSION ||
        sourceHeight > MAX_PACK_ICON_SOURCE_DIMENSION
    ) {
        return@runCatching null
    }

    var sampleSize = 1
    while (maxOf(sourceWidth, sourceHeight) / sampleSize > PACK_ICON_PREVIEW_DIMENSION) {
        sampleSize *= 2
    }

    val options = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    val bitmap = BitmapFactory.decodeByteArray(sourceBytes, 0, sourceBytes.size, options)
        ?: return@runCatching null

    try {
        ByteArrayOutputStream().use { output ->
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
                output.toByteArray()
            } else {
                null
            }
        }
    } finally {
        bitmap.recycle()
    }
}.onFailure {
    Logger.warning(TAG, "Failed to create a resource-pack icon preview", it)
}.getOrNull()

/**
 * 解析资源包文件，游戏内仅支持加载文件夹、文件后缀为zip的资源包
 * @param file 资源包文件
 */
suspend fun parseResourcePack(file: File): ResourcePackInfo? = withContext(Dispatchers.IO) {
    runCatching {
        var isValid = false
        var metaContent: String? = null
        var iconBytes: ByteArray? = null
        var fileSize: Long? = null

        if (file.isDirectory) { //文件夹形式的资源包
            //资源包元数据
            File(file, "pack.mcmeta").takeIf { it.exists() }?.let { metaFile ->
                metaContent = metaFile.readText()
            }
            //尝试读取资源包的图标
            File(file, "pack.png").takeIf { it.isFile }?.let { iconFile ->
                iconFile.inputStream().use { input -> iconBytes = readPackIcon(input) }
            }
        } else if (file.extension == "zip") { //压缩包形式的资源包
            //性能、速度考虑，仅压缩包形式的资源包可以计算文件大小
            fileSize = FileUtils.sizeOf(file)

            ZipFile(file).use { zip ->
                //资源包元数据
                zip.getEntry("pack.mcmeta")?.let { metaEntry ->
                    metaContent = zip.getInputStream(metaEntry).bufferedReader().readText()
                }
                //尝试读取资源包的图标
                zip.getEntry("pack.png")?.let { iconEntry ->
                    zip.getInputStream(iconEntry).use { input ->
                        iconBytes = readPackIcon(input)
                    }
                }
            }
        }

        val meta = metaContent?.let { content ->
            runCatching {
                GSON.fromJson(content, PackMcMeta::class.java)
            }.onFailure {
                Logger.warning(TAG, "Failed to parse the resource package metadata: ${file.absolutePath}", it)
            }.getOrNull()
        }?.also {
            //解析成功，则代表其是一个有效的格式
            isValid = true
        }

        ResourcePackInfo(
            file = file,
            fileSize = fileSize,
            isValid = isValid,
            description = meta?.pack?.description?.toPlainText(),
            packFormat = meta?.pack?.packFormat,
            icon = iconBytes
        )
    }.onFailure {
        Logger.warning(TAG, "Failed to parse the resource package: ${file.absolutePath}", it)
    }.getOrNull()
}