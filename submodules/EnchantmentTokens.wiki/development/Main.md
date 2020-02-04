Creating a main class:

Every main class must extend EnchantmentAddon.

You have access to onEnable and onDisable, just like a normal Bukkit plugin.

You do not have access to the main class.

Each EnchantmentAddon must have a name. This is used for the Configuration File.

Fields can be annotated with ConfigurationField and [EnchantmentListener](development/EnchantmentListener.md).