package com.github.sirblobman.disco.armor.configuration.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.plugin.IMultiVersionPlugin;

public final class ItemLoaderYaml extends ItemLoader {
    public ItemLoaderYaml(@NotNull IMultiVersionPlugin plugin) {
        super(plugin);
    }

    @Override
    public @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        return section.getItemStack("item");
    }
}
