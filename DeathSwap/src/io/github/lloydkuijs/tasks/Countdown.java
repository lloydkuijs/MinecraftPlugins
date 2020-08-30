package io.github.lloydkuijs.tasks;

import org.bukkit.plugin.java.JavaPlugin;

public class Countdown implements Runnable {

    private final int _stepSizeSeconds;
    private final CountdownEnd _countdownAction;
    private int _timeRemainingSeconds;
    private final JavaPlugin _plugin;
    private String _topic;
    private boolean _countdownStarted = false;

    public Countdown(JavaPlugin plugin, int timeSeconds, int stepSizeSeconds, CountdownEnd action) {
        _timeRemainingSeconds = timeSeconds;
        _plugin = plugin;
        _stepSizeSeconds = stepSizeSeconds;
        _countdownAction = action;
    }

    public Countdown(JavaPlugin plugin, int timeSeconds, int stepSizeSeconds, String topic, CountdownEnd action) {
        _timeRemainingSeconds = timeSeconds;
        _plugin = plugin;
        _topic = topic;
        _stepSizeSeconds = stepSizeSeconds;
        _countdownAction = action;
    }

    @Override
    public void run() {
        if (!_countdownStarted) {
            if (_topic != null)
                _plugin.getServer().broadcastMessage(String.format("%s Starting the countdown", _topic));
            else
                _plugin.getServer().broadcastMessage("Starting the countdown");

            _countdownStarted = true;
        }

        _timeRemainingSeconds -= _stepSizeSeconds;

        if (_timeRemainingSeconds <= 0) {
            _countdownAction.OnCountDownEnd();
        }
        else if(_timeRemainingSeconds < 10) {
            if (_topic != null)
                _plugin.getServer().broadcastMessage(String.format("Time remaining: %s", _topic, _timeRemainingSeconds));
            else
                _plugin.getServer().broadcastMessage(String.format("%s Time remaining: %s", _timeRemainingSeconds));
        }
    }
}
