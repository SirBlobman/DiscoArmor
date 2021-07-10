package com.github.sirblobman.disco.armor.menu;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.menu.button.QuickButton;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class PatternButton extends QuickButton {
    private final DiscoArmorPattern pattern;
    private final DiscoArmorPlugin plugin;

    public PatternButton(DiscoArmorPlugin plugin, DiscoArmorPattern pattern) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.pattern = Validate.notNull(pattern, "pattern must not be null!");
    }

    public DiscoArmorPattern getPattern() {
        return this.pattern;
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        player.closeInventory();

        DiscoArmorPattern pattern = getPattern();
        String patternId = pattern.getId();

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        LanguageManager languageManager = this.plugin.getLanguageManager();
        String patternDisplayName = MessageUtility.color(pattern.getDisplayName());
        Replacer replacer = message -> message.replace("{pattern}",patternDisplayName);
        languageManager.sendMessage(player, "command.change-type", replacer, true);
    }
}
