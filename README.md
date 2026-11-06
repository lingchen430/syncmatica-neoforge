# Syncmatica 非官方 NeoForge 移植版

[下载实验版 JAR](https://github.com/lingchen430/syncmatica-neoforge/releases/tag/v0.3.20-neoforge.1) · [上游源码](https://github.com/sakura-ryoko/syncmatica/tree/LTS/26.1)

适用 Minecraft **26.1.2**、NeoForge **26.1.2.124 或更新的 26.1.2 构建**，需要 **Java 25**。
版本：0.3.20-neoforge.1，实验性首版。基于 sakura-ryoko/syncmatica 的 LTS/26.1 分支，提交 `d145beed842ae855db7da79cd265d10ad0ecc6c3`。
此移植未经上游作者发布或背书。

## 服务端安装

把 `syncmatica-neoforge-26.1.2-0.3.20-neoforge.1.jar` 放入 NeoForge 服务器的 `mods` 文件夹并重启。
服务端只需要这个 JAR，不需要 Forgematica、MaFgLib、FoxifiedClassTweaker、Fabric API 或 Connector。
未安装 Syncmatica 的玩家仍可正常加入；是否能跨加载器加入，还取决于服务器的其他模组。

## NeoForge 客户端安装

在客户端 `mods` 中安装本 JAR 和以下三个依赖（均选 Minecraft 26.1.2 对应文件）：

1. [Forgematica 0.5.1+mc26.1.2](https://github.com/CagayakeGirls/litematica-neoforge/releases/tag/0.5.1%2Bmc26.1.2)
2. [MaFgLib 0.5.6+mc26.1.2](https://github.com/CagayakeGirls/malilib-neoforge/releases/tag/0.5.6%2Bmc26.1.2)
3. [FoxifiedClassTweaker 0.1.2](https://modrinth.com/mod/foxifiedclasstweaker/version/nq3u4UMi)

依赖文件没有打进本 JAR。此移植使用原生 NeoForge API，无需 Connector。
加入安装了 Syncmatica 的服务器后，在投影主菜单中打开共享投影列表，在投影放置列表中使用“分享”按钮。
加载投影需要位于对应维度；解锁投影申请修改，重新锁定后提交位置等变更。
原版尚未完成的材料收集按钮仍保持禁用。

## Fabric 客户端

保留原版的 `syncmatica:main` 数据包格式、版本/特性握手及文件传输协议，目标是兼容原版 Syncmatica 0.3.20。
Fabric 客户端应继续使用 [原版 Syncmatica](https://modrinth.com/mod/syncmatica) 及其对应的 Litematica、MaLiLib，不能加载本 NeoForge JAR。
跨加载器实际联机尚未验证，不能仅凭数据包编码测试认定已完全兼容。

## 数据和权限

沿用上游的数据目录：共享文件位于游戏目录 `syncmatics/`；服务端配置位于当前世界的 `syncmatica/config.json`；客户端下载位于 `schematics/sync/`。
命令为 `/syncmatica load [file]`。权限节点保留 `syncmatica.command`、`syncmatica.command.load` 和 `syncmatica.command.load_each`，改接 NeoForge 权限提供器。
默认权限沿用上游对所有玩家开放的行为。第三方权限提供器（包括相应的 LuckPerms 集成）没有做实服验证。

## 已验证范围

- Java 25 / Gradle 9.2.1 / NeoForge 26.1.2.124 编译成功，已执行过完整 `clean build`。
- 4 项自动检查通过：临时服务端运行和通信上下文/命令初始化；数据包往返与重复编码；投影元数据保存读取；注入点兼容检查。
- 服务端测试环境没有加载三个客户端依赖。
- 16 处启用的注入签名与 Minecraft / Forgematica / MaFgLib 的实际类文件匹配。

**尚未验证：图形客户端进入世界、真实玩家之间上传下载、跨加载器联机、断线重连、大型投影和完整整合包兼容性。**
服务端运行检查使用 NeoForge 的临时 JUnit 服务器，它不创建可游玩的世界；这不等同于完整游戏实测。
建议先放到测试实例验证投影分享和修改流程，再加入日常整合包。

## 从源码构建

解压完整源码工程，在该目录使用 PowerShell 运行：

```powershell
powershell -ExecutionPolicy Bypass -File .\setup-dependencies.ps1
.\gradlew.bat build
```

依赖从原发布地址下载，并逐个核对锁定的 SHA-256。源码包不包含第三方依赖、Minecraft 文件或个人游戏数据。
构建产物位于 `build/libs/`，带 `-sources` 的 JAR 是源码，不应放入 `mods`。
`gradlew -PwithClientMods runClient` 使用单独的开发实例并启用客户端依赖；`gradlew runServer` 使用独立开发服务端。

## 来源和许可

检索日期：2026-11-06。官方 Modrinth 项目列出的加载器为 Fabric / Quilt；GitHub 和 Modrinth 检索未发现现成的 Syncmatica NeoForge 26.1.2 移植发布。
这表示本次检索未找到，不表示已穷尽所有私人仓库或未公开项目。

- [Syncmatica 官方发布页](https://modrinth.com/mod/syncmatica)
- [Syncmatica 26.1 源码分支](https://github.com/sakura-ryoko/syncmatica/tree/LTS/26.1)
- [NeoForge 开发模板](https://github.com/NeoForgeMDKs/MDK-26.1.2-ModDevGradle)
- [Forgematica 源码](https://github.com/CagayakeGirls/litematica-neoforge)
- [MaFgLib 源码](https://github.com/CagayakeGirls/malilib-neoforge)

Syncmatica 源码及本移植改动采用 CC0-1.0，完整许可见 `LICENSE`。原作者及贡献者包括 nnnik、samipourquoi、kpzip、sakura-ryoko 等，完整记录以上游为准。
第三方依赖保留各自许可，并从各自官方发布地址另行获取。
