package software.bigbade.enchantmenttokens.api;

import software.bigbade.enchantmenttokens.localization.TranslatedString;

/**
 * StringUtils loads all localization strings of the selected language into memory.
 */
public class StringUtils {
    //Private constructor to hide implicit public one
    private StringUtils() {}

    /**
     * General messages
     */
    public static final String ENCHANTMENT = new TranslatedString("enchantment").translate();
    //Args: enchant name
    public static final TranslatedString ENCHANTMENT_ADD = new TranslatedString("enchantment.add");
    public static final String ENCHANTMENT_ADD_FAIL = new TranslatedString("enchantment.add.fail").translate();
    public static final String MAXED = new TranslatedString("enchantment.max").translate();
    public static final String MAXED_MESSAGE = new TranslatedString("enchantment.max.message").translate();
    public static final String NOT_APPLICABLE = new TranslatedString("enchantment.not-applicable").translate();
    //Args: enchant name, level
    public static final TranslatedString ENCHANTMENT_BOUGHT_SUCCESS = new TranslatedString("enchantment.bought.success");
    //Args: price
    public static final TranslatedString ENCHANTMENT_BOUGHT_FAIL = new TranslatedString("enchantment.bought.fail");
    //Args: price
    public static final TranslatedString PRICE = new TranslatedString("enchantment.price");
    //Args: level
    public static final TranslatedString LEVEL = new TranslatedString("enchantment.level");
    public static final String PRICE_MAXED = new TranslatedString("enchantment.price.maxed").translate();
    //ARgs: amount of gems
    public static final TranslatedString GEMS_FIND = new TranslatedString("enchantment.gems.get");

    /**
     * GUI Messages
     */
    public static final String GUI_CONFIRM = new TranslatedString("enchant.confirm").translate();
    public static final String GUI_CANCEL = new TranslatedString("enchant.cancel").translate();
    public static final String GUI_BACK = new TranslatedString("enchant.back").translate();

    /**
     * Tool names
     */
    //Args: tool name
    public static final TranslatedString TOOL_ENCHANTS = new TranslatedString("tool.enchants");
    public static final String TOOL_CROSSBOW = new TranslatedString("tool.crossbow").translate();
    public static final String TOOL_TRIDENT = new TranslatedString("tool.trident").translate();
    public static final String TOOL_FISHING_ROD = new TranslatedString("tool.fishing-rod").translate();
    public static final String TOOL_TOOLS = new TranslatedString("tool.tool").translate();
    public static final String TOOL_SWORD = new TranslatedString("tool.sword").translate();
    public static final String TOOL_ARMOR = new TranslatedString("tool.armor").translate();
    public static final String TOOL_BOW = new TranslatedString("tool.bow").translate();
    public static final String TOOL_SHIELD = new TranslatedString("tool.shield").translate();

    /**
     * Command messages
     */
    public static final String COMMAND_ERROR_PERMISSION = new TranslatedString("command.error.permission").translate();
    //Args: incorrect argument
    public static final TranslatedString COMMAND_ERROR_NOT_NUMBER = new TranslatedString("command.error.not-number");
    //Args: name
    public static final TranslatedString COMMAND_ERROR_NO_PLAYER = new TranslatedString("command.error.no-player");
    public static final String COMMAND_ERROR_TOO_MANY_ARGUMENTS = new TranslatedString("command.error.arguments").translate();
    public static final String COMMAND_ERROR_NO_ENCHANTMENT = new TranslatedString("command.error.no-enchantment").translate();

    //Args: amount added
    public static final TranslatedString COMMAND_ADD = new TranslatedString("command.add");
    //Args: player balance
    public static final TranslatedString COMMAND_BALANCE = new TranslatedString("command.balance");
    public static final String COMMAND_ENCHANT_USAGE = new TranslatedString("command.enchant.usage").translate();
    public static final String COMMAND_ENCHANT_HELD = new TranslatedString("command.enchant.held").translate();
    public static final String COMMAND_PAY_USAGE = new TranslatedString("command.pay.usage").translate();
    //Args: minimum payment
    public static final TranslatedString COMMAND_PAY_NOT_ENOUGH = new TranslatedString("command.pay.not-enough");
    //Args: amount, receiver
    public static final TranslatedString COMMAND_PAY = new TranslatedString("command.pay");
    //Args: amount, sender
    public static final TranslatedString COMMAND_PAY_RECEIVE = new TranslatedString("command.pay.receive");
    //Args: list of enchants
    public static final TranslatedString COMMAND_LIST = new TranslatedString("command.list");

    public static final TranslatedString DOLLAR_SYMBOL = new TranslatedString("dollar.symbol");
    public static final TranslatedString GEMS_SYMBOL = new TranslatedString("gems.symbol");

    /**
     * Strings used for configuration.
     */
    public static final String INCREASE = "increase";
}
