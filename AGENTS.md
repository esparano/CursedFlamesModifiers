# AGENTS.md

Guidance for AI coding agents working in `CursedFlamesModifiers` (`esparano/CursedFlamesModifiers`).

## Project Overview

Minecraft **1.21.1** NeoForge mod (**Modifiers**, mod ID `modifiers`), a fork of `CursedFlames/Modifiers` implementing Terraria-style gear modifiers for armor, tools, and weapons.

- **Active branch:** `1.21.1` (GitHub default branch is still `MC_1.20`; check `git branch` before making assumptions).
- **Stack:** Java 21, Gradle 9.2.1 wrapper, NeoForge ModDevGradle `2.0.141`, NeoForge `21.1.228`, Parchment `1.21` / `2024.11.10`, Mixin `0.8.7` + MixinExtras `0.3.5`.
- **Active subproject:** Only `:neoforge` is currently included in `settings.gradle` (`common` and `fabric` are commented out).

## Build & Run Commands

Run from the repository root (PowerShell on Windows):

```powershell
# Compile Java sources (fast verification)
.\gradlew.bat :neoforge:compileJava

# Full build & jar assembly
.\gradlew.bat :neoforge:build

# Regenerate datapack modifier JSONs from ModifiersDatagen
.\gradlew.bat :neoforge:runData

# Launch Minecraft dev client / server
.\gradlew.bat :neoforge:runClient
.\gradlew.bat :neoforge:runServer
```

Notes:
- `gradle.properties` sets `org.gradle.daemon=false`, so each Gradle invocation forks a single-use daemon.
- The project targets Java 21 via Gradle Java toolchains (`java.toolchain.languageVersion = 21`), even if the shell's default `java` on `PATH` is another version.
- Do not launch `runClient` or `runServer` unless explicitly asked or needed for runtime testing.

## Architecture & Key Files

All active mod source lives under `neoforge/src/main/java/cursedflames/modifiers/`:

- `ModifiersModNeoforge.java` — `@Mod("modifiers")` entrypoint. Registers the `modifiers:modifiers` datapack registry, `modifiers:modifier` `DataComponentType<ItemModifier>`, items (`modifier_book`, `reforge_template`), creative mode tab, and datagen provider.
- `ModifiersMod.java` / `Constants.java` — Mod ID (`modifiers`), logger, registry key, and static references for registered components/items.
- `Modifier.java` & `ItemModifier.java` — Modifier definition (`supportedItems`, `weight`, `quality`, `slots`, `effects`), Codecs/StreamCodecs, and `TooltipProvider`.
- `ModEventHandler.java` — `@EventBusSubscriber` listening to `ItemAttributeModifierEvent` to dynamically inject modifier attributes onto `ItemStack`s (appending the armor slot to modifier IDs so identical modifiers on multiple armor pieces stack).
- `ModifierHandler.java` & `ModifierPool.java` — Setting/removing `ITEM_MODIFIER_COMPONENT` and weighted random rolling from the datapack registry.
- `ModifiersDatagen.java` — Defines built-in armor and tool/weapon modifiers exported to `neoforge/src/generated/resources/data/modifiers/modifiers/modifiers/*.json`.
- `item/ItemModifierBook.java` & `item/ItemReforgeTemplate.java` — Modifier Book and Reforge Template items.
- `mixin/`:
  - `MixinAnvilMenu.java` — Applies Modifier Books or strips modifiers with Reforge Templates in anvils.
  - `MixinItemStack.java` — Prefixes item hover names (skipping custom-named items) and inserts modifier tooltips after enchantments.
  - `MixinLivingEntity.java` — Rolls a modifier on first equip in `collectEquipmentChanges`.
- Legacy commented-out reference files: `Modifiers.java` and `ModifierHandlerOld.java` (kept for reference during the 1.21 port; do not re-enable unless requested).

Resources & config:
- `neoforge/src/main/resources/META-INF/neoforge.mods.toml` — Mod metadata (interpolated from `gradle.properties`).
- `neoforge/src/main/resources/modifiers.mixins.json` & `modifiers.neoforge.mixins.json` — Mixin configs.
- `neoforge/src/main/resources/assets/modifiers/` — Translations (`lang/en_us.json`), item models, and textures.
- `neoforge/src/generated/resources/` — Datagen output (regenerate via `runData` rather than hand-editing).

## Conventions & Gotchas

- **Dynamic attributes, not mutated vanilla components:** Modifier attributes are injected on read via `ItemAttributeModifierEvent` in `ModEventHandler.java` instead of writing into `DataComponents.ATTRIBUTE_MODIFIERS`. Keep this pattern so vanilla/default attributes and smithing table upgrades stay intact.
- **Armor modifier stacking:** Attribute modifier `ResourceLocation` IDs must be unique per equipped armor slot (see `ModEventHandler.java`), otherwise equipping multiple armor pieces with the same modifier will overwrite instead of stack.
- **Datagen sync:** Whenever modifiers or tags change in `ModifiersDatagen.java`, run `.\gradlew.bat :neoforge:runData` and verify generated JSON files under `neoforge/src/generated/resources/`.
- **Resource property expansion:** Any new property added to `gradle.properties` that is used in `neoforge.mods.toml`, `pack.mcmeta`, or mixin JSONs must also be added to `expandProps` in `neoforge/build.gradle`.
- **No automated test suite yet:** Verify code changes with `.\gradlew.bat :neoforge:compileJava` (or `:neoforge:build`) at minimum, and note manual in-game test steps when changing gameplay behavior.

## Git & GitHub Workflow

- **Remotes:**
  - `origin`: `https://github.com/esparano/CursedFlamesModifiers` (user's fork)
  - `upstream`: `https://github.com/CursedFlames/Modifiers.git` (read-only reference; never push to `upstream`)
- **Safety:** Never force-push or push directly to remote branches without explicit user approval. Keep `neoforge/run/` and `build/` untracked.
- **GitHub Issues / PRs:** Use `gh` CLI (authenticated as `esparano`, default repo set to `esparano/CursedFlamesModifiers` via `gh repo set-default`). Pass `-R CursedFlames/Modifiers` explicitly only when inspecting upstream issues/PRs.
