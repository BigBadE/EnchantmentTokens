Making an enchantment is a pretty simple, yet flexible process.
Your enchantment class must extend EnchantmentBase. 

The EnchantmentBase constructor is like this:

Name, Icon, Namespace (Optional)

Name is shown in the item lore, enchantment menu, and signs.

The Icon is used for the CustomEnchant command.

The Namespace should be set to the name of the addon.

You then have to set the item target. You have two options:
EnchantmentBase#addTargets()
EnchantmentBase#setTarget()

addTargets is used for materials. The MaterialGroupUtils class has an array of each item type. You can add individual materials too.

setTarget sets a singular enchantment target. This is useful for things like pickaxes, but doesn't have an option for non-vanilla enchantable items like shields.

The next step is setting up event listeners and configuration values.

See [Enchantment Listeners](development/EnchantmentListener.md) and [Configuration](development/Configuration.md)
