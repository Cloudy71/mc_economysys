/*
  User: Cloudy
  Date: 11-Feb-19
  Time: 15:21
*/

package cz.cloudy.economysystem;

import cz.cloudy.economysystem.bank.Bank;
import cz.cloudy.economysystem.bank.BankAccount;
import cz.cloudy.economysystem.bank.BankCommands;
import cz.cloudy.economysystem.home.HomeCommands;
import cz.cloudy.economysystem.home.HomeLocation;
import cz.cloudy.economysystem.home.HomeLocations;
import cz.cloudy.economysystem.locker.*;
import cz.cloudy.economysystem.redstonecoding.CodingListener;
import cz.cloudy.economysystem.redstonecoding.CodingRegister;
import cz.cloudy.economysystem.specialization.PlayerSpec;
import cz.cloudy.economysystem.specialization.PlayerSpecCommands;
import cz.cloudy.economysystem.specialization.PlayerSpecListener;
import cz.cloudy.economysystem.specialization.PlayerSpecs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.util.Calendar;

public class Main
        extends JavaPlugin {
    public static Main instance;

    private long backupInterval = 20 * 60 * 15;

    @Override
    public void onEnable() {
        instance = this;
        boolean error = false;
        try {
            ConfigurationSerialization.registerClass(LockedChest.class);
            ConfigurationSerialization.registerClass(LockedDoor.class);
            ConfigurationSerialization.registerClass(BankAccount.class);
            ConfigurationSerialization.registerClass(HomeLocation.class);
            ConfigurationSerialization.registerClass(PlayerSpec.class);
            System.out.println("Economy System by Cloudy activated");

            Bukkit.getPluginManager()
                  .registerEvents(new LockerListener(), this);
            Bukkit.getPluginManager()
                  .registerEvents(new PlayerSpecListener(), this);
            Bukkit.getPluginManager()
                  .registerEvents(new CodingListener(), this);

            ServerCommands serverCommands = new ServerCommands();
            getCommand("ysay").setExecutor(serverCommands);
            AdminCommands adminCommands = new AdminCommands();
            getCommand("bankgive").setExecutor(adminCommands);
            getCommand("banktake").setExecutor(adminCommands);
            getCommand("banksellloc").setExecutor(adminCommands);
            getCommand("setspec").setExecutor(adminCommands);
            getCommand("addability").setExecutor(adminCommands);

            LockerCommands lockerCommands = new LockerCommands();
            getCommand("lock").setExecutor(lockerCommands);
            getCommand("addowner").setExecutor(lockerCommands);
            getCommand("protect").setExecutor(lockerCommands);
            HomeCommands homeCommands = new HomeCommands();
            getCommand("sethome").setExecutor(homeCommands);
            getCommand("home").setExecutor(homeCommands);
            getCommand("back").setExecutor(homeCommands);
            BankCommands bankCommands = new BankCommands();
            getCommand("myaccount").setExecutor(bankCommands);
            PlayerSpecCommands playerSpecCommands = new PlayerSpecCommands();
            getCommand("myspec").setExecutor(playerSpecCommands);

            Locker.initialize();
            Bank.initialize();
            HomeLocations.initialize();
            PlayerSpecs.initialize();
            CodingRegister.initialize();

            Bukkit.getServer()
                  .getScheduler()
                  .scheduleSyncRepeatingTask(this, () -> {
                      createBackup("");
                  }, backupInterval, backupInterval);
        } catch (Exception e) {
            error = true;
        }

        Bukkit.broadcastMessage((error ? (ChatColor.RED + "Deploy done with errors.")
                : (ChatColor.GREEN + "Deploy done without errors.")));
    }

    private void createBackup(String postfix) {
        Calendar calendar = Calendar.getInstance();
        File config = new File(getDataFolder(), "config.yml");
        File backup = new File(getDataFolder(),
                               "config_backup_" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH) +
                               "_" + calendar.get(Calendar.DAY_OF_MONTH) + postfix + ".yml");
        FileUtil.copy(config, backup);
        System.out.println("Creating backup");
    }

    @Override
    public void onDisable() {
        Locker.save();
        Bank.save();
        HomeLocations.save();
        PlayerSpecs.save();
        CodingRegister.save();

        createBackup("_deploy");
        Bukkit.broadcastMessage(ChatColor.GOLD + "Deploying...");
        System.out.println("Economy System by Cloudy deactivated");
    }

    /*TODO:
    <Name>
    <Item>
    B:<Price>
    <Amount>
     */
}
