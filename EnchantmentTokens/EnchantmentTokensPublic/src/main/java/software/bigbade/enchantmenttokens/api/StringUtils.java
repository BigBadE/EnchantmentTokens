/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.api;

/**
 * StringUtils loads all localization strings of the selected language into memory.
 */
public class StringUtils {
    //Private constructor to hide implicit public one
    private StringUtils() {}

    /**
     * General messages
     */
    public static final String ENCHANTMENT = "enchantment";
    //Args: enchant name
    public static final String ENCHANTMENT_ADD = "enchantment.add";
    public static final String ENCHANTMENT_ADD_FAIL = "enchantment.add.fail";
    public static final String MAXED = "enchantment.max";
    public static final String MAXED_MESSAGE = "enchantment.max.message";
    public static final String NOT_APPLICABLE = "enchantment.not-applicable";
    //Args: enchant name, level
    public static final String ENCHANTMENT_BOUGHT_SUCCESS = "enchantment.bought.success";
    //Args: price
    public static final String ENCHANTMENT_BOUGHT_FAIL = "enchantment.bought.fail";
    //Args: price
    public static final String PRICE = "enchantment.price";
    //Args: level
    public static final String LEVEL = "enchantment.level";
    public static final String PRICE_MAXED = "enchantment.price.maxed";
    //ARgs: amount of gems
    public static final String GEMS_FIND = "enchantment.gems.get";

    /**
     * GUI Messages
     */
    public static final String GUI_CONFIRM = "enchant.confirm";
    public static final String GUI_CANCEL = "enchant.cancel";
    public static final String GUI_BACK = "enchant.back";

    /**
     * Tool names
     */
    //Args: tool name
    public static final String TOOL_ENCHANTS = "tool.enchants";
    public static final String TOOL_CROSSBOW = "tool.crossbow";
    public static final String TOOL_TRIDENT = "tool.trident";
    public static final String TOOL_FISHING_ROD = "tool.fishing-rod";
    public static final String TOOL_TOOLS = "tool.tool";
    public static final String TOOL_SWORD = "tool.sword";
    public static final String TOOL_ARMOR = "tool.armor";
    public static final String TOOL_BOW = "tool.bow";
    public static final String TOOL_SHIELD = "tool.shield";

    /**
     * Command messages
     */
    public static final String COMMAND_ERROR_PERMISSION = "command.error.permission";
    //Args: incorrect argument
    public static final String COMMAND_ERROR_NOT_NUMBER = "command.error.not-number";
    //Args: name
    public static final String COMMAND_ERROR_NO_PLAYER = "command.error.no-player";
    public static final String COMMAND_ERROR_TOO_MANY_ARGUMENTS = "command.error.arguments";
    public static final String COMMAND_ERROR_NO_ENCHANTMENT = "command.error.no-enchantment";

    //Args: amount added
    public static final String COMMAND_ADD = "command.add";
    //Args: player balance
    public static final String COMMAND_BALANCE = "command.balance";
    public static final String COMMAND_ENCHANT_USAGE = "command.enchant.usage";
    public static final String COMMAND_ENCHANT_HELD = "command.enchant.held";
    public static final String COMMAND_PAY_USAGE = "command.pay.usage";
    //Args: minimum payment
    public static final String COMMAND_PAY_NOT_ENOUGH = "command.pay.not-enough";
    //Args: amount, receiver
    public static final String COMMAND_PAY = "command.pay";
    //Args: amount, sender
    public static final String COMMAND_PAY_RECEIVE = "command.pay.receive";
    //Args: list of enchants
    public static final String COMMAND_LIST = "command.list";
    public static final String COMMAND_ENCHANTMENT_TOKENS_USAGE = "command.enchantment-tokens.usage";
    public static final String COMMAND_ENCHANTMENT_TOKENS_LANGUAGES = "command.enchantment-tokens.languages";

    public static final String DOLLAR_SYMBOL = "dollar.symbol";
    public static final String GEMS_SYMBOL = "gems.symbol";
    public static final String LEVELS = "levels";

    /**
     * Strings used for configuration.
     */
    public static final String INCREASE = "increase";

    /**
     * Strings used for permissions
     */
    public static final String PERMISSION_ADMIN = "enchanttoken.admin";
    public static final String PERMISSION_LIST = "enchanttoken.list";
}
