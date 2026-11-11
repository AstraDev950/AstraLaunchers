/*
 * Astra Launcher
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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.movtery.zalithlauncher.setting

import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsInitializerTest {

    @Test
    fun `default heap scales with device memory`() {
        assertEquals(1792, calculateDefaultRAMAllocation(4096, false, 3072))
        assertEquals(2688, calculateDefaultRAMAllocation(6144, false, 5120))
        assertEquals(3584, calculateDefaultRAMAllocation(8192, false, 7168))
    }

    @Test
    fun `32 bit devices keep conservative heap`() {
        assertEquals(696, calculateDefaultRAMAllocation(4096, true, 1024))
        assertEquals(512, calculateDefaultRAMAllocation(512, true, 512))
    }

    @Test
    fun `heap never exceeds device limit`() {
        assertEquals(1920, calculateDefaultRAMAllocation(8192, false, 2000))
    }
}
