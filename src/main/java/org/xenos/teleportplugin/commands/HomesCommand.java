package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.gui.HomeGui;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class HomesCommand implements CommandExecutor {

    private final TeleportPlugin plugin;
    private final HomeManager homeManager;

    public HomesCommand(TeleportPlugin plugin, HomeManager homeManager) {
        this.plugin = plugin;
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        HomeGui gui = new HomeGui(plugin, homeManager, player);
        gui.open();

        return true;
    }
}
