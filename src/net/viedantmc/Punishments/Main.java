package net.viedantmc.Punishments;
import net.viedantmc.Punishments.Files.DataManager;
import net.viedantmc.Punishments.Files.PunishmentsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.print.DocFlavor;
import java.util.*;

public class Main extends JavaPlugin {
    public DataManager data;
    public PunishmentsManager Punish;
    public static Map<UUID , Integer> warnings = new HashMap<>(); //maps the PLayerUUID -> Integer , and the integer would be the amount of warnings

    @Override
    public void onEnable() {
        this.Punish = new PunishmentsManager(this);
        this.data = new DataManager(this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("warn")) {
            UUID PlayerUUID = Bukkit.getPlayer(args[0]).getUniqueId();//warn args0 args1
            List<String> Reasons = getConfig().getStringList(PlayerUUID + ".Reasons");
            Reasons.add(args[1]);
            String StringUUID = PlayerUUID.toString(); // I'm not going to use this yet but this turns the PlayerUUID to a string // for storing it in lists later on possibly
            this.getConfig().createSection(PlayerUUID + ".Reasons");
            String PlayerName = Bukkit.getPlayer(PlayerUUID).getName();
            if (this.getConfig().contains("Warnings")) {
                this.getConfig().getConfigurationSection(String.valueOf(PlayerUUID)).getKeys(false).forEach(key ->
                        warnings.put(UUID.fromString(key), this.getConfig().getInt(PlayerUUID + ".Warnings" + key))
                );
            }
            // When we map a UUID to an integer , the integer is the "key" , like a child of the UUID
            if (warnings.containsKey(PlayerUUID)) {
                this.getConfig().set(PlayerUUID + ".Reasons", Reasons);
                warnings.put(PlayerUUID, warnings.get(PlayerUUID) + 1);
            } else {
                this.getConfig().set(PlayerUUID + ".Reasons", Reasons);
                warnings.put(PlayerUUID, 1);
            }

            if (!this.getConfig().contains(PlayerUUID + ".PlayerName")) {
                this.getConfig().createSection(PlayerUUID + ".PlayerName");
                this.getConfig().set(PlayerUUID+ ".PlayerName", PlayerName);
            }
            if (!warnings.isEmpty()) {
                for (Map.Entry<UUID, Integer> entry : warnings.entrySet()) {
                    this.getConfig().set(entry.getKey().toString() + ".Warnings", entry.getValue());
                }
                this.saveConfig();
            }
            String ConfigTest = this.Punish.getConfig().getString("Punishments.Warnings.Command");
            sender.sendMessage(ConfigTest);
             //Threshold is the amount of warnings required before executing the command
            int WarningsAmount = warnings.get(PlayerUUID);
            int threshold =  this.Punish.getConfig().getInt("Punishments.Warnings");
            if(WarningsAmount == threshold) {
                String command = this.Punish.getConfig().getString("Punishments.Warnings.Command");
                command.replace("$Player", PlayerUUID.toString());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            return true;
        }
        return false;
    }


}





