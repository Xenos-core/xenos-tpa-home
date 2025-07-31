package org.xenos.teleportplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpIgnoreCommand implements CommandExecutor {

    private final TeleportPlugin plugin;
    private final TeleportManager teleportManager;

    public TpIgnoreCommand(TeleportPlugin plugin, TeleportManager teleportManager) {
        this.plugin = plugin;
        this.teleportManager = teleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.send(player, "<red>Usage: /tpignore <player>");
            return true;
        }

        Player ignored = Bukkit.getPlayerExact(args[0]);
        if (ignored == null || !ignored.isOnline()) {
            MessageUtil.send(player, "<red>Player not found or is offline.");
            return true;
        }

        if (ignored.equals(player)) {
            MessageUtil.send(player, "<red>You can't ignore yourself.");
            return true;
        }

        teleportManager.ignorePlayer(player, ignored);

        MessageUtil.send(player, "<green>You are now ignoring <white>" + ignored.getName() + "</white>.");
        return true;
    }
}
