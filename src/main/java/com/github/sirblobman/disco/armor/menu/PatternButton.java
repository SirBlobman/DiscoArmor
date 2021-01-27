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
import com.github.sirblobman.disco.armor.pattern.Pattern;

public class PatternButton extends QuickButton {
    private final Pattern pattern;
    private final DiscoArmorPlugin plugin;
    public PatternButton(DiscoArmorPlugin plugin, Pattern pattern) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.pattern = Validate.notNull(pattern, "pattern must not be null!");
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        player.closeInventory();

        Pattern pattern = getPattern();
        String patternId = pattern.getId();

        PlayerDataManager playerDataManager = this.plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        LanguageManager languageManager = this.plugin.getLanguageManager();
        Replacer replacer = message -> message.replace("{pattern}", MessageUtility.color(pattern.getDisplayName()));
        languageManager.sendMessage(player, "command.change-type", replacer, true);
    }
}