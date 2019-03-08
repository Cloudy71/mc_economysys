/*
  User: Cloudy
  Date: 13-Feb-19
  Time: 01:04
*/

package cz.cloudy.economysystem;

import cz.cloudy.economysystem.bank.Bank;
import cz.cloudy.economysystem.specialization.IAbility;
import cz.cloudy.economysystem.specialization.ISpec;
import cz.cloudy.economysystem.specialization.PlayerSpec;
import cz.cloudy.economysystem.specialization.PlayerSpecs;
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
public class AdminCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println(sender.isOp());
        if (!sender.isOp()) {
            return false;
        }

        if (label.equals("bankgive")) {
            if (args.length != 2) {
                return false;
            }
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                sender.sendMessage(ChatColor.AQUA + "Invalid player.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            Bank.giveMoney(player1, amount);
            player1.sendMessage(ChatColor.AQUA + "You have been given " + ChatColor.GREEN + amount + ChatColor.AQUA +
                                " coins from Bank fund.");
            return true;
        } else if (label.equals("banktake")) {
            if (args.length != 2) {
                return false;
            }
            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null) {
                sender.sendMessage(ChatColor.AQUA + "Invalid player.");
                return true;
            }
            int amount = Integer.parseInt(args[1]);
            Bank.takeMoney(player1, amount);
            player1.sendMessage(ChatColor.AQUA + "You have been taken " + ChatColor.RED + amount + ChatColor.AQUA +
                                " coins from your account.");
            return true;
        } else if (label.equals("banksellloc")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            Block target = player.getTargetBlock(null, 4);
            if (target.getType() != Material.CHEST) {
                sender.sendMessage(ChatColor.RED + "Bank Sell must be a chest!");
                return true;
            }
            Chest chest = (Chest) target.getState();
            Bank.bankSell = chest;
            player.sendMessage(ChatColor.GREEN + "Success.");
            Bank.save();
            return true;
        } else if (label.equals("setspec")) {
            if (args.length != 2) {
                return false;
            }

            Player player1 = Bukkit.getPlayer(args[0]);
            Class<?> clazz = null;
            try {
                clazz = Class.forName("cz.cloudy.economysystem.specialization.specs." + args[1]);
            } catch (ClassNotFoundException e) {
                sender.sendMessage(ChatColor.RED + "Invalid specs!");
                return true;
            }

            PlayerSpec playerSpec = PlayerSpecs.getPlayerSpec(player1);
            ISpec spec = null;
            try {
                spec = (ISpec) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return true;
            }
            playerSpec.setSpecs(spec);
            player1.sendMessage(ChatColor.AQUA + "You have been given " + ISpec.getSpecData(spec)
                                                                               .color() + ISpec.getSpecData(spec)
                                                                                               .name() +
                                ChatColor.AQUA + " specialization.");
            PlayerSpecs.save();
            return true;
        } else if (label.equals("addability")) {
            if (args.length != 2) {
                return false;
            }

            Player player1 = Bukkit.getPlayer(args[0]);
            Class<?> clazz = null;
            try {
                clazz = Class.forName("cz.cloudy.economysystem.specialization.abilities." + args[1]);
            } catch (ClassNotFoundException e) {
                sender.sendMessage(ChatColor.RED + "Invalid ability!");
                return true;
            }

            PlayerSpec playerSpec = PlayerSpecs.getPlayerSpec(player1);
            IAbility ability = null;
            try {
                ability = (IAbility) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return true;
            }
            playerSpec.getAbilities()
                      .add(ability);
            player1.sendMessage(ChatColor.AQUA + "You have been given " + IAbility.getAbilityData(ability)
                                                                                  .color() +
                                IAbility.getAbilityData(ability)
                                        .name() + ChatColor.AQUA + " ability.");
            PlayerSpecs.save();
            return true;
        }

        return false;
    }
}
