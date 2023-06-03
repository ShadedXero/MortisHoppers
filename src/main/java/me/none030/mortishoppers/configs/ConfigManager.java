package me.none030.mortishoppers.configs;

import me.none030.mortishoppers.managers.MainManager;

public class ConfigManager {

    private final MainManager manager;
    private final ItemConfig itemConfig;
    private final MainConfig mainConfig;

    public ConfigManager(MainManager manager) {
        this.manager = manager;
        this.itemConfig = new ItemConfig("items.yml", manager.getItemManager());
        this.mainConfig = new MainConfig(this);
    }

    public MainManager getManager() {
        return manager;
    }

    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }
}
