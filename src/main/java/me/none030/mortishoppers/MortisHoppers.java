package me.none030.mortishoppers;

import me.none030.mortishoppers.managers.MainManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MortisHoppers extends JavaPlugin {

    private static MortisHoppers Instance;
    private boolean towny;
    private MainManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        towny = getServer().getPluginManager().getPlugin("Towny") != null;
        manager = new MainManager();
    }

    public static MortisHoppers getInstance() {
        return Instance;
    }

    public boolean hasTowny() {
        return towny;
    }

    public MainManager getManager() {
        return manager;
    }
}
