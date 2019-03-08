/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 19:59
*/

package cz.cloudy.economysystem.home;

import cz.cloudy.economysystem.Main;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class HomeLocations {
    private static List<HomeLocation> homeLocations;

    static {
        homeLocations = new LinkedList<>();
    }

    public static void initialize() {
        List<?> data = Main.instance.getConfig()
                                    .getList("home-locations");

        if (data != null) {
            homeLocations.addAll((List<HomeLocation>) data);
        }
        System.out.println("Home locations initialized");
    }

    public static void save() {
        while (homeLocations.contains(null)) {
            homeLocations.remove(null);
        }
        Main.instance.getConfig()
                     .set("home-locations", homeLocations);
        Main.instance.saveConfig();
    }

    public static HomeLocation getPlayerHomeLocation(Player player) {
        for (HomeLocation homeLocation : homeLocations) {
            if (homeLocation.getOwner()
                            .equals(player.getUniqueId().toString())) {
                return homeLocation;
            }
        }
        return null;
    }

    public static List<HomeLocation> getHomeLocations() {
        return homeLocations;
    }
}
