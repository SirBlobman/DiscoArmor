package com.github.sirblobman.disco.armor.configuration.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.plugin.IMultiVersionPlugin;

public final class ItemLoaderBase64 extends ItemLoader {
    public ItemLoaderBase64(@NotNull IMultiVersionPlugin plugin) {
        super(plugin);
    }

    @Override
    public @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        String base64String = section.getString("base64");
        if (base64String == null || base64String.isBlank()) {
            return null;
        }

        IMultiVersionPlugin multiVersionPlugin = getMultiVersionPlugin();
        MultiVersionHandler multiVersionHandler = multiVersionPlugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        return itemHandler.fromBase64String(base64String);
    }
}
