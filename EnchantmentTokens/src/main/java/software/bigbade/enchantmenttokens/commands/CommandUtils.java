package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;

public class CommandUtils {
    public static final String NOPERMISSION = new TranslatedTextMessage("command.error.permission").getText();
    public static final String TOOMANYARGUMENTS = new TranslatedTextMessage("command.error.arguments").getText();
    public static final TranslatedTextMessage NOPLAYER = new TranslatedTextMessage("command.error.noplayer");
    public static final TranslatedTextMessage NOTANUMBER = new TranslatedTextMessage("command.error.notnumber");
}
