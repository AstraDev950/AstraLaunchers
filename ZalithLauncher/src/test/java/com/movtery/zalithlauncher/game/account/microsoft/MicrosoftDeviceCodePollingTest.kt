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

import org.junit.Assert.assertEquals
import org.junit.Test

class MicrosoftDeviceCodePollingTest {
    @Test
    fun slowDownAddsFiveSecondsToTheCurrentInterval() {
        assertEquals(8_000L, nextMicrosoftDeviceCodePollingIntervalMillis(3_000L))
    }

    @Test
    fun eachSlowDownResponseAddsAnotherFiveSeconds() {
        val firstInterval = nextMicrosoftDeviceCodePollingIntervalMillis(3_000L)
        val secondInterval = nextMicrosoftDeviceCodePollingIntervalMillis(firstInterval)

        assertEquals(13_000L, secondInterval)
    }
}
