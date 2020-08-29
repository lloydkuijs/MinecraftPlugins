package io.github.lloydkuijs.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class DeathSwapCommand implements CommandExecutor {
    private List<Player> _players;
    private World _world;
    private boolean _gameInProgress;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player) || _gameInProgress) {
            return false;
        }
        _world = ((Player)sender).getWorld();

        StartGame();

        GameEnd();

        return true;
    }

    public void StartGame() {
        _gameInProgress = true;
        _players = _world.getPlayers();

        SpawnPlayers(1000);

    }

    public void SpawnPlayers(int blockDistance) {
        Vector location = _world.getSpawnLocation().toVector(); // Top most position of spawn box
        location = new Vector(location.getX(), 0, location.getZ());
        int size = _players.size() * blockDistance;

        location.setX(location.getX() - (size / 2));
        location.setX(location.getY() - (size / 2));

        for (Player player: _players) {
            Vector offset = Vector.getRandom().multiply(size);
            offset.add(location);

            Location playerLocation = new Location(_world, 0,0 , 0);
            playerLocation.add(offset);


            playerLocation.setY(_world.getHighestBlockYAt(playerLocation));

            player.teleport(playerLocation);
        }

    }

    public void GameEnd() {
        _gameInProgress = false;
        _world = null;
        _players = null;
    }
}
