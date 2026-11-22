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

class MicrosoftTokenRefreshTest {
    @Test
    fun refreshUsesTheMicrosoftIdentityPlatformV2TokenEndpoint() {
        assertEquals(
            "https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
            MICROSOFT_TOKEN_URL
        )
    }

    @Test
    fun refreshRequestContainsRequiredGrantAndMinecraftScopes() {
        val parameters = buildMicrosoftRefreshTokenParameters(
            clientId = "00000000-0000-0000-0000-000000000000",
            refreshToken = "refresh-token-value"
        )

        assertEquals("00000000-0000-0000-0000-000000000000", parameters["client_id"])
        assertEquals("refresh-token-value", parameters["refresh_token"])
        assertEquals("refresh_token", parameters["grant_type"])
        assertEquals(
            "XboxLive.signin offline_access openid profile email",
            parameters["scope"]
        )
    }
}
