/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 21:20
*/

package cz.cloudy.economysystem.bank;

import cz.cloudy.economysystem.FillUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class BankAccount
        implements ConfigurationSerializable {
    private String owner;
    private int    amount;

    public BankAccount(String owner, int amount) {
        this.owner = owner;
        this.amount = amount;
    }

    public String getOwner() {
        return owner;
    }

    public int getAmount() {
        return amount;
    }

    public boolean hasMoney(int amount) {
        return this.amount >= amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("owner", owner);
        data.put("amount", amount);
        return data;
    }

    public static BankAccount deserialize(Map<String, Object> args) {
        return new BankAccount((String) args.get("owner"), FillUtil.fill(args.get("amount"), 0));
    }
}
