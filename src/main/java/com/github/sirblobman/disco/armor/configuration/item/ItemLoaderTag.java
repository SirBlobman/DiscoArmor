package com.github.sirblobman.disco.armor.configuration.item;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.plugin.IMultiVersionPlugin;

public final class ItemLoaderTag extends ItemLoaderConfigurable {
    public ItemLoaderTag(@NotNull IMultiVersionPlugin plugin) {
        super(plugin);
    }

    @Override
    public @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String tagKeyString = section.getString("tag", "minecraft:wool");
        NamespacedKey tagKey = NamespacedKey.fromString(tagKeyString);
        if (tagKey == null) {
            logger.warning("Invalid tag key format '" + tagKeyString + "'.");
            return null;
        }

        Tag<Material> tag = findBlockOrItemTag(tagKey);
        if (tag == null) {
            logger.warning("Unknown or invalid tag key '" + tagKeyString + "'.");
            return null;
        }

        Set<Material> materialSet = tag.getValues();
        if (materialSet.isEmpty()) {
            logger.warning("Tag key '" + tagKeyString + "' is empty or does not contain materials.");
            return null;
        }

        List<Material> materialList = List.copyOf(materialSet);
        int materialListSize = materialList.size();

        Random random = ThreadLocalRandom.current();
        Material material = materialList.get(random.nextInt(materialListSize));
        NamespacedKey materialKey = material.getKey();

        section.set("id", materialKey.toString());
        return super.loadItemStack(section);
    }

    private @Nullable Tag<Material> findBlockOrItemTag(@NotNull NamespacedKey key) {
        Tag<Material> tagBlocks = Bukkit.getTag(Tag.REGISTRY_BLOCKS, key, Material.class);
        if (tagBlocks != null) {
            return tagBlocks;
        }

        return Bukkit.getTag(Tag.REGISTRY_ITEMS, key, Material.class);
    }
}
