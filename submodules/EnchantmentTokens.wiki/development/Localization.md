Strings can be localized through the TranslatedMessage class.

Call TranslatedMessage#translate(String key, String namespace, String... arguments).

The key should be the key in the .properties file,
the namespace should be the EnchantmentAddon name.
The arguments replace any %s in the string.

You can use an enum to store values like the name to prevent mistyping.
