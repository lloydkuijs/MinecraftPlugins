package io.github.lloydkuijs;

import io.github.lloydkuijs.commands.DeathSwapCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("deathSwap").setExecutor(new DeathSwapCommand());
    }

    @Override
    public void onDisable() {

    }
}
