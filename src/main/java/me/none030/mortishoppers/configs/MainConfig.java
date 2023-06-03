package me.none030.mortishoppers.configs;

import me.none030.mortishoppers.hoppers.HopperManager;
import me.none030.mortishoppers.hoppers.HopperSettings;
import me.none030.mortishoppers.menu.Menu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainConfig extends Config {

    private final ConfigManager configManager;

    public MainConfig(ConfigManager configManager) {
        super("config.yml");
        this.configManager = configManager;
        loadConfig();
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        HopperSettings settings = loadSettings(config);
        if (settings == null) {
            return;
        }
        Menu menu = loadMenu(config.getConfigurationSection("menu"), configManager.getManager().getItemManager());
        if (menu == null) {
            return;
        }
        configManager.getManager().setHopperManager(new HopperManager(settings, menu));
        configManager.getManager().getHopperManager().addMessages(loadMessages(config.getConfigurationSection("messages")));
    }

    private HopperSettings loadSettings(FileConfiguration config) {
        if (config == null) {
            return null;
        }
        List<ItemStack> interactive = new ArrayList<>();
        for (String itemId : config.getStringList("interactive")) {
            ItemStack item = configManager.getManager().getItemManager().getItem(itemId);
            if (item == null) {
                continue;
            }
            interactive.add(item);
        }
        List<String> exactLore = config.getStringList("exact-lore");
        return new HopperSettings(interactive, exactLore);
    }
}
