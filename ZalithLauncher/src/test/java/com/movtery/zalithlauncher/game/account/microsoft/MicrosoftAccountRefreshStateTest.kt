/*
 * Zalith Launcher 2
 * Copyright (C) 2025-2026 MovTery and contributors
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
 */

package com.movtery.zalithlauncher.game.account.microsoft

import com.movtery.zalithlauncher.game.account.Account
import com.movtery.zalithlauncher.game.account.applyMicrosoftRefreshResult
import com.movtery.zalithlauncher.game.account.wardrobe.SkinModelType
import org.junit.Assert.assertEquals
import org.junit.Test

class MicrosoftAccountRefreshStateTest {
    @Test
    fun refreshedSessionCopiesAccessTokenExpiryAndProfileFields() {
        val account = Account(
            accessToken = "old-access-token",
            expiresAt = 1L,
            clientToken = "old-client-token",
            username = "OldName",
            profileId = "old-profile",
            refreshToken = "old-refresh-token",
            xUid = "old-xuid",
            skinModelType = SkinModelType.STEVE
        )
        val refreshed = Account(
            accessToken = "new-access-token",
            expiresAt = 1_800_000_000_000L,
            clientToken = "new-client-token",
            username = "NewName",
            profileId = "new-profile",
            refreshToken = "new-refresh-token",
            xUid = "new-xuid",
            skinModelType = SkinModelType.ALEX
        )

        account.applyMicrosoftRefreshResult(refreshed)

        assertEquals("new-access-token", account.accessToken)
        assertEquals(1_800_000_000_000L, account.expiresAt)
        assertEquals("new-client-token", account.clientToken)
        assertEquals("NewName", account.username)
        assertEquals("new-profile", account.profileId)
        assertEquals("new-refresh-token", account.refreshToken)
        assertEquals("new-xuid", account.xUid)
        assertEquals(SkinModelType.ALEX, account.skinModelType)
    }
}
