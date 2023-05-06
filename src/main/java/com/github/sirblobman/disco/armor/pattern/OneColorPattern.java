package com.github.sirblobman.disco.armor.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class OneColorPattern extends DiscoArmorPattern {
    public OneColorPattern(@NotNull DiscoArmorPlugin plugin) {
        super(plugin, "one_color");
    }

    @Override
    public @NotNull Map<ArmorType, ItemStack> getNextArmor(@NotNull Player player) {
        Color nextColor = getNextColor(player);
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        ArmorType[] armorTypeArray = ArmorType.values();

        for (ArmorType armorType : armorTypeArray) {
            ItemStack armor = createArmor(player, armorType, nextColor);
            armorMap.put(armorType, armor);
        }

        return Collections.unmodifiableMap(armorMap);
    }

    @Override
    protected @NotNull Color getNextColor(@NotNull Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int randomColor = rng.nextInt(0xFFFFFF + 1);
        return Color.fromRGB(randomColor);
    }

    @Override
    protected @NotNull ItemStack getMenuItem() {
        Set<Material> bannerSet = Tag.ITEMS_BANNERS.getValues();
        List<Material> bannerList = new ArrayList<>(bannerSet);
        int bannerListSize = bannerList.size();

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Material bannerType = bannerList.get(rng.nextInt(bannerListSize));
        return new ItemStack(bannerType, 1);
    }
}
