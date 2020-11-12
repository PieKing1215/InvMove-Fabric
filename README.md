<h1>InvMove<br>
  <a href="https://www.curseforge.com/minecraft/mc-mods/invmove"><img src="http://cf.way2muchnoise.eu/versions/%20For%20MC%20_invmove_all(555-0C8E8E-fff-010101).svg" alt="Supported Versions"></a>
  <a href="https://github.com/PieKing1215/InvMove/blob/master/LICENSE.md"><img src="https://img.shields.io/github/license/PieKing1215/InvMove?style=flat&color=0C8E8E" alt="License"></a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/invmove"><img src="http://cf.way2muchnoise.eu/full_invmove_downloads(E04E14-555-fff-010101-1C1C1C).svg" alt="Download Count"></a>
</h1>

### Minecraft Forge mod that adds the ability to walk around while in inventories

![](demo/demo.gif)

Allows for moving, jumping, sprinting, etc. from within inventories.

Also disables the darkened background in inventories that don't pause the game.

Both features can be toggled on or off per-inventory in the config menu.

This mod is client-side, but it may raise alarms if used on servers with anticheat.<br>
I take no responsibility if you get banned or something because you used this on public servers.

Made after [I couldn't find an existing mod for this](https://redd.it/egwe8w).

### [Releases](https://github.com/PieKing1215/InvMove/releases)

Requires [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config-forge) (1.14+ only) so the config screen works.

## Config (In-game)
<details>
  <summary>Click to expand.</summary>
  
![](demo/where_config.png)
<img src="demo/config_screen.png" alt="alt text" width="657" height="528">
### InvMove has an in-game config menu which can be accessed from the Forge mod list.<br>
(for 1.14+ also highly recommend [GameMenuModOption](https://www.curseforge.com/minecraft/mc-mods/gamemenumodoption) which adds the mod list button to the pause screen)<br>
(note the config for 1.12 looks different but has generally the same layout)<br>
In the config menu, you can find several options:<br>
#### General:
- Enable: Enable the entire mod
- Debug Display: Enables a debug overlay that can help debug compatibility problems.
#### UI Movement:
- Move In Inventories: Enable movement in inventories
- Allow Sneaking: Allow sneaking in inventories (disabled by default because it's distracting when shift-clicking)
- Allow Jumping: Allow jumping in inventories
- Text Field Disables Movement: Disable movement when a text field is focused (like search bars or in an anvil)
- (Expandable categories that let you enable/disable movement for certain inventories)
#### UI Background:
- Hide Inventory Backgrounds: Hides the background tint while in inventories.
- (Expandable categories that let you enable/disable the background for certain inventories)
</details>

## Compatibility / Mod Support
<details>
  <summary>Click to expand.</summary>
  
![](demo/mod_demo.gif)<br>
*(Note: Corail Woodcutter is no longer supported)*<br>
### As of version 0.4.0+, InvMove has actual mod support.<br>

InvMove has specific support for certain mods, but any GUIs from unrecognized mods are added into the config dynamically and can be manually enabled.<br>
Unrecognized GUIs will only appear in the config once they have been opened/seen in-game.

Explicitly supported mods have their own sections in the config, and come with tested default settings.

If you find problems with any of the explicitly supported mods, please start a ticket in the [issue tracker](https://github.com/PieKing1215/InvMove/issues).<br>
If there's a mod you want to be added, also please start a ticket in the [issue tracker](https://github.com/PieKing1215/InvMove/issues), especially if it doesn't work enabling it from the "unrecognized" section of the config.

*(The initial list of supported mods was arbitrarily chosen from a mod pack I'm playing.)*
*(More are being added mostly by going through curseforge sorted by popularity)*

InvMove explicitly supports the following mods (as of version 0.4.4):
- [Botania](https://www.curseforge.com/minecraft/mc-mods/botania)
- [CC: Tweaked](https://www.curseforge.com/minecraft/mc-mods/cc-tweaked)
- [Charm](https://www.curseforge.com/minecraft/mc-mods/charm)
- [Cooking for Blockheads](https://www.curseforge.com/minecraft/mc-mods/cooking-for-blockheads)
- [Create](https://www.curseforge.com/minecraft/mc-mods/create) (1.14+)
- [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) (1.14+)
- [EmbellishCraft](https://www.curseforge.com/minecraft/mc-mods/embellishcraft) (1.14+)
- [Ender Storage 1.8.+](https://www.curseforge.com/minecraft/mc-mods/ender-storage-1-8)
- [Engineer's Decor](https://www.curseforge.com/minecraft/mc-mods/engineers-decor)
- [FreeCamera](https://www.curseforge.com/minecraft/mc-mods/freecamera) (1.12)
- [Immersive Engineering](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering)
- [Industrial Foregoing](https://www.curseforge.com/minecraft/mc-mods/industrial-foregoing)
- [Iron Chests](https://www.curseforge.com/minecraft/mc-mods/iron-chests)
- [ItemPhysic Full](https://www.curseforge.com/minecraft/mc-mods/itemphysic)
- [Just Enough Items (JEI)](https://www.curseforge.com/minecraft/mc-mods/jei)
- [Locks](https://www.curseforge.com/minecraft/mc-mods/locks) (1.14+)
- [Mekanism](https://www.curseforge.com/minecraft/mc-mods/mekanism)
- [Mekanism Generators](https://www.curseforge.com/minecraft/mc-mods/mekanism-generators) (1.14+)
- [MrCrayfish's Furniture Mod](https://www.curseforge.com/minecraft/mc-mods/mrcrayfish-furniture-mod) (1.14+)
- [Music Maker Mod](https://www.curseforge.com/minecraft/mc-mods/music-maker-mod) (1.14+)
- [Optifine](https://www.optifine.net/)
- [Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli)
- [Quark](https://www.curseforge.com/minecraft/mc-mods/quark)
- [Refined Storage](https://www.curseforge.com/minecraft/mc-mods/refined-storage)
- [Waystones](https://www.curseforge.com/minecraft/mc-mods/waystones)
</details>

## Usage

Feel free to use in packs if you wish.

Do not download this mod from anywhere other than the InvMove GitHub page or the InvMove Curseforge page.<br>
This mod may not be reposted to any other third-party websites.<br>
[#StopModReposts](https://stopmodreposts.org)

Tested on<br>
Forge 31.2.31 for MC 1.15.2,<br>
Forge 28.2.23 for MC 1.14.4,<br>
Forge 14.23.5.2854 for MC 1.12.2,<br>
on both singleplayer and multiplayer.

The mod is licensed under the [GNU General Public License v3.0](LICENSE.md)

## Upcoming
More mod compatibilities, bug fixes, and code cleanup will come as v0.4.X.<br>
Once it's stable and has a good amount of compatibilities, it'll become v1.0.0.
