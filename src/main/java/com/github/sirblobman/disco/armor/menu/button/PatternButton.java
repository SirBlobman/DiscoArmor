package com.github.sirblobman.disco.armor.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.replacer.ComponentReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.api.menu.button.QuickButton;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class PatternButton extends QuickButton {
    private final DiscoArmorPattern pattern;
    private final DiscoArmorPlugin plugin;

    public PatternButton(@NotNull DiscoArmorPlugin plugin, @NotNull DiscoArmorPattern pattern) {
        this.plugin = plugin;
        this.pattern = pattern;
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull DiscoArmorPattern getPattern() {
        return this.pattern;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        DiscoArmorPattern pattern = getPattern();
        String patternId = pattern.getId();

        String permissionName = ("disco-armor.pattern." + patternId);
        if (!player.hasPermission(permissionName)) {
            Replacer replacer = new StringReplacer("{pattern}", patternId);
            languageManager.sendMessage(player, "error.no-pattern-permission", replacer);
            return;
        }

        player.closeInventory();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        configuration.set("pattern", patternId);
        playerDataManager.save(player);

        Component displayName = pattern.getDisplayName(player);
        Replacer replacer = new ComponentReplacer("{pattern}", displayName);
        languageManager.sendMessage(player, "command.change-type", replacer);
    }
}
