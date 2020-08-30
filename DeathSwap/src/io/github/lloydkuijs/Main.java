package io.github.lloydkuijs;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        var deathSwap = new DeathSwapGameMode(this);

        this.getCommand("deathSwap").setExecutor(deathSwap);
        getServer().getPluginManager().registerEvents(deathSwap, this);
    }

    @Override
    public void onDisable() {

    }
}
