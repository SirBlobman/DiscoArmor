package com.SirBlobman.disco.armor.manager;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.disco.armor.DiscoArmorPlugin;
import com.SirBlobman.disco.armor.object.ArmorType;

public class ArmorTypeManager {
    private final DiscoArmorPlugin plugin;
    private final Map<String, ArmorType> armorTypeMap;
    public ArmorTypeManager(DiscoArmorPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.armorTypeMap = new HashMap<>();
    }
    
    public DiscoArmorPlugin getPlugin() {
        return this.plugin;
    }
    
    public void registerArmorType(Class<? extends ArmorType> armorTypeClass) {
        try {
            Objects.requireNonNull(armorTypeClass, "A null ArmorType class cannot be registered!");
    
            Constructor<? extends ArmorType> constructor = armorTypeClass.getDeclaredConstructor();
            ArmorType type = constructor.newInstance();
            String id = type.getId();
    
            ArmorType currentType = armorTypeMap.getOrDefault(id, null);
            if(currentType != null) throw new IllegalArgumentException("An armor type with id '" + id + "' is already registered.");
    
            this.armorTypeMap.put(id, type);
        } catch(ReflectiveOperationException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering an ArmorType:", ex);
        }
    }
    
    public List<ArmorType> getArmorTypes() {
        return new ArrayList<>(this.armorTypeMap.values());
    }
    
    public ArmorType getArmorType(String id) {
        return this.armorTypeMap.getOrDefault(id, null);
    }
    
    public void removeArmorType(String id) {
        this.armorTypeMap.remove(id);
    }
}