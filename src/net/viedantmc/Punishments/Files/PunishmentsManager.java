package net.viedantmc.Punishments.Files;

import net.viedantmc.Punishments.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class PunishmentsManager {
    private Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;



    public PunishmentsManager(Main plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "Punishments.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("Punishments.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig != null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.configFile == null || this.dataConfig == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config" + this.configFile, e);
        }
    }
//Suu
    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "Punishments.yml");
        }
        if (!this.configFile.exists())
            this.plugin.saveResource("Punishments.yml", false);
    }



    }

	