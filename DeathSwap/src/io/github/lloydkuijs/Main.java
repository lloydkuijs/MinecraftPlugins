package io.github.lloydkuijs;

import io.github.lloydkuijs.commands.DeathSwapCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("deathSwap").setExecutor(new DeathSwapCommand(this));
    }

    @Override
    public void onDisable() {

    }
}
