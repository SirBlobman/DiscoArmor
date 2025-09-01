package com.github.sirblobman.disco.armor.configuration.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.ShieldMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.tag.DamageTypeTags;
import org.bukkit.util.NumberConversions;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

import com.github.sirblobman.api.plugin.IMultiVersionPlugin;
import com.github.sirblobman.api.utility.ConfigurationHelper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ItemLoaderConfigurable extends ItemLoader {
    private final MiniMessage miniMessage;

    public ItemLoaderConfigurable(@NotNull IMultiVersionPlugin plugin) {
        super(plugin);
        this.miniMessage = MiniMessage.miniMessage();
    }

    private @NotNull MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    @Override
    public @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String itemId = section.getString("id", "minecraft:barrier");
        NamespacedKey itemIdKey = NamespacedKey.fromString(itemId);
        if (itemIdKey == null) {
            logger.warning("Invalid item id key format '" + itemId + "'.");
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull ItemType> registry = registryAccess.getRegistry(RegistryKey.ITEM);
        ItemType itemType = registry.get(itemIdKey);
        if (itemType == null) {
            logger.warning("Unknown or unregistered item id key '" + itemId + "'.");
            return null;
        }

        int quantity = section.getInt("quantity", 1);
        ItemStack itemStack = itemType.createItemStack(quantity);

        // Common Item Meta
        loadAttributeModifiers(itemStack, section);
        loadEnchantments(itemStack, section);
        loadItemFlags(itemStack, section);
        loadHideTooltip(itemStack, section);
        loadTooltipStyle(itemStack, section);
        loadDisplayName(itemStack, section);
        loadItemName(itemStack, section);
        loadLore(itemStack, section);
        loadItemModel(itemStack, section);
        loadCustomModelData(itemStack, section);
        loadGlowing(itemStack, section);
        loadUnbreakable(itemStack, section);
        loadGlider(itemStack, section);
        loadDamageResistances(itemStack, section);
        loadMaxStackSize(itemStack, section);
        loadRarity(itemStack, section);
        loadEnchantable(itemStack, section);

        // Component Meta
        loadUseCooldownComponent(itemStack, section);
        loadFoodComponent(itemStack, section);
        loadToolComponent(itemStack, section);
        loadEquippableComponent(itemStack, section);
        loadJukeboxPlayableComponent(itemStack, section);

        // Other Meta
        loadArmorMeta(itemStack, section);
        loadAxolotlBucketMeta(itemStack, section);
        loadBannerMeta(itemStack, section);
        loadBookMeta(itemStack, section);
        loadBundleMeta(itemStack, section);
        loadCompassMeta(itemStack, section);
        loadDamageableMeta(itemStack, section);
        loadEnchantmentStorageMeta(itemStack, section);
        loadFireworkEffectMeta(itemStack, section);
        loadFireworkMeta(itemStack, section);
        loadKnowledgeBookMeta(itemStack, section);
        loadLeatherArmorMeta(itemStack, section);
        loadMusicInstrumentMeta(itemStack, section);
        loadOminousBottleMeta(itemStack, section);
        loadPotionMeta(itemStack, section);
        loadRepairableMeta(itemStack, section);
        loadShieldMeta(itemStack, section);
        loadSkullMeta(itemStack, section);
        loadSuspiciousStewMeta(itemStack, section);
        loadTropicalFishBucketMeta(itemStack, section);
        loadWritableBookMeta(itemStack, section);

        return itemStack;
    }

    private <T extends ConfigurationSerializable> @NotNull List<T> parseSerializableList(@NotNull Class<T> clazz, @NotNull ConfigurationSection section, @NotNull String path) {
        List<?> objectList = section.getList(path);
        if (objectList == null) {
            return new ArrayList<>();
        }

        List<T> serializableList = new ArrayList<>(objectList.size());
        for (Object object : objectList) {
            if (clazz.isInstance(object)) {
                T serializable = clazz.cast(object);
                serializableList.add(serializable);
            }
        }

        return serializableList;
    }

    private void loadAttributeModifiers(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection attributeModifiersSection = section.getConfigurationSection("attribute-modifiers");
        if (attributeModifiersSection == null) {
            return;
        }

        Set<String> attributeModifierConfigurationKeySet = attributeModifiersSection.getKeys(false);
        for (String configurationKey : attributeModifierConfigurationKeySet) {
            ConfigurationSection attributeModifierSection = attributeModifiersSection.getConfigurationSection(configurationKey);
            if (attributeModifierSection == null) {
                continue;
            }

            loadAttributeModifier(itemMeta, attributeModifierSection);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadAttributeModifier(@NotNull ItemMeta meta, @NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String attributeId = section.getString("attribute", "minecraft:scale");
        NamespacedKey attributeIdKey = NamespacedKey.fromString(attributeId);
        if (attributeIdKey == null) {
            logger.info("Invalid attribute id format '" + attributeId + "'.");
            return;
        }

        Attribute attribute = Registry.ATTRIBUTE.get(attributeIdKey);
        if (attribute == null) {
            logger.info("Unknown or unregistered attribute id '" + attributeId + "'.");
            return;
        }

        String attributeKeyString = section.getString("id", "custom:custom");
        NamespacedKey attributeKey = NamespacedKey.fromString(attributeKeyString);
        if (attributeKey == null) {
            logger.info("Invalid attribute id format '" + attributeKeyString + "'.");
            return;
        }

        double value = section.getDouble("value", 0.0D);
        AttributeModifier.Operation operation = loadAttributeModifierOperation(section);
        EquipmentSlotGroup slotGroup = loadAttributeModifierEquipmentSlotGroup(section);
        AttributeModifier attributeModifier = new AttributeModifier(attributeKey, value, operation, slotGroup);
        meta.addAttributeModifier(attribute, attributeModifier);
    }

    @SuppressWarnings("UnstableApiUsage")
    private @NotNull EquipmentSlotGroup loadAttributeModifierEquipmentSlotGroup(@NotNull ConfigurationSection section) {
        String slotGroupName = section.getString("slot-group");
        if (slotGroupName == null || slotGroupName.isBlank()) {
            return EquipmentSlotGroup.ANY;
        }

        EquipmentSlotGroup byName = EquipmentSlotGroup.getByName(slotGroupName);
        return (byName == null ? EquipmentSlotGroup.ANY : byName);
    }

    private @NotNull AttributeModifier.Operation loadAttributeModifierOperation(@NotNull ConfigurationSection section) {
        String operationName = section.getString("operation", "ADD_NUMBER");
        AttributeModifier.Operation defaultValue = AttributeModifier.Operation.ADD_NUMBER;
        return ConfigurationHelper.parseEnum(AttributeModifier.Operation.class, operationName, defaultValue);
    }

    private void loadEnchantments(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
        if (enchantmentsSection == null) {
            return;
        }

        Set<String> enchantmentConfigurationKeySet = enchantmentsSection.getKeys(false);
        for (String configurationKey : enchantmentConfigurationKeySet) {
            ConfigurationSection enchantmentSection = enchantmentsSection.getConfigurationSection(configurationKey);
            if (enchantmentSection == null) {
                continue;
            }

            loadEnchantment(itemMeta, section);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadEnchantment(@NotNull ItemMeta itemMeta, @NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String enchantmentKeyString = section.getString("id", "minecraft:sharpness");
        NamespacedKey enchantmentKey = NamespacedKey.fromString(enchantmentKeyString);
        if (enchantmentKey == null) {
            logger.warning("Invalid enchantment id format '" + enchantmentKeyString + "'.");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull Enchantment> registry = registryAccess.getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment enchantment = registry.get(enchantmentKey);
        if (enchantment == null) {
            logger.warning("Unknown or unregistered enchantment id '" + enchantmentKeyString + "'.");
            return;
        }

        if (itemMeta.hasConflictingEnchant(enchantment)) {
            logger.warning("This item has an enchantment that conflicts with '" + enchantmentKeyString + "'.");
        }

        int level = section.getInt("level");
        boolean unsafe = section.getBoolean("unsafe", false);
        boolean enchant = itemMeta.addEnchant(enchantment, level, unsafe);
        if (!enchant) {
            logger.info("Failed to enchant with enchantment '"
                    + enchantmentKeyString + "' and level '" + level + "'.");
        }
    }

    private void loadItemFlags(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        List<String> flagNameList = section.getStringList("flags");
        Set<ItemFlag> itemFlagSet = ConfigurationHelper.parseEnums(flagNameList, ItemFlag.class);
        ItemFlag[] itemFlagArray = itemFlagSet.toArray(ItemFlag[]::new);

        itemMeta.addItemFlags(itemFlagArray);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadHideTooltip(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean hideTooltip = section.getBoolean("hide-tooltip", false);
        itemMeta.setHideTooltip(hideTooltip);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadTooltipStyle(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String tooltipStyleKeyString = section.getString("tooltip-style");
        if (tooltipStyleKeyString == null || tooltipStyleKeyString.isBlank()) {
            return;
        }

        NamespacedKey tooltipStyleKey = NamespacedKey.fromString(tooltipStyleKeyString);
        if (tooltipStyleKey == null) {
            getLogger().warning("Invalid tooltip style key format '" + tooltipStyleKeyString + "'.");
            return;
        }

        itemMeta.setTooltipStyle(tooltipStyleKey);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadDisplayName(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("display-name")) {
            return;
        }

        String displayNameString = section.getString("display-name");
        if (displayNameString == null || displayNameString.isBlank()) {
            return;
        }

        Component displayName = parseComponent(displayNameString);
        itemMeta.displayName(displayName);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadItemName(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("item-name")) {
            return;
        }

        String itemNameString = section.getString("item-name");
        if (itemNameString == null || itemNameString.isBlank()) {
            return;
        }

        Component itemName = parseComponent(itemNameString);
        itemMeta.itemName(itemName);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private @NotNull Component parseComponent(@NotNull String text) {
        if (text.contains("&")) {
            LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
            return legacy.deserialize(text);
        }

        if (text.contains("\u00A7")) {
            LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
            return legacy.deserialize(text);
        }

        MiniMessage miniMessage = getMiniMessage();
        return miniMessage.deserialize(text);
    }

    private @NotNull List<Component> parseComponents(@NotNull List<String> textList) {
        int textListSize = textList.size();
        List<Component> componentList = new ArrayList<>(textListSize);
        for (String text : textList) {
            Component component = parseComponent(text);
            componentList.add(component);
        }

        return componentList;
    }

    private void loadLore(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("lore")) {
            return;
        }

        List<String> loreStringList = section.getStringList("lore");
        if (loreStringList.isEmpty()) {
            return;
        }

        List<Component> loreList = parseComponents(loreStringList);
        itemMeta.lore(loreList);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadItemModel(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String itemModelKeyString = section.getString("item-model");
        if (itemModelKeyString == null || itemModelKeyString.isBlank()) {
            return;
        }

        NamespacedKey itemModelKey = NamespacedKey.fromString(itemModelKeyString);
        if (itemModelKey == null) {
            getLogger().warning("Invalid item model key format '" + itemModelKeyString + "'.");
            return;
        }

        itemMeta.setItemModel(itemModelKey);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadCustomModelData(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        ConfigurationSection customModelDataSection = section.getConfigurationSection("custom-model-data");
        if (customModelDataSection == null) {
            return;
        }

        List<String> strings = customModelDataSection.getStringList("strings");
        List<Float> floats = customModelDataSection.getFloatList("floats");
        List<Boolean> flags = customModelDataSection.getBooleanList("flags");

        CustomModelDataComponent customModelDataComponent = itemMeta.getCustomModelDataComponent();
        customModelDataComponent.setStrings(strings);
        customModelDataComponent.setFloats(floats);
        customModelDataComponent.setFlags(flags);

        List<?> unknownColorList = customModelDataSection.getList("colors");
        if (unknownColorList != null) {
            List<Color> colors = parseColors(unknownColorList);
            customModelDataComponent.setColors(colors);
        }

        itemMeta.setCustomModelDataComponent(customModelDataComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private @NotNull List<Color> parseColors(@NotNull List<?> unknownList) {
        int unknownListSize = unknownList.size();
        List<Color> colors = new ArrayList<>(unknownListSize);

        for (Object unknownObject : unknownList) {
            if (unknownObject instanceof Color color) {
                colors.add(color);
            }

            if (unknownObject instanceof Integer integer) {
                Color color = Color.fromARGB(integer);
                colors.add(color);
            }

            if (unknownObject instanceof String string) {
                Color color = parseColor(string);
                if (color != null) {
                    colors.add(color);
                }
            }
        }

        return colors;
    }

    /**
     * Parse a color from a string.
     * @param string A string in the format of "alpha;red;green;blue" which are four integers between 0-255.
     * @return A {@link Color} value from the parsed ARGB values.
     */
    private @Nullable Color parseColor(@NotNull String string) {
        String[] split = string.split(Pattern.quote(";"), 3);
        if (split.length != 4) {
            return null;
        }

        String alphaString = split[0];
        String redString = split[1];
        String greenString = split[2];
        String blueString = split[3];

        try {
            int alpha = Integer.parseInt(alphaString);
            int red = Integer.parseInt(redString);
            int green = Integer.parseInt(greenString);
            int blue = Integer.parseInt(blueString);

            if (alpha < 0 || red < 0 || green < 0 || blue < 0) {
                throw new NumberFormatException("< 0");
            }

            if (alpha > 255 || red > 255 || green > 255 || blue > 255) {
                throw new NumberFormatException("> 255");
            }

            return Color.fromARGB(alpha, red, green, blue);
        } catch (NumberFormatException ex) {
            Logger logger = getLogger();
            logger.warning("Failed to parse color from string '" + string + "'.");
            logger.warning("Colors should be in format 'alpha;red;green;blue' with integers between 0 and 255");
            logger.log(Level.WARNING, "Full Exception:", ex);
            return null;
        }
    }

    private void loadGlowing(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("glowing")) {
            itemMeta.setEnchantmentGlintOverride(null);
        } else {
            boolean glowing = section.getBoolean("glowing", false);
            itemMeta.setEnchantmentGlintOverride(glowing);
        }

        itemStack.setItemMeta(itemMeta);
    }

    private void loadUnbreakable(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean unbreakable = section.getBoolean("unbreakable", false);
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadGlider(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        boolean glider = section.getBoolean("glider", false);
        itemMeta.setGlider(glider);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadDamageResistances(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        String damageResistanceKeyString = section.getString("damage-resistance");
        if (damageResistanceKeyString == null || damageResistanceKeyString.isBlank()) {
            return;
        }

        NamespacedKey damageResistanceKey = NamespacedKey.fromString(damageResistanceKeyString);
        if (damageResistanceKey == null) {
            getLogger().warning("Invalid damage resistance tag key format '" + damageResistanceKeyString + "'.");
            return;
        }

        Tag<DamageType> damageTypeTag = Bukkit.getTag(DamageTypeTags.REGISTRY_DAMAGE_TYPES, damageResistanceKey, DamageType.class);
        if (damageTypeTag == null) {
            getLogger().warning("Unknown or unregistered damage resistance tag key '" + damageResistanceKeyString + "'.");
            return;
        }

        itemMeta.setDamageResistant(damageTypeTag);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadMaxStackSize(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("max-stack-size")) {
            return;
        }

        int currentMax = itemStack.getMaxStackSize();
        int maxStackSize = section.getInt("max-stack-size", currentMax);
        itemMeta.setMaxStackSize(maxStackSize);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadRarity(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("rarity")) {
            return;
        }

        String rarityName = section.getString("rarity", "COMMON");
        ItemRarity rarity = ConfigurationHelper.parseEnum(ItemRarity.class, rarityName, ItemRarity.COMMON);
        itemMeta.setRarity(rarity);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadEnchantable(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("enchantable")) {
            return;
        }

        int defaultEnchantable = itemMeta.getEnchantable();
        int enchantable = section.getInt("enchantable", defaultEnchantable);
        itemMeta.setEnchantable(enchantable);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadUseCooldownComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.use-cooldown")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.use-cooldown");
        if (componentSection == null) {
            return;
        }

        UseCooldownComponent useCooldownComponent = itemMeta.getUseCooldown();

        if (componentSection.isSet("group")) {
            String groupKeyString = componentSection.getString("group");
            if (groupKeyString != null) {
                NamespacedKey groupKey = NamespacedKey.fromString(groupKeyString);
                if (groupKey != null) {
                    useCooldownComponent.setCooldownGroup(groupKey);
                }
            }
        }

        if (componentSection.isSet("seconds")) {
            float seconds = getFloat(componentSection, "seconds");
            useCooldownComponent.setCooldownSeconds(seconds);
        }

        itemMeta.setUseCooldown(useCooldownComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private float getFloat(@NotNull ConfigurationSection section, @NotNull String path) {
        Object object = section.get(path, 0.0F);
        return (object instanceof Number) ? NumberConversions.toFloat(object) : 0.0F;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadFoodComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.food")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.food");
        if (componentSection == null) {
            return;
        }

        boolean canAlwaysEat = componentSection.getBoolean("can-always-eat", false);
        int nutrition = componentSection.getInt("nutrition", 0);
        float saturation = getFloat(componentSection, "saturation");

        FoodComponent foodComponent = itemMeta.getFood();
        foodComponent.setCanAlwaysEat(canAlwaysEat);
        foodComponent.setNutrition(nutrition);
        foodComponent.setSaturation(saturation);

        itemMeta.setFood(foodComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadToolComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.tool")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.tool");
        if (componentSection == null) {
            return;
        }

        int damagePerBlock = componentSection.getInt("damage-per-block");
        float defaultMiningSpeed = getFloat(componentSection, "default-mining-speed");

        ToolComponent toolComponent = itemMeta.getTool();
        toolComponent.setDamagePerBlock(damagePerBlock);
        toolComponent.setDefaultMiningSpeed(defaultMiningSpeed);

        if (componentSection.isSet("rules")) {
            List<ToolComponent.ToolRule> toolRuleList = parseRulesList(componentSection.getList("rules"));
            toolComponent.setRules(toolRuleList);
        }

        itemMeta.setTool(toolComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private List<ToolComponent.ToolRule> parseRulesList(@Nullable List<?> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        int listSize = list.size();
        List<ToolComponent.ToolRule> toolRuleList = new ArrayList<>(listSize);

        for (Object object : list) {
            if (object instanceof ToolComponent.ToolRule toolRule) {
                toolRuleList.add(toolRule);
            }
        }

        return toolRuleList;
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadEquippableComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.equippable")) {
            return;
        }

        ConfigurationSection componentSection = section.getConfigurationSection("components.equippable");
        if (componentSection == null) {
            return;
        }

        EquippableComponent equippableComponent = itemMeta.getEquippable();
        if (componentSection.isSet("allowed-entities")) {
            List<String> allowNameList = componentSection.getStringList("allowed-entities");
            Set<EntityType> allowList = ConfigurationHelper.parseEnums(allowNameList, EntityType.class);
            equippableComponent.setAllowedEntities(allowList);
        }

        if (componentSection.isSet("dispensable")) {
            boolean dispensable = componentSection.getBoolean("dispensable", false);
            equippableComponent.setDispensable(dispensable);
        }

        if (componentSection.isSet("slot")) {
            String slotName = componentSection.getString("slot", "HAND");
            EquipmentSlot slot = ConfigurationHelper.parseEnum(EquipmentSlot.class, slotName, EquipmentSlot.HAND);
            equippableComponent.setSlot(slot);
        }

        if (componentSection.isSet("camera-overlay")) {
            String cameraOverlayKeyString = componentSection.getString("camera-overlay");
            if (cameraOverlayKeyString != null && !cameraOverlayKeyString.isBlank()) {
                NamespacedKey cameraOverlayKey = NamespacedKey.fromString(cameraOverlayKeyString);
                if (cameraOverlayKey != null) {
                    equippableComponent.setCameraOverlay(cameraOverlayKey);
                }
            }
        }

        if (componentSection.isSet("damage-on-hurt")) {
            boolean damageOnHurt = componentSection.getBoolean("damage-on-hurt", false);
            equippableComponent.setDamageOnHurt(damageOnHurt);
        }

        if (componentSection.isSet("equip-on-interact")) {
            boolean equipOnInteract = componentSection.getBoolean("equip-on-interact", false);
            equippableComponent.setEquipOnInteract(equipOnInteract);
        }

        if (componentSection.isSet("equip-sound")) {
            String soundKeyString = componentSection.getString("equip-sound");
            if (soundKeyString != null && !soundKeyString.isBlank()) {
                NamespacedKey soundKey = NamespacedKey.fromString(soundKeyString);
                if (soundKey != null) {
                    RegistryAccess registryAccess = RegistryAccess.registryAccess();
                    Registry<@NotNull Sound> registry = registryAccess.getRegistry(RegistryKey.SOUND_EVENT);
                    Sound sound = registry.get(soundKey);
                    if (sound != null) {
                        equippableComponent.setEquipSound(sound);
                    }
                }
            }
        }

        if (componentSection.isSet("model")) {
            String modelKeyString = componentSection.getString("model");
            if (modelKeyString != null && !modelKeyString.isBlank()) {
                NamespacedKey modelKey = NamespacedKey.fromString(modelKeyString);
                if (modelKey != null) {
                    equippableComponent.setModel(modelKey);
                }
            }
        }

        if (section.isSet("swappable")) {
            boolean swappable = section.getBoolean("swappable", false);
            equippableComponent.setSwappable(swappable);
        }

        itemMeta.setEquippable(equippableComponent);
        itemStack.setItemMeta(itemMeta);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadJukeboxPlayableComponent(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (!section.isSet("components.jukebox-playable")) {
            return;
        }

        String songKeyString = section.getString("components.jukebox-playable");
        if (songKeyString == null || songKeyString.isBlank()) {
            return;
        }

        NamespacedKey songKey = NamespacedKey.fromString(songKeyString);
        if (songKey == null) {
            return;
        }

        JukeboxPlayableComponent jukeboxPlayableComponent = itemMeta.getJukeboxPlayable();
        jukeboxPlayableComponent.setSongKey(songKey);

        itemMeta.setJukeboxPlayable(jukeboxPlayableComponent);
        itemStack.setItemMeta(itemMeta);
    }

    private void loadArmorMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof ArmorMeta armorMeta)) {
            return;
        }

        if (!section.isSet("armor-trim")) {
            return;
        }

        ConfigurationSection armorTrimSection = section.getConfigurationSection("armor-trim");
        if (armorTrimSection == null) {
            return;
        }

        String trimMaterialKeyString = armorTrimSection.getString("material", "minecraft:amethyst");
        String trimPatternKeyString = armorTrimSection.getString("pattern", "minecraft:eye");
        NamespacedKey trimMaterialKey = NamespacedKey.fromString(trimMaterialKeyString);
        NamespacedKey trimPatternKey = NamespacedKey.fromString(trimPatternKeyString);
        Logger logger = getLogger();

        if (trimMaterialKey == null) {
            logger.warning("Invalid trim material key '" + trimMaterialKeyString + "'.");
            return;
        }

        if (trimPatternKey == null) {
            logger.warning("Invalid trim pattern key '" + trimPatternKeyString + "'.");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull TrimMaterial> registryMaterial = registryAccess.getRegistry(RegistryKey.TRIM_MATERIAL);
        Registry<@NotNull TrimPattern> registryPattern = registryAccess.getRegistry(RegistryKey.TRIM_PATTERN);

        TrimMaterial trimMaterial = registryMaterial.get(trimMaterialKey);
        if (trimMaterial == null) {
            logger.warning("Unknown trim material with key '" + trimMaterialKeyString + "'.");
            return;
        }

        TrimPattern trimPattern = registryPattern.get(trimPatternKey);
        if (trimPattern == null) {
            logger.warning("Unknown trim pattern with key '" + trimPatternKeyString + "'.");
            return;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        armorMeta.setTrim(armorTrim);
        itemStack.setItemMeta(armorMeta);
    }

    private void loadAxolotlBucketMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            return;
        }

        if (!section.isSet("axolotl-variant")) {
            return;
        }

        String variantName = section.getString("axolotl-variant", "WILD");
        Axolotl.Variant variant = ConfigurationHelper.parseEnum(Axolotl.Variant.class, variantName, Axolotl.Variant.WILD);
        axolotlBucketMeta.setVariant(variant);

        itemStack.setItemMeta(axolotlBucketMeta);
    }

    private void loadBannerMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BannerMeta bannerMeta)) {
            return;
        }

        if (!section.isSet("banner-patterns")) {
            return;
        }

        ConfigurationSection bannerPatternsSection = section.getConfigurationSection("banner-patterns");
        if (bannerPatternsSection == null) {
            return;
        }

        Set<String> bannerPatternKeySet = bannerPatternsSection.getKeys(false);
        for (String bannerPatternKey : bannerPatternKeySet) {
            ConfigurationSection bannerPatternSection = bannerPatternsSection.getConfigurationSection(bannerPatternKey);
            if (bannerPatternSection == null) {
                continue;
            }

            org.bukkit.block.banner.Pattern pattern = parseBannerPattern(bannerPatternSection);
            if (pattern != null) {
                bannerMeta.addPattern(pattern);
            }
        }

        itemStack.setItemMeta(bannerMeta);
    }

    private @Nullable org.bukkit.block.banner.Pattern parseBannerPattern(@NotNull ConfigurationSection section) {
        String colorName = section.getString("color", "WHITE");
        DyeColor color = ConfigurationHelper.parseEnum(DyeColor.class, colorName, DyeColor.WHITE);

        String patternKeyString = section.getString("pattern", "minecraft:base");
        NamespacedKey patternKey = NamespacedKey.fromString(patternKeyString);
        Logger logger = getLogger();

        if (patternKey == null) {
            logger.warning("Invalid banner pattern key format '" + patternKeyString + "'.");
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull PatternType> registry = registryAccess.getRegistry(RegistryKey.BANNER_PATTERN);
        PatternType patternType = registry.get(patternKey);
        if (patternType == null) {
            logger.warning("Unknown banner pattern with key '" + patternKeyString + "'.");
            return null;
        }

        return new org.bukkit.block.banner.Pattern(color, patternType);
    }

    private void loadBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BookMeta bookMeta)) {
            return;
        }

        if (section.isSet("title")) {
            String titleText = section.getString("title");
            if (titleText != null && !titleText.isBlank()) {
                Component title = parseComponent(titleText);
                bookMeta.title(title);
            }
        }

        if (section.isSet("author")) {
            String authorText = section.getString("author");
            if (authorText != null && !authorText.isBlank()) {
                Component author = parseComponent(authorText);
                bookMeta.author(author);
            }
        }

        if (section.isSet("generation")) {
            String generationName = section.getString("generation", "ORIGINAL");
            BookMeta.Generation generation = ConfigurationHelper.parseEnum(BookMeta.Generation.class, generationName, BookMeta.Generation.ORIGINAL);
            bookMeta.setGeneration(generation);
        }

        if (section.isSet("pages")) {
            List<String> pageTextList = section.getStringList("pages");
            List<Component> pages = parseComponents(pageTextList);
            Component[] pageArray = pages.toArray(Component[]::new);
            bookMeta.addPages(pageArray);
        }

        itemStack.setItemMeta(bookMeta);
    }

    private void loadBundleMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BundleMeta bundleMeta)) {
            return;
        }

        if (section.isSet("bundle-items")) {
            List<ItemStack> itemList = parseSerializableList(ItemStack.class, section, "bundle-items");
            bundleMeta.setItems(itemList);
        }

        itemStack.setItemMeta(bundleMeta);
    }

    private void loadCompassMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof CompassMeta compassMeta)) {
            return;
        }

        if (!section.isSet("compass")) {
            return;
        }

        ConfigurationSection compassSection = section.getConfigurationSection("compass");
        if (compassSection == null) {
            return;
        }

        boolean lodestoneTracked = compassSection.getBoolean("lodestone");
        Location location = compassSection.getSerializable("lodestone-location", Location.class);
        if (location != null) {
            compassMeta.setLodestoneTracked(lodestoneTracked);
            compassMeta.setLodestone(location);
        }

        itemStack.setItemMeta(compassMeta);
    }

    private void loadDamageableMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof Damageable damageable)) {
            return;
        }

        if (section.isSet("max-damage")) {
            int defaultMaxDamage = damageable.getMaxDamage();
            int maxDamage = section.getInt("max-damage", defaultMaxDamage);
            damageable.setMaxDamage(maxDamage);
        }

        if (section.isSet("damage")) {
            int maxDamage = damageable.getMaxDamage();
            int damage = section.getInt("damage", 0);
            if (damage > maxDamage) {
                damage = maxDamage;
            }

            damageable.setDamage(damage);
        }

        itemStack.setItemMeta(damageable);
    }

    private void loadEnchantmentStorageMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof EnchantmentStorageMeta enchantmentStorageMeta)) {
            return;
        }

        ConfigurationSection enchantmentsSection = section.getConfigurationSection("stored-enchantments");
        if (enchantmentsSection == null) {
            return;
        }

        Set<String> enchantmentConfigurationKeySet = enchantmentsSection.getKeys(false);
        for (String configurationKey : enchantmentConfigurationKeySet) {
            ConfigurationSection enchantmentSection = enchantmentsSection.getConfigurationSection(configurationKey);
            if (enchantmentSection == null) {
                continue;
            }

            loadStoredEnchantment(enchantmentStorageMeta, section);
        }

        itemStack.setItemMeta(enchantmentStorageMeta);
    }

    private void loadStoredEnchantment(@NotNull EnchantmentStorageMeta itemMeta, @NotNull ConfigurationSection section) {
        Logger logger = getLogger();
        String enchantmentKeyString = section.getString("id", "minecraft:sharpness");
        NamespacedKey enchantmentKey = NamespacedKey.fromString(enchantmentKeyString);
        if (enchantmentKey == null) {
            logger.warning("Invalid enchantment id format '" + enchantmentKeyString + "'.");
            return;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull Enchantment> registry = registryAccess.getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment enchantment = registry.get(enchantmentKey);
        if (enchantment == null) {
            logger.warning("Unknown or unregistered enchantment id '" + enchantmentKeyString + "'.");
            return;
        }

        if (itemMeta.hasConflictingEnchant(enchantment)) {
            logger.warning("This item has an enchantment that conflicts with '" + enchantmentKeyString + "'.");
        }

        int level = section.getInt("level");
        boolean unsafe = section.getBoolean("unsafe", false);
        boolean enchant = itemMeta.addStoredEnchant(enchantment, level, unsafe);
        if (!enchant) {
            logger.info("Failed to enchant with enchantment '"
                    + enchantmentKeyString + "' and level '" + level + "'.");
        }
    }

    private void loadFireworkEffectMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof FireworkEffectMeta fireworkEffectMeta)) {
            return;
        }

        if (section.isSet("firework-effect")) {
            FireworkEffect effect = section.getSerializable("firework-effect", FireworkEffect.class);
            fireworkEffectMeta.setEffect(effect);
        }

        itemStack.setItemMeta(fireworkEffectMeta);
    }

    private void loadFireworkMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof FireworkMeta fireworkMeta)) {
            return;
        }

        if (section.isSet("firework-power")) {
            int power = section.getInt("firework-power");
            fireworkMeta.setPower(power);
        }

        if (section.isSet("firework-effects")) {
            List<FireworkEffect> effectList = parseSerializableList(FireworkEffect.class, section, "firework-effects");
            fireworkMeta.addEffects(effectList);
        }

        itemStack.setItemMeta(fireworkMeta);
    }

    private void loadKnowledgeBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof KnowledgeBookMeta knowledgeBookMeta)) {
            return;
        }

        if (section.isSet("knowledge-book-recipes")) {
            List<String> recipeKeyStringList = section.getStringList("knowledge-book-recipes");
            List<NamespacedKey> recipeKeyList = recipeKeyStringList.stream().map(NamespacedKey::fromString)
                    .filter(Objects::nonNull).toList();
            knowledgeBookMeta.setRecipes(recipeKeyList);
        }

        itemStack.setItemMeta(knowledgeBookMeta);
    }

    private void loadLeatherArmorMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof LeatherArmorMeta leatherArmorMeta)) {
            return;
        }

        if (section.isSet("color")) {
            String colorString = section.getString("color", "255;0;0;0");
            Color color = parseColor(colorString);
            leatherArmorMeta.setColor(color);
        }

        itemStack.setItemMeta(leatherArmorMeta);
    }

    private void loadMusicInstrumentMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof MusicInstrumentMeta musicInstrumentMeta)) {
            return;
        }

        if (section.isSet("music-instrument")) {
            String instrumentKeyString = section.getString("music-instrument", "minecraft:call_goat_horn");
            NamespacedKey instrumentKey = NamespacedKey.fromString(instrumentKeyString);
            Logger logger = getLogger();

            if (instrumentKey == null) {
                logger.warning("Invalid music instrument key '" + instrumentKeyString + "'.");
                return;
            }

            RegistryAccess registryAccess = RegistryAccess.registryAccess();
            Registry<@NotNull MusicInstrument> registry = registryAccess.getRegistry(RegistryKey.INSTRUMENT);
            MusicInstrument instrument = registry.get(instrumentKey);
            if (instrument == null) {
                logger.warning("Unknown music instrument with key '" + instrumentKeyString + "'.");
                return;
            }

            musicInstrumentMeta.setInstrument(instrument);
        }

        itemStack.setItemMeta(musicInstrumentMeta);
    }

    private void loadOminousBottleMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof OminousBottleMeta ominousBottleMeta)) {
            return;
        }

        if (section.isSet("ominous-amplifier")) {
            int amplifier = section.getInt("ominous-amplifier");
            ominousBottleMeta.setAmplifier(amplifier);
        }

        itemStack.setItemMeta(ominousBottleMeta);
    }

    private void loadPotionMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof PotionMeta potionMeta)) {
            return;
        }

        if (!section.isSet("potion")) {
            return;
        }

        Logger logger = getLogger();
        ConfigurationSection potionSection = section.getConfigurationSection("potion");
        if (potionSection == null) {
            return;
        }

        if (potionSection.isSet("base-type")) {
            String potionTypeKeyString = potionSection.getString("base-type", "minecraft:water");
            NamespacedKey potionTypeKey = NamespacedKey.fromString(potionTypeKeyString);
            if (potionTypeKey == null) {
                logger.warning("Invalid potion type key '" + potionTypeKeyString + "'.");
                return;
            }

            RegistryAccess registryAccess = RegistryAccess.registryAccess();
            Registry<@NotNull PotionType> registry = registryAccess.getRegistry(RegistryKey.POTION);
            PotionType potionType = registry.get(potionTypeKey);
            if (potionType == null) {
                logger.warning("Unknown potion type with key '" + potionTypeKeyString + "'.");
                return;
            }

            potionMeta.setBasePotionType(potionType);
        }

        if (potionSection.isSet("custom-effects")) {
            ConfigurationSection customEffectsSection = potionSection.getConfigurationSection("custom-effects");
            if (customEffectsSection != null) {
                Set<String> customEffectKeySet = customEffectsSection.getKeys(false);
                for (String customEffectKey : customEffectKeySet) {
                    ConfigurationSection customEffectSection = customEffectsSection.getConfigurationSection(customEffectKey);
                    if (customEffectSection == null) {
                        continue;
                    }

                    PotionEffect potionEffect = parsePotionEffect(customEffectSection);
                    if (potionEffect != null) {
                        potionMeta.addCustomEffect(potionEffect, true);
                    }
                }
            }
        }

        if (potionSection.isSet("color")) {
            String colorString = section.getString("color", "255;0;0;0");
            Color color = parseColor(colorString);
            potionMeta.setColor(color);
        }

        if (potionSection.isSet("translation-suffix")) {
            String customName = section.getString("translation-suffix");
            potionMeta.setCustomPotionName(customName);
        }

        itemStack.setItemMeta(potionMeta);
    }

    private @Nullable PotionEffect parsePotionEffect(@NotNull ConfigurationSection section) {
        String effectTypeKeyString = section.getString("effect-type", "minecraft:speed");
        NamespacedKey effectTypeKey = NamespacedKey.fromString(effectTypeKeyString);
        Logger logger = getLogger();

        if (effectTypeKey == null) {
            logger.warning("Invalid potion effect key '" + effectTypeKeyString + "'.");
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull PotionEffectType> registry = registryAccess.getRegistry(RegistryKey.MOB_EFFECT);
        PotionEffectType effectType = registry.get(effectTypeKey);
        if (effectType == null) {
            logger.warning("Unknown potion effect with key '" + effectTypeKeyString + "'.");
            return null;
        }

        int duration = section.getInt("duration", 0);
        int amplifier = section.getInt("amplifier", 0);
        boolean ambient = section.getBoolean("ambient", false);
        boolean particles = section.getBoolean("particles",true);
        boolean icon = section.getBoolean("icon", true);
        return new PotionEffect(effectType, duration, amplifier, ambient, particles, icon);
    }

    private void loadRepairableMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof Repairable repairable)) {
            return;
        }

        if (section.isSet("repair-cost")) {
            int repairCost = section.getInt("repair-cost", 0);
            repairable.setRepairCost(repairCost);
        }

        itemStack.setItemMeta(repairable);
    }

    private void loadShieldMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof ShieldMeta shieldMeta)) {
            return;
        }

        if (section.isSet("base-color")) {
            String baseColorName = section.getString("base-color", "WHITE");
            DyeColor baseColor = ConfigurationHelper.parseEnum(DyeColor.class, baseColorName, DyeColor.WHITE);
            shieldMeta.setBaseColor(baseColor);
        }

        itemStack.setItemMeta(shieldMeta);
    }

    private void loadSkullMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return;
        }

        if (section.isSet("skull-owner")) {
            String ownerName = section.getString("skull-owner");
            if (ownerName != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(ownerName);
                skullMeta.setOwningPlayer(player);
            }
        }

         if (section.isSet("skin")) {
             String skinBase64 = section.getString("skin");
             if (skinBase64 != null && !skinBase64.isBlank()) {
                 UUID uniqueId = loadProfileSkinUniqueId(section);
                 String skinName = section.getString("skin-name", "custom");

                 PlayerProfile profile = Bukkit.createProfileExact(uniqueId, skinName);
                 ProfileProperty textures = new ProfileProperty("textures", skinBase64);
                 profile.setProperty(textures);

                 skullMeta.setPlayerProfile(profile);
             }
         }

        itemStack.setItemMeta(skullMeta);
    }

    private @NotNull UUID loadProfileSkinUniqueId(@NotNull ConfigurationSection section) {
        if (!section.isSet("skin-uuid")) {
            return UUID.randomUUID();
        }

        String uuidString = section.getString("skin-uuid");
        if (uuidString == null || uuidString.isBlank()) {
            return UUID.randomUUID();
        }

        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException ex) {
            getLogger().warning("Invalid UUID format '" + uuidString + "'.");
            return UUID.randomUUID();
        }
    }

    private void loadSuspiciousStewMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SuspiciousStewMeta suspiciousStewMeta)) {
            return;
        }

        if (!section.isSet("custom-stew-effects")) {
            return;
        }

        ConfigurationSection customStewEffectsSection = section.getConfigurationSection("custom-stew-effects");
        if (customStewEffectsSection == null) {
            return;
        }

        Set<String> customStewEffectKeySet = customStewEffectsSection.getKeys(false);
        for (String key : customStewEffectKeySet) {
            ConfigurationSection customStewEffectSection = customStewEffectsSection.getConfigurationSection(key);
            if (customStewEffectSection == null) {
                continue;
            }

            SuspiciousEffectEntry entry = parseSuspiciousStewEffect(customStewEffectSection);
            if (entry != null) {
                suspiciousStewMeta.addCustomEffect(entry, true);
            }
        }

        itemStack.setItemMeta(suspiciousStewMeta);
    }

    private @Nullable SuspiciousEffectEntry parseSuspiciousStewEffect(@NotNull ConfigurationSection section) {
        String effectTypeKeyString = section.getString("effect-type", "minecraft:speed");
        NamespacedKey effectTypeKey = NamespacedKey.fromString(effectTypeKeyString);
        Logger logger = getLogger();

        if (effectTypeKey == null) {
            logger.warning("Invalid potion effect key '" + effectTypeKeyString + "'.");
            return null;
        }

        RegistryAccess registryAccess = RegistryAccess.registryAccess();
        Registry<@NotNull PotionEffectType> registry = registryAccess.getRegistry(RegistryKey.MOB_EFFECT);
        PotionEffectType effectType = registry.get(effectTypeKey);
        if (effectType == null) {
            logger.warning("Unknown potion effect with key '" + effectTypeKeyString + "'.");
            return null;
        }

        int duration = section.getInt("duration", 0);
        return SuspiciousEffectEntry.create(effectType, duration);
    }

    private void loadTropicalFishBucketMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof TropicalFishBucketMeta tropicalFishBucketMeta)) {
            return;
        }

        if (section.isSet("tropical-fish.pattern")) {
            String patternString = section.getString("tropical-fish-pattern", "KOB");
            TropicalFish.Pattern pattern = ConfigurationHelper.parseEnum(TropicalFish.Pattern.class, patternString, TropicalFish.Pattern.KOB);
            tropicalFishBucketMeta.setPattern(pattern);
        }

        if (section.isSet("tropical-fish.pattern-color")) {
            String colorString = section.getString("tropical-fish.pattern-color", "WHITE");
            DyeColor color = ConfigurationHelper.parseEnum(DyeColor.class, colorString, DyeColor.WHITE);
            tropicalFishBucketMeta.setPatternColor(color);
        }

        if (section.isSet("tropical-fish.body-color")) {
            String colorString = section.getString("tropical-fish.body-color", "WHITE");
            DyeColor color = ConfigurationHelper.parseEnum(DyeColor.class, colorString, DyeColor.WHITE);
            tropicalFishBucketMeta.setBodyColor(color);
        }

        itemStack.setItemMeta(tropicalFishBucketMeta);
    }

    private void loadWritableBookMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof WritableBookMeta writableBookMeta)) {
            return;
        }

        if (itemMeta instanceof BookMeta) {
            // Books that are already written are also somehow 'WritableBookMeta'.
            return;
        }

        if (section.isSet("pages")) {
            List<String> pages = section.getStringList("pages");
            writableBookMeta.setPages(pages);
        }

        itemStack.setItemMeta(writableBookMeta);
    }
}
