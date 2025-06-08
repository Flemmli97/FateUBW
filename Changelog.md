FateUBW 1.2.0
================
- Removed truce system and replaced it with a team system
  - Players can create teams with other players
  - Teams can create alliances with each others
  - Servants will not attack allied players/servants
- Rewrite datapack entity properties
- Some entity config values (e.g. mob health) moved to datapack
- Rewrote some combat handling such as projectile blocking to be more generic
- Add combat and passive health regen (attribute)
  - Combat regen gets applied in combat, passive otherwise
- Rewrote the grailwar system
  - Instead of triggered by players a grailwar now happens based on time since the last one passed
- Servants in a grailwar do not drop loot now
- Update mana bar and config
- Remove command control item for the gui and replace with simple shift right click on servant action
- Add resummmon and servant loot drop entry to grail loot
- Various servant related configs are moved to datapack
- Reduced default deaths for heracles 3 -> 2. Also configurable now

FateUBW 1.1.2
================
- Updated ai for:
  - Heracles, Sasaki, Diarmuid, Medea
- Updated item/block texture resolution now to all use 16x16
- Changed katana id to monohoshi_zao
- Fix bow being stackable
- Updated some servant stats
- Updated Hassan to have a backstab movement
- Fix some damage sources tags
- Remove all chalk block states and replace it with a pure rendering solution

FateUBW 1.1.1
================
- Updated ai for:
  - Iskander, Lancelot, Medusa, Hassan
- Updated and fixed some textures
- Adjusted attack range for some attacks
- Update attributes of servants
- Update stats of various items
- Add a ranged attack for gilles
- Add magic attack attribute. Only few servants make use of that
- Updated various translation stuff

FateUBW 1.1.0
================
- Start of combat update. Other servants will follow
  - Updated Artoria, Gilgamesh, Emiya, Cuchulainn, Gilles (missing nobel phantasm)
  - Rest will follow
- Update dependencies
- Update pegasus model
- Changed ids of various things like items
- Add corruption render layer to lancelot items
- Updated gate of babylon and babylon weapons rendering
- Rewrite how servants and players are handled in grailwar which should fix missing servants
  - Its possible now (e.g. via spawn egg) to spawn an owned servant without joining a grailwar  
    They need a command seal item to be controlled in that case.
- Rewrite grail loot to use codecs
- Add command spell command `/fate command_spell` to give/take command spells
- Add ui for spawnegg. Replacing the need to rename a spawnegg

FateUBW 1.0.4
================
- Forge: Fixing problems with capability loading on player clone

FateUBW 1.0.3
================
- Fix player owned servants not chunk loading
- Fix emiyas bow missing texture
- Fix owned servants not tracked client side when out of render distance
- Fix servant names being rendered in fabric

FateUBW 1.0.2
================
- Emiyas blades are now rendered as dual weapons
- Fix some problems with tags
- Fix medeas beam sometimes at wrong location

FateUBW 1.0.1
================
- Fix heracles and medeas unkown texture
- Fix heracles staying sideways after death
- Fix emiya not shooting when using the bow
- Fix emiyas bow using wrong model
- Fix projectile prot calculation
- Fix player servant sometimes not loading

FateUBW 1.0.0
================
- 1.18 and release