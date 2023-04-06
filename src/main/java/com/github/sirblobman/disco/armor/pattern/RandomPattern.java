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

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class RandomPattern extends DiscoArmorPattern {
    public RandomPattern(DiscoArmorPlugin plugin) {
        super(plugin, "random");
    }

    @Override
    public Component getDisplayName() {
        LanguageManager languageManager = getLanguageManager();
        MiniMessage miniMessage = languageManager.getMiniMessage();
        return miniMessage.deserialize("<rainbow:!>Random Color</rainbow>");
    }

    @Override
    protected Color getNextColor(Player player) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int red = rng.nextInt(256);
        int green = rng.nextInt(256);
        int blue = rng.nextInt(256);
        return Color.fromRGB(red, green, blue);
    }

    @Override
    public Map<ArmorType, ItemStack> getNextArmor(Player player) {
        Map<ArmorType, ItemStack> armorMap = new EnumMap<>(ArmorType.class);
        ArmorType[] armorTypeArray = ArmorType.values();
        for (ArmorType armorType : armorTypeArray) {
            Color nextColor = getNextColor(player);
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
