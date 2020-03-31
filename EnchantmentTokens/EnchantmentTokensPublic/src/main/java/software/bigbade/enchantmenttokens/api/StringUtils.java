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

import software.bigbade.enchantmenttokens.localization.TranslatedStringMessage;

/**
 * StringUtils loads all localization strings of the selected language into memory.
 */
public class StringUtils {
    //Private constructor to hide implicit public one
    private StringUtils() {}

    /**
     * General messages
     */
    public static final String ENCHANTMENT = new TranslatedStringMessage("enchantment").translate();
    //Args: enchant name
    public static final TranslatedStringMessage ENCHANTMENT_ADD = new TranslatedStringMessage("enchantment.add");
    public static final String ENCHANTMENT_ADD_FAIL = new TranslatedStringMessage("enchantment.add.fail").translate();
    public static final String MAXED = new TranslatedStringMessage("enchantment.max").translate();
    public static final String MAXED_MESSAGE = new TranslatedStringMessage("enchantment.max.message").translate();
    public static final String NOT_APPLICABLE = new TranslatedStringMessage("enchantment.not-applicable").translate();
    //Args: enchant name, level
    public static final TranslatedStringMessage ENCHANTMENT_BOUGHT_SUCCESS = new TranslatedStringMessage("enchantment.bought.success");
    //Args: price
    public static final TranslatedStringMessage ENCHANTMENT_BOUGHT_FAIL = new TranslatedStringMessage("enchantment.bought.fail");
    //Args: price
    public static final TranslatedStringMessage PRICE = new TranslatedStringMessage("enchantment.price");
    //Args: level
    public static final TranslatedStringMessage LEVEL = new TranslatedStringMessage("enchantment.level");
    public static final String PRICE_MAXED = new TranslatedStringMessage("enchantment.price.maxed").translate();
    //ARgs: amount of gems
    public static final TranslatedStringMessage GEMS_FIND = new TranslatedStringMessage("enchantment.gems.get");

    /**
     * GUI Messages
     */
    public static final String GUI_CONFIRM = new TranslatedStringMessage("enchant.confirm").translate();
    public static final String GUI_CANCEL = new TranslatedStringMessage("enchant.cancel").translate();
    public static final String GUI_BACK = new TranslatedStringMessage("enchant.back").translate();

    /**
     * Tool names
     */
    //Args: tool name
    public static final TranslatedStringMessage TOOL_ENCHANTS = new TranslatedStringMessage("tool.enchants");
    public static final String TOOL_CROSSBOW = new TranslatedStringMessage("tool.crossbow").translate();
    public static final String TOOL_TRIDENT = new TranslatedStringMessage("tool.trident").translate();
    public static final String TOOL_FISHING_ROD = new TranslatedStringMessage("tool.fishing-rod").translate();
    public static final String TOOL_TOOLS = new TranslatedStringMessage("tool.tool").translate();
    public static final String TOOL_SWORD = new TranslatedStringMessage("tool.sword").translate();
    public static final String TOOL_ARMOR = new TranslatedStringMessage("tool.armor").translate();
    public static final String TOOL_BOW = new TranslatedStringMessage("tool.bow").translate();
    public static final String TOOL_SHIELD = new TranslatedStringMessage("tool.shield").translate();

    /**
     * Command messages
     */
    public static final String COMMAND_ERROR_PERMISSION = new TranslatedStringMessage("command.error.permission").translate();
    //Args: incorrect argument
    public static final TranslatedStringMessage COMMAND_ERROR_NOT_NUMBER = new TranslatedStringMessage("command.error.not-number");
    //Args: name
    public static final TranslatedStringMessage COMMAND_ERROR_NO_PLAYER = new TranslatedStringMessage("command.error.no-player");
    public static final String COMMAND_ERROR_TOO_MANY_ARGUMENTS = new TranslatedStringMessage("command.error.arguments").translate();
    public static final String COMMAND_ERROR_NO_ENCHANTMENT = new TranslatedStringMessage("command.error.no-enchantment").translate();

    //Args: amount added
    public static final TranslatedStringMessage COMMAND_ADD = new TranslatedStringMessage("command.add");
    //Args: player balance
    public static final TranslatedStringMessage COMMAND_BALANCE = new TranslatedStringMessage("command.balance");
    public static final String COMMAND_ENCHANT_USAGE = new TranslatedStringMessage("command.enchant.usage").translate();
    public static final String COMMAND_ENCHANT_HELD = new TranslatedStringMessage("command.enchant.held").translate();
    public static final String COMMAND_PAY_USAGE = new TranslatedStringMessage("command.pay.usage").translate();
    //Args: minimum payment
    public static final TranslatedStringMessage COMMAND_PAY_NOT_ENOUGH = new TranslatedStringMessage("command.pay.not-enough");
    //Args: amount, receiver
    public static final TranslatedStringMessage COMMAND_PAY = new TranslatedStringMessage("command.pay");
    //Args: amount, sender
    public static final TranslatedStringMessage COMMAND_PAY_RECEIVE = new TranslatedStringMessage("command.pay.receive");
    //Args: list of enchants
    public static final TranslatedStringMessage COMMAND_LIST = new TranslatedStringMessage("command.list");

    public static final TranslatedStringMessage DOLLAR_SYMBOL = new TranslatedStringMessage("dollar.symbol");
    public static final TranslatedStringMessage GEMS_SYMBOL = new TranslatedStringMessage("gems.symbol");
    public static final TranslatedStringMessage LEVELS = new TranslatedStringMessage("levels");

    /**
     * Strings used for configuration.
     */
    public static final String INCREASE = "increase";
}
