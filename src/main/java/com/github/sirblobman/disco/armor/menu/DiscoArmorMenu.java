package com.github.sirblobman.disco.armor.menu;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.menu.AbstractMenu;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.manager.PatternManager;
import com.github.sirblobman.disco.armor.pattern.DiscoArmorPattern;

public final class DiscoArmorMenu extends AbstractMenu {
    private final DiscoArmorPlugin plugin;

    public DiscoArmorMenu(DiscoArmorPlugin plugin, Player player) {
        super(plugin, player);
        this.plugin = plugin;
    }

    @Override
    public int getSize() {
        PatternManager patternManager = this.plugin.getPatternManager();
        List<String> patternIdList = patternManager.getPatternIds();
        return getSize(patternIdList.size());
    }

    @Override
    public String getTitle() {
        ConfigurationManager configurationManager = this.plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return configuration.getString("menu-title");
    }

    @Override
    public ItemStack getItem(int slot) {
        DiscoArmorPattern pattern = getPattern(slot);
        if(pattern == null) {
            return null;
        }

        return pattern.getMenuIcon();
    }

    @Override
    public PatternButton getButton(int slot) {
        DiscoArmorPattern pattern = getPattern(slot);
        if(pattern == null) {
            return null;
        }

        return new PatternButton(this.plugin, pattern);
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private DiscoArmorPattern getPattern(int slot) {
        PatternManager patternManager = this.plugin.getPatternManager();
        List<DiscoArmorPattern> patternList = patternManager.getPatterns();
        int patternListSize = patternList.size();
        if(slot < 0 || slot >= patternListSize) {
            return null;
        }

        return patternList.get(slot);
    }

    private int getSize(int number) {
        if(number <= 9) {
            return 9;
        }

        if(number <= 18) {
            return 18;
        }

        if(number <= 27) {
            return 27;
        }

        if(number <= 36) {
            return 36;
        }

        if(number <= 45) {
            return 45;
        }

        return 54;
    }
}
