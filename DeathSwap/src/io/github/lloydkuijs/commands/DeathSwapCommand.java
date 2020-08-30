package io.github.lloydkuijs.commands;

import io.github.lloydkuijs.tasks.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import io.github.lloydkuijs.Main;

public class DeathSwapCommand implements CommandExecutor {
    private final Main _main;
    private List<Player> _players;
    private World _world;
    private boolean _gameInProgress;

    public DeathSwapCommand(Main main) {
        _main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player) || _gameInProgress) {
            return false;
        }
        _world = ((Player) sender).getWorld();

        StartGame();


        _main.getServer().getScheduler().scheduleSyncRepeatingTask(_main,
                new Countdown(_main, 100, () -> TeleportPlayers()), 0L, 20L
        );

        GameEnd();

        return true;
    }

    public void TeleportPlayers() {

    }

    public void StartGame() {
        _gameInProgress = true;
        _players = _world.getPlayers();

        SpawnPlayers(1000);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.g, new Runnable() {
            @Override
            public void run() {

            }
        }, 0, 20);
    }

    public void SpawnPlayers(int blockDistance) {
        Vector location = new Vector(0, 0, 0);
        int size = _players.size() * blockDistance;

        // Top position of play area
        location.setX(location.getX() - (size / 2));
        location.setX(location.getY() - (size / 2));

        Random random = new Random();

        for (Player player : _players) {

            int randomX = (int) location.getX() + random.nextInt(size);
            int randomZ = (int) location.getZ() + random.nextInt(size);

            Location playerLocation = new Location(_world, randomX, _world.getHighestBlockYAt(randomX, randomZ) + 1, randomZ);

            player.teleport(playerLocation);
        }

    }

    public void GameEnd() {
        _gameInProgress = false;
        _world = null;
        _players = null;
    }
}