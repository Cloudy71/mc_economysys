/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 17:48
*/

package cz.cloudy.economysystem.specialization;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSpecCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equals("myspec")) {
            PlayerSpec playerSpec = PlayerSpecs.getPlayerSpec(player);
            player.sendMessage(ChatColor.AQUA + "Your specialization is " + ISpec.getSpecData(playerSpec.getSpecs())
                                                                                 .color() +
                               ISpec.getSpecData(playerSpec.getSpecs())
                                    .name() + ChatColor.AQUA + ".");
            String abilities = "";
            for (IAbility ability : playerSpec.getAbilities()) {
                if (abilities.length() != 0) {
                    abilities += ChatColor.AQUA + ", ";
                }
                abilities += IAbility.getAbilityData(ability)
                                     .color() + IAbility.getAbilityData(ability)
                                                        .name();
            }
            if (abilities.length() == 0) {
                abilities = ChatColor.GRAY + "None";
            }
            player.sendMessage(ChatColor.AQUA + "Abilities: " + abilities + ChatColor.AQUA + ".");
            return true;
        }

        return false;
    }
}
