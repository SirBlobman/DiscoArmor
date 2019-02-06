package com.SirBlobman.discoarmor.configuration;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class ConfigSettings extends Config {
	private static YamlConfiguration config = new YamlConfiguration();
	public static void save() {saveConfig(config, "config.yml");}
	public static YamlConfiguration load() {
		copyFromJar("config.yml");
		config = loadConfig("config.yml");
		return config;
	}
	
	@SuppressWarnings("unchecked")
    public static <O> O getOption(String path, O defaultValue) {
	    if(config == null) load();
	    if(config.isSet(path)) {
	        Object obj = config.get(path);
	        Class<?> valClass = defaultValue.getClass();
	        Class<?> objClass = obj.getClass();
	        if(obj != null && (valClass.isInstance(obj) || objClass.isAssignableFrom(valClass))) {
	            O o = (O) obj;
	            return o;
	        } else return defaultValue;
	    } else return defaultValue;
	}
	
	public static String getMessage(String path) {
	    String actualPath = "messages." + path;
	    String message = getOption(actualPath, "");
	    String color = message.isEmpty() ? "" : ChatColor.translateAlternateColorCodes('&', message);
	    return color;
	}
	
	public static void sendMessage(CommandSender cs, String messagePath) {
	    String color = getMessage(messagePath);
	    if(color != null && !color.isEmpty()) {
	        cs.sendMessage(color);
	    }
	}
}