package io.github.lloydkuijs;

import io.github.lloydkuijs.tasks.Countdown;
import io.github.lloydkuijs.tasks.CountdownEnd;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.jar.Attributes;

import io.github.lloydkuijs.Main;

import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class DeathSwapGameMode implements CommandExecutor, Runnable, Listener {
    private final Main _main;
    private List<Player> _players;
    private World _world;
    private boolean _gameInProgress;
    private int counter = 10;
    private Location _startLocation;
    private int _id;

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
                counter = 240;
                StartGame();
            }
        }
        else {
            if(counter <= 0) {

                TeleportPlayers();

                counter = 240;
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
            _players.remove(player);

            if(_players.size() == 1) {
                _main.getServer().broadcastMessage(String.format("%s has won the game!", _players.get(0).getDisplayName()));
                Bukkit.getScheduler().cancelTask(_id);
                _gameInProgress = false;
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if(_players.contains(player)) {
            _main.getServer().broadcastMessage(String.format("%s has disconnected", player.getDisplayName()));
            _players.remove(player);

            if(_players.size() == 1) {
                _main.getServer().broadcastMessage(String.format("%s has won the game!", _players.get(0).getDisplayName()));
                Bukkit.getScheduler().cancelTask(_id);
                _gameInProgress = false;
            }
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

        for (Player player : _players) {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setSaturation(20);
        }

        SpawnPlayers(6000);
    }

    private void SpawnPlayers(int blockDistance) {
        Random random = new Random();

        for (Player player : _players) {

            int randomX = (int) _startLocation.getX() - random.nextInt(blockDistance);
            int randomZ = (int) _startLocation.getZ() - random.nextInt(blockDistance);

            _main.getServer().broadcastMessage(String.format("X: %d, Y: %d", randomX, randomZ));
            Location playerLocation = new Location(_world, randomX, _world.getHighestBlockYAt(randomX, randomZ) + 4, randomZ);

            player.teleport(playerLocation);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player) || _gameInProgress) {
            return false;
        }

        Player player = (Player)sender;
        _world = player.getWorld();
        _startLocation = player.getLocation();
        _main.getServer().broadcastMessage( "Get ready! DeathSwap is about to begin!");

        _id = Bukkit.getScheduler().scheduleSyncRepeatingTask(_main, this, 0, 20L);

        return true;
    }

    public boolean InProgress() {
        return _gameInProgress;
    }
}