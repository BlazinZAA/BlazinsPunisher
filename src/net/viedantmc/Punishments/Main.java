package net.viedantmc.Punishments;
import net.viedantmc.Punishments.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class Main extends JavaPlugin {
    public DataManager data;
    public static Map<UUID , Integer> warnings = new HashMap<>();

    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }

    public static void StringBuilder() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("warn")) {
            UUID PlayerUUID = Bukkit.getPlayer(args[0]).getUniqueId();
            List<String> Reasons = getConfig().getStringList(PlayerUUID + ".Reasons");
            Reasons.add(args[1]);
            this.getConfig().createSection(PlayerUUID + ".Reasons");
            String PlayerName = Bukkit.getPlayer(PlayerUUID).getName();
            if (this.getConfig().contains("Warnings")) {
                this.getConfig().getConfigurationSection(String.valueOf(PlayerUUID)).getKeys(false).forEach(key ->
                        warnings.put(UUID.fromString(key), this.getConfig().getInt(PlayerUUID + ".Warnings" + key))
                );
            }
            //
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
            return true;
        }
        return false;
    }
}





