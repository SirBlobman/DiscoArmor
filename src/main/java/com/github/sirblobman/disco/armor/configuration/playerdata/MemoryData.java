package com.github.sirblobman.disco.armor.configuration.playerdata;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.disco.armor.configuration.pattern.BuiltInType;
import com.github.sirblobman.disco.armor.configuration.pattern.PatternType;
import com.github.sirblobman.disco.armor.configuration.pattern.custom.ArmorSlot;

import static com.github.sirblobman.api.utility.ConfigurationHelper.parseEnum;

public final class MemoryData implements IConfigurable {
    private boolean armorStatus; // True: ON ; False: OFF
    private boolean glowingArmor;
    private PatternType selectedPatternType;
    private BuiltInType selectedBuiltInType;
    private String selectCustomPattern;

    private final Map<ArmorSlot, ItemStack> previousArmor;

    public MemoryData() {
        this.armorStatus = false;
        this.glowingArmor = false;
        this.selectedPatternType = PatternType.BUILT_IN;
        this.selectedBuiltInType = BuiltInType.RANDOM;
        this.selectCustomPattern = null;
        this.previousArmor = new EnumMap<>(ArmorSlot.class);
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        setArmorStatus(section.getBoolean("armor-status", false));
        setGlowingArmor(section.getBoolean("glowing", false));

        ConfigurationSection patternSection = getOrCreateSection(section, "pattern");
        String selectedPatternTypeName = patternSection.getString("type", "BUILT_IN");
        PatternType selectedPatternType = parseEnum(PatternType.class, selectedPatternTypeName, PatternType.BUILT_IN);
        setSelectedPatternType(selectedPatternType);

        if (selectedPatternType == PatternType.BUILT_IN) {
            String builtInTypeName = patternSection.getString("built-in-type", "RAINBOW");
            BuiltInType builtInType = parseEnum(BuiltInType.class, builtInTypeName, BuiltInType.RAINBOW);
            setSelectedBuiltInType(builtInType);
            setSelectCustomPattern(null);
        } else {
            setSelectCustomPattern(patternSection.getString("custom-id"));
            setSelectedBuiltInType(null);
        }

        this.previousArmor.clear();
        ConfigurationSection oldArmorSection = getOrCreateSection(section, "old-armor");
        loadPreviousArmor(oldArmorSection);
    }

    @Override
    public void save(@NotNull ConfigurationSection section) {
        section.set("armor-status", isArmorEnabled());
        section.set("glowing", isGlowingArmor());

        section.set("pattern", null);
        ConfigurationSection patternSection = getOrCreateSection(section, "pattern");

        PatternType selectedPatternType = getSelectedPatternType();
        patternSection.set("type", selectedPatternType.name());

        if (selectedPatternType == PatternType.BUILT_IN) {
            BuiltInType selectedBuiltInType = getSelectedBuiltInType();
            patternSection.set("built-in-type", selectedBuiltInType == null ? "RAINBOW" : selectedBuiltInType.name());
        } else {
            patternSection.set("custom-id", getSelectCustomPattern());
        }

        section.set("previous-armor", null);
        ConfigurationSection oldArmorSection = getOrCreateSection(section, "old-armor");
        ArmorSlot[] armorSlots = ArmorSlot.values();
        for (ArmorSlot armorSlot : armorSlots) {
            String key = armorSlot.name();
            ItemStack itemStack = this.previousArmor.get(armorSlot);
            oldArmorSection.set(key, itemStack);
        }
    }

    private void loadPreviousArmor(@NotNull ConfigurationSection section) {
        ArmorSlot[] armorSlots = ArmorSlot.values();
        for (ArmorSlot armorSlot : armorSlots) {
            String key = armorSlot.name();
            ItemStack itemStack = section.getItemStack(key);
            this.previousArmor.put(armorSlot, itemStack);
        }
    }

    public boolean isArmorEnabled() {
        return armorStatus;
    }

    public void setArmorStatus(boolean armorStatus) {
        this.armorStatus = armorStatus;
    }

    public boolean isGlowingArmor() {
        return glowingArmor;
    }

    public void setGlowingArmor(boolean glowingArmor) {
        this.glowingArmor = glowingArmor;
    }

    public PatternType getSelectedPatternType() {
        return selectedPatternType;
    }

    public void setSelectedPatternType(@NotNull PatternType selectedPatternType) {
        this.selectedPatternType = selectedPatternType;
    }

    public @Nullable BuiltInType getSelectedBuiltInType() {
        return selectedBuiltInType;
    }

    public void setSelectedBuiltInType(@Nullable BuiltInType selectedBuiltInType) {
        this.selectedBuiltInType = selectedBuiltInType;
    }

    public @Nullable String getSelectCustomPattern() {
        return selectCustomPattern;
    }

    public void setSelectCustomPattern(@Nullable String selectCustomPattern) {
        this.selectCustomPattern = selectCustomPattern;
    }

    public @NotNull Map<ArmorSlot, ItemStack> getPreviousArmor() {
        return Collections.unmodifiableMap(this.previousArmor);
    }

    public void setPreviousArmor(@NotNull PlayerInventory playerInventory) {
        this.previousArmor.clear();
        ArmorSlot[] armorSlots = ArmorSlot.values();
        for (ArmorSlot armorSlot : armorSlots) {
            EquipmentSlot slot = armorSlot.getEquipmentSlot();
            ItemStack itemStack = playerInventory.getItem(slot);
            this.previousArmor.put(armorSlot, itemStack);
        }
    }
}
