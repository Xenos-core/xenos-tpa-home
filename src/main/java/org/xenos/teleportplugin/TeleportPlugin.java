package org.xenos.teleportplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.xenos.teleportplugin.commands.*;
import org.xenos.teleportplugin.gui.HomeGuiListener;
import org.xenos.teleportplugin.listeners.WarmupListener;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TeleportPlugin extends JavaPlugin {

    private TeleportManager teleportManager;
    private HomeManager homeManager;

    @Override
    public void onEnable() {
        // Load config and initialize utilities
        saveDefaultConfig();
        MessageUtil.init(this);

        // Initialize managers
        this.teleportManager = new TeleportManager(this);
        this.homeManager = new HomeManager(this);

        // Register commands
        getCommand("tpa").setExecutor(new TpaCommand(this, teleportManager));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(this, teleportManager));
        getCommand("tpdeny").setExecutor(new TpDenyCommand(this, teleportManager));
        getCommand("tptoggle").setExecutor(new TpToggleCommand(this, teleportManager));
        getCommand("tpignore").setExecutor(new TpIgnoreCommand(this, teleportManager));
        getCommand("sethome").setExecutor(new SetHomeCommand(this, homeManager));
        HomeCommand homeCommand = new HomeCommand(this, homeManager);
        getCommand("home").setExecutor(homeCommand);
        getCommand("home").setTabCompleter(homeCommand);
        DelHomeCommand delHomeCommand = new DelHomeCommand(this, homeManager);
        getCommand("delhome").setExecutor(delHomeCommand);
        getCommand("delhome").setTabCompleter(delHomeCommand);
        getCommand("homes").setExecutor(new HomesCommand(this, homeManager));

        // Register listeners
        getServer().getPluginManager().registerEvents(new WarmupListener(homeManager, teleportManager), this);
        getServer().getPluginManager().registerEvents(new HomeGuiListener(this, homeManager), this);

        getLogger().info("Xenos-TPA-HOME enabled!");
    }

    @Override
    public void onDisable() {
        if (homeManager != null) {
            homeManager.cancelAllWarmups();
        }
        if (teleportManager != null) {
            teleportManager.cancelAllWarmups();
        }
        getLogger().info("Xenos-TPA-HOME disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("xenosreload")) {
            if (sender.hasPermission("xenos.reload")) {
                reloadConfig();
                MessageUtil.init(this);
                homeManager.loadConfig();
                teleportManager.loadConfig();
                MessageUtil.send(sender, "<green>Xenos-TPA-HOME configuration reloaded.");
            } else {
                MessageUtil.send(sender, "<red>You do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }
}
