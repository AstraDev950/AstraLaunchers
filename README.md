# Astra Launcher

[English](README.md) | [简体中文](README_ZH_CN.md) | [繁體中文](README_ZH_TW.md)

**An unofficial Minecraft: Java Edition launcher modification for Android.**

Astra Launcher is an unofficial Android launcher fork maintained by **AstraDev**, built on [Zalith Launcher 2](https://github.com/ZalithLauncher/ZalithLauncher2) with Astra-specific branding and modifications.

> **Unofficial Modified Version**
>
> Astra Launcher is an independent community project. It is not affiliated with or endorsed by the Zalith Launcher developers, Mojang Studios, or Microsoft.

## Project ownership and attribution

AstraDev maintains this fork and its Astra-specific branding and modifications. AstraDev does not claim authorship of upstream launcher code or third-party components.

## Microsoft sign-in build configuration

Microsoft account sign-in uses the device-code flow and requires an Astra-owned Microsoft Entra application (client) ID. Configure the app registration for public-client/device-code sign-in and personal Microsoft accounts. Set the ID as the GitHub Actions repository variable `OAUTH_CLIENT_ID` (recommended), or use the `OAUTH_CLIENT_ID` Actions secret as a fallback. Local builds can use `OAUTH_CLIENT_ID` or `ZalithLauncher/.oauth_client_id.txt`. The same public client ID works for both supported Android ABIs; it is not a per-device value. A client ID is public configuration, not a client secret. Never commit account passwords, access tokens, refresh tokens, or client secrets.

## License and attribution

This project contains code derived from [Zalith Launcher 2](https://github.com/ZalithLauncher/ZalithLauncher2) and other upstream contributions. Original source headers, copyright notices, license texts, and third-party attributions are retained. See [LICENSE](LICENSE) and the relevant upstream notices for applicable terms.

When redistributing this modified version:

- Keep original copyright and license information.
- Clearly identify it as an unofficial modified version.
- Provide the corresponding source code and comply with GPL-3.0 and any applicable additional terms.

## Disclaimer

Minecraft is a trademark of Microsoft Corporation. Astra Launcher is an independent project and is not affiliated with or endorsed by Mojang Studios or Microsoft.
