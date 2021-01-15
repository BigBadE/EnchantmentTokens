EnchantmentTokens is a plugin allowing for easy addition of customized enchantments into Minecraft.

Latest Build: 

[![Latest Build](https://travis-ci.com/BigBadE/EnchantmentTokens.svg?branch=master)](https://travis-ci.com/BigBadE/EnchantmentTokens)

Code analysis:

[![BCH compliance](https://bettercodehub.com/edge/badge/BigBadE/EnchantmentTokens?branch=master)](https://bettercodehub.com/)
[![CodeScene Code Health](https://codescene.io/projects/6865/status-badges/code-health)](https://codescene.io/projects/6865)

Coverage (Needs to be added):

[![codecov](https://codecov.io/gh/BigBadE/EnchantmentTokens/branch/master/graph/badge.svg?token=1hsnZMVMJQ)](https://codecov.io/gh/BigBadE/EnchantmentTokens)

Current features:
- Add enchantments to items. Currently, it supports:
    - Armor
    - Swords
    - Bows
    - Tridents
    - Crossbows
    - Shields
    - Tools
    - Fishing Rod
    
- Allows for enchantments to be bought through:
    - Command GUI
    - Signs
    - Enchantment Table (beta)
    
- Supports tons of events (listed on wiki)
    
- Supported Currencies:
    - Gems (custom currency)
    - Experience levels
    - Vault

- Save methods:
    - PersistentData
    - Flat files
    - MySQL (https://github.com/BigBadE/EnchantmentTokensMySQL)
    - MongoDB  (https://github.com/BigBadE/EnchantmentTokensMongo)
    - Any others can be added, feel free to use MySQL/MongoDB as an example.

Building source:

```./gradlew shadowJar test```

No prerequisites, Gradle will automatically do everything.

If you are using an IDE, Lombok is used, make sure to install the appropriate plugin!
