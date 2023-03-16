package com.github.sirblobman.disco.armor.pattern;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.format.NamedTextColor;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class OneColorPattern extends DiscoArmorPattern {
    public OneColorPattern(DiscoArmorPlugin plugin) {
        super(plugin, "one_color");
    }

    @Override
    public Component getDisplayName() {
        return Component.text("One Color", NamedTextColor.YELLOW);
    }

    @Override
    protected Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int randomColor = rng.nextInt(0xFFFFFF + 1);
        return Color.fromRGB(randomColor);
    }

    @Override
    public Map<ArmorType, ItemStack> getNextArmor(Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        Color nextColor = getNextColor(player);

        ArmorType[] armorTypeArray = ArmorType.values();
        for (ArmorType armorType : armorTypeArray) {
            ItemStack armor = createArmor(player, armorType, nextColor);
            armorMap.put(armorType, armor);
        }

        return armorMap;
    }

    @Override
    protected ItemStack getMenuItem() {
        Tag<Material> bannerTag = Tag.ITEMS_BANNERS;
        Set<Material> bannerSet = bannerTag.getValues();
        List<Material> bannerList = new ArrayList<>(bannerSet);
        int bannerListSize = bannerList.size();

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Material bannerType = bannerList.get(rng.nextInt(bannerListSize));
        return new ItemStack(bannerType, 1);
    }
}
