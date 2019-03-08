/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 21:30
*/

package cz.cloudy.economysystem.bank;

import cz.cloudy.economysystem.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class Bank {
    private static int               money;
    private static List<BankAccount> bankAccounts;
    public static  Chest             bankSell;

    static {
        bankAccounts = new LinkedList<>();
    }

    public static void initialize() {
        List<?> data = Main.instance.getConfig()
                                    .getList("bank-accounts");

        money = Main.instance.getConfig()
                             .getInt("bank-money");

        Location bankSellLocation = (Location) Main.instance.getConfig()
                                                            .get("bank-sell");
        if (bankSellLocation != null) {
            Block block = bankSellLocation.getBlock();
            if (block.getType() == Material.CHEST) {
                bankSell = (Chest) block.getState();
            }
        }

        if (data != null) {
            bankAccounts.addAll((List<BankAccount>) data);
        }
        save();
        System.out.println("Bank accounts initialized.");
    }

    public static void save() {
        while (bankAccounts.contains(null)) {
            bankAccounts.remove(null);
        }
        Main.instance.getConfig()
                     .set("bank-accounts", bankAccounts);
        Main.instance.getConfig()
                     .set("bank-money", money);
        Main.instance.getConfig()
                     .set("bank-sell", bankSell != null ? bankSell.getLocation() : null);

        Main.instance.saveConfig();
    }

    public static int getMoney() {
        return money;
    }

    private static boolean bankAccountExist(Player player) {
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getOwner()
                           .equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static BankAccount getBankAccount(Player player) {
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getOwner()
                           .equals(player.getUniqueId().toString())) {
                return bankAccount;
            }
        }
        return createBankAccount(player);
    }

    public static BankAccount createBankAccount(Player player) {
        if (bankAccountExist(player)) {
            return getBankAccount(player);
        }

        BankAccount bankAccount = new BankAccount(player.getUniqueId().toString(), 0);
        bankAccounts.add(bankAccount);
        save();
        return bankAccount;
    }

    public static void giveMoney(Player player, int amount) {
        getBankAccount(player).addAmount(amount);
        money -= amount;
        save();
    }

    public static void takeMoney(Player player, int amount) {
        getBankAccount(player).addAmount(-amount);
        money += amount;
        save();
    }

    public static boolean transaction(Player playerFrom, Player playerTo, int amount) {
        if (!getBankAccount(playerFrom).hasMoney(amount)) {
            return false;
        }
        getBankAccount(playerFrom).addAmount(-amount);
        getBankAccount(playerTo).addAmount(amount);
        save();
        return true;
    }

    public static List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
}
