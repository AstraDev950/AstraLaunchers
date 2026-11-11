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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package com.movtery.zalithlauncher.game.account.microsoft

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class MicrosoftOAuthConfigurationTest {
    @Test
    fun configuredClientIdIsTrimmed() {
        val clientId = "00000000-0000-0000-0000-000000000000"
        assertEquals(clientId, requireMicrosoftOAuthClientId("  $clientId \n"))
    }

    @Test
    fun emptyClientIdFailsBeforeNetworkRequest() {
        try {
            requireMicrosoftOAuthClientId("")
            fail("A missing Microsoft OAuth client ID must be rejected.")
        } catch (_: MicrosoftOAuthNotConfiguredException) {
            // Expected: never send an empty client_id to Microsoft.
        }
    }

    @Test
    fun whitespaceOnlyClientIdFailsBeforeNetworkRequest() {
        try {
            requireMicrosoftOAuthClientId("   \t\n")
            fail("A blank Microsoft OAuth client ID must be rejected.")
        } catch (_: MicrosoftOAuthNotConfiguredException) {
            // Expected: whitespace is not a valid client ID.
        }
    }
}
