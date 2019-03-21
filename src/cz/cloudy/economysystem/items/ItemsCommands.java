/*
  User: Cloudy
  Date: 20-Mar-19
  Time: 18:36
*/

package cz.cloudy.economysystem.items;

import cz.cloudy.economysystem.ActiveConst;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemsCommands
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ActiveConst.ITEMS) return false;

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equals("setcolor")) {
            if (args.length != 3) return false;
            int r = Integer.parseInt(args[0]);
            int g = Integer.parseInt(args[1]);
            int b = Integer.parseInt(args[2]);

            ItemsModifier.changeArmorColor(player, Color.fromRGB(r, g, b));

            return true;
        } else if (label.equals("blinkrandomly")) {
            if (args.length != 1) return false;
            int interval = Integer.parseInt(args[0]);
            ItemsModifier.setRandomBlinkingTask(player, interval);

            return true;
        }

        return false;
    }
}
