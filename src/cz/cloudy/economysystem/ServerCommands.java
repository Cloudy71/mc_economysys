/*
  User: Cloudy
  Date: 13-Feb-19
  Time: 00:33
*/

package cz.cloudy.economysystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ServerCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return false;
        }

        if (label.equals("ysay")) {
            String msg = String.join(" ", args);
            Bukkit.broadcastMessage(ChatColor.YELLOW + msg);
            return true;
        }

        return false;
    }
}
