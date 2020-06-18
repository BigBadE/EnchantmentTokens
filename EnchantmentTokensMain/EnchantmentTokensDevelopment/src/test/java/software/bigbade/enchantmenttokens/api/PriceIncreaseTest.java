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

import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.utils.math.AlgebraicCalculator;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PriceIncreaseTypes.class, AlgebraicCalculator.class})
public class PriceIncreaseTest {

    @Test
    public void testPriceIncreases() {
        ConfigurationSection section = mock(ConfigurationSection.class);
        when(section.getInt("0")).thenReturn(100);
        when(section.getObject("increase", Integer.class, 10)).thenReturn(10);
        Assert.assertEquals(100, PriceIncreaseTypes.CUSTOM.getPrice(0, section));
        Assert.assertEquals(30, PriceIncreaseTypes.LINEAR.getPrice(3, section));
    }
}
