FateUBW 1.1.0
================
- Start of combat update. Other servants will follow
  - Updated artoria, gilgamesh, emiya, cuchulainn, gilles (missing nobel phantasm)
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