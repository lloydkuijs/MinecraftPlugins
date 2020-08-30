package io.github.lloydkuijs;

import io.github.lloydkuijs.tasks.Countdown;
import io.github.lloydkuijs.tasks.CountdownEnd;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

import io.github.lloydkuijs.Main;

public class DeathSwapGameMode implements CommandExecutor, Runnable, Listener {
    private final Main _main;
    private List<Player> _players;
    private World _world;
    private boolean _gameInProgress;
    private int counter = 10;

    public DeathSwapGameMode(Main main) {
        _main = main;
    }

    @Override
    public void run() {
        if(!_gameInProgress) {
            _main.getServer().broadcastMessage( String.format("Time left: %d", counter));
            counter--;

            if(counter <= 0) {
                _gameInProgress = true;
                counter = 60;
                StartGame();
            }
        }
        else {
            if(counter <= 0) {
                TeleportPlayers();

                counter = 60;
            }

            if(counter <= 10) {
                _main.getServer().broadcastMessage( String.format("Switching places in: %d", counter));
            }
            counter--;
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if(_players.contains(player)) {
            _main.getServer().broadcastMessage(String.format("%s has died", player.getDisplayName()));
        }
    }

    // Possible optimizations with exchanging List and using less loops, didn't bother.
    private void TeleportPlayers() {
        _main.getServer().broadcastMessage("Teleporting players");

        List<Location> locations = new ArrayList<>();

        for (Player player : _players) {
            locations.add(player.getLocation());
        }

        Collections.reverse(locations);

        for (int i = 0; i < _players.size(); i++) {
            _players.get(i).teleport(locations.get(i));
        }
    }

    private void StartGame() {
        _gameInProgress = true;
        _players = _world.getPlayers();

        SpawnPlayers(1000);
    }

    private void SpawnPlayers(int blockDistance) {
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player) || _gameInProgress) {
            return false;
        }

        _world = ((Player)sender).getWorld();
        _main.getServer().broadcastMessage( "Get ready! DeathSwap is about to begin!");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(_main, this, 0, 20L);

        return true;
    }

    public boolean InProgress() {
        return _gameInProgress;
    }
}