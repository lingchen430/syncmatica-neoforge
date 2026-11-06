# Port implementation notes

Upstream: sakura-ryoko/syncmatica, LTS/26.1, d145beed842ae855db7da79cd265d10ad0ecc6c3 (0.3.20).
Target: Minecraft 26.1.2, NeoForge 26.1.2.124, Java 25.

- Replaced Fabric initializer, loader metadata/path access, networking registrations and Fabric permission wrapper with native NeoForge equivalents.
- Registered an optional bidirectional PLAY payload. Client handling is registered only on the client distribution; NeoForge dispatches both directions on the main thread.
- Removed common/client packet interception mixins to prevent duplicate or off-thread dispatch. Retained connection adapters used by upstream's exchange system.
- Start one version handshake after player placement. For non-NeoForge peers, retain upstream's optional version probe even if a Fabric receiver has not yet been advertised. For NeoForge peers, honor negotiated channels.
- Preserved the existing syncmatica:main wire codec, feature negotiation and transfer logic. Encoding no longer creates an unfreed ByteBuf copy and does not consume the source buffer.
- Adapted placement persistence callbacks to Forgematica 0.5.1's NBT event API. Locate the unlock button by its listener type instead of relying on a fixed button-list index.
- Use NeoForge permission nodes with upstream's level-ALL defaults. Refresh command context when switching worlds. Create missing nested configuration/download directories recursively.
- Client dependencies are compile-only for the common artifact, enabled at development runtime only with -PwithClientMods, and client-side requirements in mod metadata. Dedicated-server tests omit them entirely.

Unverified: real graphical client startup/world loading, actual multiplayer file transfer/reconnects, large payload behavior, and real Fabric/NeoForge interoperability. The JUnit server is ephemeral and has no playable level. Tests do not establish those outcomes.
