FateUBW 2.1.0-beta
================
- Larger weapon now have an inventory texture variant to not look too small in guis
- Fix gilles monster able to retaliate on owner
- Add config to change damage reduction for:
  - Armor (servants only)
  - Projectile Protection
  - Magic Protection
- Adjusted servant stats
- Updated animations/attacks. Most have a bit longer of a windup now
- Artoria:
  - Stab attack now hits multiple times
  - Most attacks always have a follow-up attack
  - Excalibur now hits multiple times (you should prob update its damage in the config. default now 15)
- Cu chulainn:
  - Added slash trail
  - Added a stab attack combo
  - Gáe Bolg has very strong homing now.
  - Gáe Bolg does additional 10% percentage damage based on targets current health
- Diarmuid:
  - Left spear now attacks first (won't really change much combat wise)
  - Now immune when unsealing his weapons
  - When in unsealed form gae dearg has partial armor piercing
- Emiya:
  - Add triple shot attack
  - Arrows ignore i-frames
  - Arrow barrage damage reduced and spread increased to balance above
  - Now immune when using caladbolg
- Gilgamesh:
  - Reduces his melee attacks, focuses more on ranged now
  - Melee attacks deal increased knockback
  - Stab now hits twice
  - Has a chance to guard with EA dealing damage and knockback now
  - EA now hits multiple times (you should prob update its damage in the config. default now 15)
- Gilles:
  - Some monster can use ranged attacks now
  - Can summon a tentacle slamming into target now
- Hassan:
  - Added slash trail
  - Increase clone stats
  - Clone count: 5 -> 4

FateUBW 2.0.0-beta
================
- Update to 1.21.1
- All mobs now use brains instead of goals
- Pathouli book now removed and replaced by modopedia
- For mod devs: Added api to more easily add servant entities without using internal implementations
- Renamed ids/names of all servants
- Changed servant weight calc for summoning
- Updated AI behaviour of various servants
- Entity models are now also changeable via resource pack
- Some servants wielding dual weapons now hit twice when attacking at the same tick
- Nobel phantasm now have a cooldown too in addition to mana cost so low cost ones can't be spammed too much
- Gilles monsters now have life drain

FateUBW 1.2.1
================
- Fix only one player able to join the war
- Don't delay the join using altar after the servant spawned as that messed up the state
- Target key also applies to all nearby owned servants
- Invalid players during join phase now don't take up a space anymore
  - Happens if e.g. the servant gets killed during join phase
  - Player won't be able to rejoin in that case to prevent rerolling your servant
- Add weight to servant properties to make some rarer. Although for now everything is 1 cause only few servants are implemented

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