package me.none030.mortishoppers.configs;

import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.items.ItemManager;
import me.none030.mortishoppers.menu.Menu;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

import static me.none030.mortishoppers.utils.MessageUtils.color;

public abstract class Config {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private final String fileName;

    public Config(@NotNull String fileName) {
        this.fileName = fileName;
    }

    public abstract void loadConfig();

    public Menu loadMenu(ConfigurationSection section, @NotNull ItemManager itemManager) {
        if (section == null) {
            return null;
        }
        Menu menu = new Menu(color(section.getString("title")));
        for (String key : section.getKeys(false)) {
            if (key.equalsIgnoreCase("title")) {
                continue;
            }
            String itemId = section.getString(key);
            if (itemId == null) {
                continue;
            }
            ItemStack item = itemManager.getItem(itemId);
            menu.addItem(key.replace("-", "_").toUpperCase(), item);
        }
        return menu;
    }

    public HashMap<String, Component> loadMessages(ConfigurationSection section) {
        HashMap<String, Component> messageById = new HashMap<>();
        if (section == null) {
            return messageById;
        }
        for (String key : section.getKeys(false)) {
            String id = key.replace("-", "_").toUpperCase();
            String message = section.getString(key);
            messageById.put(id, color(message));
        }
        return messageById;
    }

    public File saveConfig() {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, true);
        }
        return file;
    }

    public MortisHoppers getPlugin() {
        return plugin;
    }

    public String getFileName() {
        return fileName;
    }
}
