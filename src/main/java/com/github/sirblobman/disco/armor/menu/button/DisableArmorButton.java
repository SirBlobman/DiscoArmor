package com.github.sirblobman.disco.armor.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.button.QuickButton;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;
import com.github.sirblobman.disco.armor.menu.DiscoArmorMainMenu;
import com.github.sirblobman.disco.armor.task.DiscoArmorTask;
import com.github.sirblobman.disco.armor.task.DiscoArmorTaskManager;

public final class DisableArmorButton extends QuickButton {
    private final DiscoArmorMainMenu menu;

    public DisableArmorButton(@NotNull DiscoArmorMainMenu menu) {
        this.menu = menu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        player.closeInventory();

        DiscoArmorPlugin plugin = getPlugin();
        LanguageManager languageManager = getLanguageManager();
        DiscoArmorTaskManager taskManager = plugin.getTaskManager();
        DiscoArmorTask task = taskManager.getTask(player);

        if (task == null || !task.isEnabled()) {
            languageManager.sendMessage(player, "error.armor-already-disabled");
            return;
        }

        task.disable();
    }

    private @NotNull DiscoArmorMainMenu getMenu() {
        return this.menu;
    }

    private @NotNull DiscoArmorPlugin getPlugin() {
        DiscoArmorMainMenu menu = getMenu();
        return menu.getPlugin();
    }

    private @NotNull LanguageManager getLanguageManager() {
        DiscoArmorPlugin plugin = getPlugin();
        return plugin.getLanguageManager();
    }
}
