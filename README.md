# Syncmatica NeoForge 26.1.2 通用版

[下载通用版 JAR](https://github.com/lingchen430/syncmatica-neoforge/releases/tag/v0.3.20-neoforge.3) · [上游源码](https://github.com/sakura-ryoko/syncmatica/tree/LTS/26.1)

**Minecraft 26.1.2 / NeoForge 26.1.2.* / Java 25**。同一个 JAR 用于客户端和专用服务器。
版本：**0.3.20-neoforge.3**，非官方实验性移植。以 NeoForge **26.1.2.0-beta** 为编译基线，加载范围覆盖整个 NeoForge 26.1.2 构建系列，包括早期 beta。
不适用于 Minecraft 26.1、26.1.1、26.2 或其他游戏版本。

## 服务端

将 `syncmatica-neoforge-26.1.2-0.3.20-neoforge.3.jar` 放入服务器 `mods` 后重启。
服务端只需要此 JAR，不需要 Forgematica、MaFgLib、FoxifiedClassTweaker、Fabric API 或 Connector。
未安装 Syncmatica 的玩家仍可加入；是否允许跨加载器玩家，还取决于服务器的其他模组。

## 客户端

安装同一个 Syncmatica JAR，并安装以下前置：

1. [Forgematica 0.5.1+mc26.1.2](https://github.com/CagayakeGirls/litematica-neoforge/releases/tag/0.5.1%2Bmc26.1.2)
2. 按下表选择 MaFgLib，游戏版本均为 26.1.2。
3. [FoxifiedClassTweaker 0.1.2](https://modrinth.com/mod/foxifiedclasstweaker/version/nq3u4UMi)

| 客户端 NeoForge 版本 | MaFgLib 版本 |
| --- | --- |
| 26.1.2.0-beta 至 26.1.2.83 | [0.5.4+mc26.1.2](https://github.com/CagayakeGirls/malilib-neoforge/releases/tag/0.5.4%2Bmc26.1.2) |
| 26.1.2.84 及以后的 26.1.2 构建 | [0.5.5+mc26.1.2](https://github.com/CagayakeGirls/malilib-neoforge/releases/tag/0.5.5%2Bmc26.1.2) 或 [0.5.6+mc26.1.2](https://github.com/CagayakeGirls/malilib-neoforge/releases/tag/0.5.6%2Bmc26.1.2) |

这是 MaFgLib 自身提示信息注入点随 NeoForge .84 变化而产生的分界，详见 [MaFgLib 0.5.5 变更](https://github.com/CagayakeGirls/malilib-neoforge/releases/tag/0.5.5%2Bmc26.1.2)。
例如 NeoForge .122 + MaFgLib 0.5.5 可以直接使用本版。
同一模组只保留一个版本；先移除旧的 Syncmatica neoforge.1 / neoforge.2，再加入 neoforge.3。

加入安装了 Syncmatica 的服务器后，在投影主菜单中打开共享投影列表，在投影放置列表中使用“分享”按钮。
加载投影需要位于对应维度；解锁投影申请修改，重新锁定后提交位置等变更。上游未完成的材料收集按钮仍禁用。

## 兼容范围与验证限制

模组声明支持 NeoForge **26.1.2 全构建系列**，不是只解除 .122 或 .124 的版本拦截。
构建基线是该系列最早的 .0-beta；自动检查覆盖 .0-beta、.77、.84、.122、.126 等代表性版本。
检查项目及结果见 [验证记录](docs/validation.md)。

**支持范围声明不等于每个版本都经过游戏内实测。** 未逐一运行所有历史构建，也未进行图形客户端进入世界、真实多人文件传输、重连、大型投影或整合包实测。
临时服务端检查使用无可游玩世界的 JUnit 服务器；客户端检查验证实际类文件的接口及注入点，包括 MaFgLib 提示信息注入点。
未来同系列构建允许加载，但若其接口再次发生不兼容变化，仍需单独修复。

保留 `syncmatica:main` 数据包格式及原版握手，目标是兼容原版 Syncmatica 0.3.20。Fabric 客户端继续使用原版 Syncmatica / Litematica / MaLiLib；实际跨加载器联机尚未验证。

## 数据与权限

共享文件位于游戏目录 `syncmatics/`；服务端配置位于当前世界 `syncmatica/config.json`；客户端下载位于 `schematics/sync/`。
命令为 `/syncmatica load [file]`。权限节点为 `syncmatica.command`、`syncmatica.command.load` 和 `syncmatica.command.load_each`，使用 NeoForge 权限提供器。
默认权限沿用上游对所有玩家开放的行为；第三方权限提供器没有做实服验证。

## 源码构建

解压源码，在目录内运行：

```powershell
powershell -ExecutionPolicy Bypass -File .\setup-dependencies.ps1
.\gradlew.bat build
```

默认按 .0-beta / MaFgLib 0.5.4 构建。检查其他组合时，例如：

```powershell
.\gradlew.bat build "-PneoVersion=26.1.2.126" "-PmafglibVersion=0.5.6"
```

`gradlew -PwithClientMods runClient` 启用所选客户端前置；`gradlew runServer` 不加载客户端前置。
依赖从原发布地址下载并核对锁定的 SHA-256。产物位于 `build/libs/`；带 `-sources` 的 JAR 不应安装到游戏。

## 来源与许可

基于 [sakura-ryoko/syncmatica LTS/26.1](https://github.com/sakura-ryoko/syncmatica/tree/LTS/26.1) 提交 `d145beed842ae855db7da79cd265d10ad0ecc6c3`（0.3.20）。
原作者及贡献者包括 nnnik、samipourquoi、kpzip、sakura-ryoko 等。此移植未经上游作者发布或背书。
Syncmatica 源码及本移植改动采用 CC0-1.0，见 `LICENSE`；第三方前置保留各自许可，并另行从原发布地址获取。
