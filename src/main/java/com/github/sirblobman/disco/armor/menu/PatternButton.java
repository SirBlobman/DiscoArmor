package com.github.sirblobman.disco.armor.menu;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.language.SimpleReplacer;
import com.github.sirblobman.api.menu.button.QuickButton;
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

    private DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private DiscoArmorPattern getPattern() {
        return this.pattern;
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        DiscoArmorPattern pattern = getPattern();
        String patternId = pattern.getId();

        String permissionName = ("disco-armor.pattern." + patternId);
        if(!player.hasPermission(permissionName)) {
            Replacer replacer = new SimpleReplacer("{pattern}", patternId);
            languageManager.sendMessage(player, "error.no-pattern-permission", replacer);
            return;
        }

        player.closeInventory();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        String patternDisplayName = pattern.getDisplayName();
        Replacer replacer = new SimpleReplacer("{pattern}", patternDisplayName);
        languageManager.sendMessage(player, "command.change-type", replacer);
    }
}
