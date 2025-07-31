package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpToggleCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public TpToggleCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        teleportManager.toggleTeleport(player);

        boolean enabled = teleportManager.isTeleportEnabled(player);

        if (enabled) {
            MessageUtil.send(player, "<green>You are now <white>accepting</white> teleport requests.");
        } else {
            MessageUtil.send(player, "<red>You are now <white>ignoring</white> all teleport requests.");
        }

        return true;
    }
}
