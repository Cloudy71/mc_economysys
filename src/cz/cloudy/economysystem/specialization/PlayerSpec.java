/*
  User: Cloudy
  Date: 12-Feb-19
  Time: 13:11
*/

package cz.cloudy.economysystem.specialization;

import cz.cloudy.economysystem.FillUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class PlayerSpec
        implements ConfigurationSerializable {
    private String         owner;
    private ISpec          specs;
    private List<IAbility> abilities;

    public PlayerSpec(String owner, ISpec specs) {
        this.owner = owner;
        this.specs = specs;
        this.abilities = new LinkedList<>();
    }

    public String getOwner() {
        return owner;
    }

    public ISpec getSpecs() {
        return specs;
    }

    public void setSpecs(ISpec specs) {
        this.specs = specs;
    }

    public List<IAbility> getAbilities() {
        return abilities;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("owner", owner);
        map.put("specs", specs.getClass()
                              .getSimpleName());
        String[] abils = new String[abilities.size()];
        for (int i = 0; i < abils.length; i++) {
            abils[i] = abilities.get(i)
                                .getClass()
                                .getSimpleName();
        }
        map.put("abilities", abils);
        return map;
    }

    public static PlayerSpec deserialize(Map<String, Object> args) {
        ISpec spec = null;
        try {
            spec = (ISpec) Class.forName(
                    "cz.cloudy.economysystem.specialization.specs." + FillUtil.fill(args.get("specs"), "DefaultSpecs"))
                                .newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        PlayerSpec playerSpec = new PlayerSpec((String) args.get("owner"), spec);
        List<String> abils = FillUtil.fill(args.get("abilities"), new ArrayList<>());
        for (String abil : abils) {
            IAbility obj = null;
            try {
                obj = (IAbility) Class.forName("cz.cloudy.economysystem.specialization.abilities." + abil)
                                      .newInstance();
                playerSpec.abilities.add(obj);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return playerSpec;
    }
}
