/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 21:47
*/

package cz.cloudy.economysystem.bank;

import cz.cloudy.economysystem.ActiveConst;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("Duplicates")
public class BankCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ActiveConst.BANK) return false;

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
        } else if (label.equals("banksend")) {
            if (args.length != 2) return false;
            String playerName = args[0];
            int amount = Integer.parseInt(args[1]);
            Player toPlayer = Bukkit.getPlayer(playerName);
            if (toPlayer == null) return false;
            BankAccount bankAccount = Bank.getBankAccount(player);
            BankAccount toBankAccount = Bank.getBankAccount(toPlayer);
            if (!bankAccount.hasMoney(amount)) return false;
            toBankAccount.addAmount(amount);
            bankAccount.addAmount(-amount);
            player.sendMessage(ChatColor.AQUA + "You have sent " + ChatColor.RED + amount + ChatColor.AQUA + " to " +
                               ChatColor.GOLD + toPlayer.getName() + ChatColor.AQUA + "'s account.");
            toPlayer.sendMessage(
                    ChatColor.AQUA + "You have received " + ChatColor.GREEN + amount + ChatColor.AQUA + " from " +
                    ChatColor.GOLD + toPlayer.getName() + ChatColor.AQUA + ".");
        }

        return false;
    }
}
