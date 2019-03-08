/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 19:56
*/

package cz.cloudy.economysystem.home;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommands
        implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equals("sethome")) {
            HomeLocation homeLocation = HomeLocations.getPlayerHomeLocation(player);
            Location location = new Location(player.getWorld(), player.getLocation()
                                                                      .getX(), player.getLocation()
                                                                                     .getY(), player.getLocation()
                                                                                                    .getZ(),
                                             player.getLocation()
                                                   .getYaw(), player.getLocation()
                                                                    .getPitch());
            if (homeLocation == null) {
                HomeLocations.getHomeLocations()
                             .add(new HomeLocation(player.getUniqueId().toString(), location));
            } else {
                homeLocation.setLocation(location);
            }
            player.sendMessage(ChatColor.AQUA + "New home location set!");
            HomeLocations.save();
            return true;
        } else if (label.equals("home")) {
            HomeLocation homeLocation = HomeLocations.getPlayerHomeLocation(player);
            if (homeLocation == null) {
                player.sendMessage(ChatColor.AQUA + "You have no home location set!");
                return true;
            }
            homeLocation.setBack(player.getLocation());
            player.teleport(homeLocation.getLocation());
            return true;
        } else if (label.equals("back")) {
            HomeLocation homeLocation = HomeLocations.getPlayerHomeLocation(player);
            if (homeLocation == null || homeLocation.getBack() == null) {
                player.sendMessage(ChatColor.AQUA + "You have no back location set!");
                return true;
            }
            player.teleport(homeLocation.getBack());
            HomeLocations.save();
            return true;
        }

        return false;
    }
}
