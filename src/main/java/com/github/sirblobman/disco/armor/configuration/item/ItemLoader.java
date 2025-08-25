package com.github.sirblobman.disco.armor.configuration.item;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.plugin.IMultiVersionPlugin;

public abstract class ItemLoader {
    private final IMultiVersionPlugin plugin;

    public ItemLoader(@NotNull IMultiVersionPlugin plugin) {
        this.plugin = plugin;
    }

    protected @NotNull IMultiVersionPlugin getMultiVersionPlugin() {
        return this.plugin;
    }

    protected @NotNull Logger getLogger() {
        return getMultiVersionPlugin().getPlugin().getLogger();
    }

    public abstract @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section);
}
