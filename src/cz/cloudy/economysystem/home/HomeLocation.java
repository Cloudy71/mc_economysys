/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 19:59
*/

package cz.cloudy.economysystem.home;

import cz.cloudy.economysystem.FillUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class HomeLocation
        implements ConfigurationSerializable {
    private String   owner;
    private Location location;
    private Location back;

    public HomeLocation(String owner, Location location) {
        this.owner = owner;
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getBack() {
        return back;
    }

    public void setBack(Location back) {
        this.back = back;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("owner", owner);
        map.put("location", location);
        map.put("back", back);
        return map;
    }

    public static HomeLocation deserialize(Map<String, Object> args) {
        HomeLocation homeLocation = new HomeLocation(FillUtil.fill(args.get("owner"), ""),
                                                     (Location) args.get("location"));
        homeLocation.back = (Location) args.get("back");
        return homeLocation;
    }
}
