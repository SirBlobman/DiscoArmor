package com.github.sirblobman.disco.armor.menu;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AbstractMenu;
import com.github.sirblobman.api.nms.MultiVersionHandler;
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
    public LanguageManager getLanguageManager() {
        return this.plugin.getLanguageManager();
    }

    @Override
    public MultiVersionHandler getMultiVersionHandler() {
        return this.plugin.getMultiVersionHandler();
    }

    @Override
    public int getSize() {
        PatternManager patternManager = this.plugin.getPatternManager();
        List<String> patternIdList = patternManager.getPatternIds();
        return getSize(patternIdList.size());
    }

    @Override
    public Component getTitle() {
        Player player = getPlayer();
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        LanguageManager languageManager = plugin.getLanguageManager();
        return languageManager.getMessage(player, "menu-title");
    }

    @Override
    public ItemStack getItem(int slot) {
        DiscoArmorPattern pattern = getPattern(slot);
        if (pattern == null) {
            return null;
        }

        return pattern.getMenuIcon();
    }

    @Override
    public PatternButton getButton(int slot) {
        DiscoArmorPattern pattern = getPattern(slot);
        if (pattern == null) {
            return null;
        }

        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        return new PatternButton(plugin, pattern);
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private DiscoArmorPlugin getDiscoArmorPlugin() {
        return this.plugin;
    }

    private DiscoArmorPattern getPattern(int slot) {
        DiscoArmorPlugin plugin = getDiscoArmorPlugin();
        PatternManager patternManager = plugin.getPatternManager();
        List<DiscoArmorPattern> patternList = patternManager.getPatterns();
        int patternListSize = patternList.size();

        if (slot < 0 || slot >= patternListSize) {
            return null;
        }

        return patternList.get(slot);
    }

    private int getSize(int number) {
        if (number <= 9) {
            return 9;
        }

        if (number <= 18) {
            return 18;
        }

        if (number <= 27) {
            return 27;
        }

        if (number <= 36) {
            return 36;
        }

        if (number <= 45) {
            return 45;
        }

        return 54;
    }
}
