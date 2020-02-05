Any field annotated with @ConfigurationField is automatically loaded from the configuration.

For the main EnchantmentAddon class, this is loaded from the base file.

For EnchantmentBases, it is inside the enchants.(name) section.

the value of the field is the default value.

The annotation has a "location" value. This can be used for subsections. For example:

@ConfigurationField("test")

Will check enchants.(name).test for the value "value".

NOTE: This will NOT be saved on server stopping. 
If you would like to set values, set them in EnchantmentBase#loadConfig().
You have to get the section using a ConfigurationField with a type of ConfigurationSection and set it that way.
Changing the value of the field will NOT update the configuration.