package me.none030.mortishoppers.hoppers;

import me.none030.mortishoppers.MortisHoppers;
import me.none030.mortishoppers.managers.Manager;
import me.none030.mortishoppers.menu.Menu;

public class HopperManager extends Manager {

    private final HopperSettings settings;
    private final Menu menu;

    public HopperManager(HopperSettings settings, Menu menu) {
        this.settings = settings;
        this.menu = menu;
        MortisHoppers plugin = MortisHoppers.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new HopperListener(this), plugin);
    }

    public HopperSettings getSettings() {
        return settings;
    }

    public Menu getMenu() {
        return menu;
    }
}
