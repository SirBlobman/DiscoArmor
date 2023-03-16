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
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.item.ArmorType;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.disco.armor.DiscoArmorPlugin;

public final class RainbowPattern extends DiscoArmorPattern {
    private final List<Color> rainbowColorList;

    public RainbowPattern(DiscoArmorPlugin plugin) {
        super(plugin, "rainbow");
        this.rainbowColorList = new ArrayList<>();
        this.rainbowColorList.add(Color.fromRGB(0xFF0000)); // Red
        this.rainbowColorList.add(Color.fromRGB(0xFFA500)); // Orange
        this.rainbowColorList.add(Color.fromRGB(0xFFFF00)); // Yellow
        this.rainbowColorList.add(Color.fromRGB(0x008000)); // Green
        this.rainbowColorList.add(Color.fromRGB(0x0000FF)); // Blue
        this.rainbowColorList.add(Color.fromRGB(0x4B0082)); // Indigo
        this.rainbowColorList.add(Color.fromRGB(0xEE82EE)); // Violet
    }

    @Override
    public Component getDisplayName() {
        LanguageManager languageManager = getLanguageManager();
        MiniMessage miniMessage = languageManager.getMiniMessage();
        return miniMessage.deserialize("<rainbow>Rainbow</rainbow>");
    }

    @Override
    protected Color getNextColor(Player player) {
        int colorListSize = this.rainbowColorList.size();
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        return this.rainbowColorList.get(rng.nextInt(colorListSize));
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
