package software.bigbade.enchantmenttokens.localization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public class AddonResourceBundle {
    @Getter
    private final String addon;
    @Getter
    private final ResourceBundle bundle;
}
