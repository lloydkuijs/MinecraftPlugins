package io.github.lloydkuijs.tasks;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final int _countMs;
    private final JavaPlugin _plugin;
    private String _topic;
    private int _currentCountMs;

    public Countdown(JavaPlugin plugin, int countMs, CountdownEnd action) {
        _countMs = countMs;
        _plugin = plugin;
    }

    public Countdown(JavaPlugin plugin, int countMs, String topic, CountdownEnd action) {
        _countMs = countMs;
        _plugin = plugin;
        _topic = topic;
    }

    @Override
    public void run() {
        if(_currentCountMs == )
        if(_topic != null)
            _plugin.getServer().broadcastMessage(String.format("%s Starting the countdown", _topic));
        else
            _plugin.getServer().broadcastMessage("Starting the countdown");
    }
}
