package me.none030.mortishoppers.managers;

import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.configs.ConfigManager;
import me.none030.mortishoppers.hoppers.HopperCommand;
import me.none030.mortishoppers.hoppers.HopperManager;
import me.none030.mortishoppers.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class MainManager {

    private final MortisHoppers plugin = MortisHoppers.getInstance();
    private ItemManager itemManager;
    private HopperManager hopperManager;
    private ConfigManager configManager;

    public MainManager() {
        this.itemManager = new ItemManager();
        this.configManager = new ConfigManager(this);
        plugin.getServer().getPluginCommand("hopper").setExecutor(new HopperCommand(this));
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
        setItemManager(new ItemManager());
        setConfigManager(new ConfigManager(this));
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public HopperManager getHopperManager() {
        return hopperManager;
    }

    public void setHopperManager(HopperManager hopperManager) {
        this.hopperManager = hopperManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
