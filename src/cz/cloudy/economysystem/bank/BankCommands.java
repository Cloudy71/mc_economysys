/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 21:47
*/

package cz.cloudy.economysystem.bank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("Duplicates")
public class BankCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equals("myaccount")) {
            BankAccount bankAccount = Bank.getBankAccount(player);
            player.sendMessage(
                    ChatColor.AQUA + "You have " + (bankAccount.getAmount() > 0 ? ChatColor.GREEN : ChatColor.RED) +
                    bankAccount.getAmount() + ChatColor.AQUA + " coins on account.");

            return true;
        }

        return false;
    }
}
