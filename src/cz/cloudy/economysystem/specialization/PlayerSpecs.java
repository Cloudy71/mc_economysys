/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 17:29
*/

package cz.cloudy.economysystem.specialization;

import cz.cloudy.economysystem.Main;
import cz.cloudy.economysystem.specialization.specs.DefaultSpecs;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class PlayerSpecs {
    private static List<PlayerSpec> playerSpecs;

    static {
        playerSpecs = new LinkedList<>();
    }

    public static void initialize() {
        List<?> data = Main.instance.getConfig()
                                    .getList("player-specs");

        if (data != null) {
            playerSpecs.addAll((List<PlayerSpec>) data);
        }

        save();
        System.out.println("Player specs initialized");
    }

    public static void save() {
        while (playerSpecs.contains(null)) {
            playerSpecs.remove(null);
        }
        Main.instance.getConfig()
                     .set("player-specs", playerSpecs);

        Main.instance.saveConfig();
    }

    private static boolean playerSpecExist(Player player) {
        for (PlayerSpec playerSpec : playerSpecs) {
            if (playerSpec.getOwner()
                          .equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static PlayerSpec getPlayerSpec(Player player) {
        for (PlayerSpec playerSpec : playerSpecs) {
            if (playerSpec.getOwner()
                          .equals(player.getUniqueId().toString())) {
                return playerSpec;
            }
        }
        return createPlayerSpec(player);
    }

    public static PlayerSpec createPlayerSpec(Player player) {
        if (playerSpecExist(player)) {
            return getPlayerSpec(player);
        }

        PlayerSpec playerSpec = new PlayerSpec(player.getUniqueId().toString(), new DefaultSpecs());
        playerSpecs.add(playerSpec);
        save();

        return playerSpec;
    }
}
