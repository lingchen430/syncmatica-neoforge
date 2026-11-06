# 构建与验证记录

日期：2026-11-06

- 上游提交：`d145beed842ae855db7da79cd265d10ad0ecc6c3`
- Minecraft 26.1.2 / NeoForge 26.1.2.124 / Java 25 / Gradle 9.2.1
- 完整 clean build 和随后改动的 build 均成功；最终测试 4 通过、0 失败。
- 核对了 16 处启用的注入点签名。
- JAR 大小：261801 字节。
- JAR SHA-256：`cce139e034004ce3b7da26a852eab6ce78fd9c8467b3625a82719ada7528a279`
- JAR 有原生 NeoForge 元数据，不含 Fabric 模组入口或打包的 Fabric API 类。

测试项目：
- `allActiveMixinTargetsAndCallbackSignaturesMatch()`
- `dedicatedServerStartsWithoutClientMods(MinecraftServer)`
- `payloadRoundTripKeepsFabricWireFormatAndSupportsRepeatedEncoding(MinecraftServer)`
- `placementMetadataRetainsPositionRotationAndOwner(MinecraftServer)`

服务端测试使用 NeoForge 临时 JUnit 服务器，不创建可游玩的世界，且未加载 Forgematica / MaFgLib / FoxifiedClassTweaker。
网络检查验证的是上游数据包格式、往返读取和重复编码，不是实际跨网络传输。
客户端检查属于类文件注入签名检查，不等于图形客户端启动成功。

尚未进行游戏内多人联机、真实文件上传下载、重连、大型投影或整合包实测。本产物为非官方实验性首版。
