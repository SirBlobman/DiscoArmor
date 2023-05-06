package com.github.sirblobman.disco.armor.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.button.QuickButton;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.DiscoArmorMainMenu;

public final class ToggleGlowButton extends QuickButton {
    private final DiscoArmorMainMenu menu;

    public ToggleGlowButton(@NotNull DiscoArmorMainMenu menu) {
        this.menu = menu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        player.closeInventory();

        PlayerDataManager playerDataManager = getPlayerDataManager();
        YamlConfiguration configuration = playerDataManager.get(player);
        boolean glowing = !configuration.getBoolean("glowing");

        configuration.set("glowing", glowing);
        playerDataManager.save(player);

        String messagePath = (glowing ? "glow.enabled" : "glow.disabled");
        LanguageManager languageManager = getLanguageManager();
        languageManager.sendMessage(player, messagePath);
    }

    private @NotNull DiscoArmorMainMenu getMenu() {
        return this.menu;
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        DiscoArmorMainMenu menu = getMenu();
        return menu.getPlugin();
    }

    private @NotNull PlayerDataManager getPlayerDataManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getPlayerDataManager();
    }

    private @NotNull LanguageManager getLanguageManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }
}
