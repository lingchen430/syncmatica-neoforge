# Changelog

## 0.3.20-neoforge.3

- Support the whole NeoForge 26.1.2 build series, including 26.1.2.0-beta.
- Compile against the earliest published NeoForge 26.1.2 build instead of an arbitrarily recent minimum.
- Permit MaFgLib 0.5.4 for pre-.84 clients; document the .84 tooltip-hook boundary and check those injection points.
- Add representative CI combinations spanning .0-beta, .77, .84, .122 and .126.
- Retain one universal client/server artifact and the existing Syncmatica protocol.

## 0.3.20-neoforge.2

- Lower the supported NeoForge minimum from 26.1.2.124 to 26.1.2.122.
- Lower the supported MaFgLib minimum from 0.5.6 to 0.5.5 (Minecraft 26.1.2 builds).
- Build against the minimum supported versions and check mixin targets against the selected dependency artifacts.
- Add CI coverage for NeoForge 26.1.2.122 / MaFgLib 0.5.5 and NeoForge 26.1.2.124 / MaFgLib 0.5.6.
- Keep the Syncmatica protocol, persistence format, and other dependency requirements unchanged.

## 0.3.20-neoforge.1

Initial experimental native NeoForge port of Syncmatica 0.3.20 for Minecraft 26.1.2.
