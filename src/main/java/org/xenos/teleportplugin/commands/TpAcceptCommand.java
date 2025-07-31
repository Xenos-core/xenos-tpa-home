package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpAcceptCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public TpAcceptCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        TeleportManager.TpaRequest request = teleportManager.getRequest(target);

        if (request == null || (System.currentTimeMillis() - request.timestamp > 60000)) { // 60 second timeout
            MessageUtil.send(target, "<red>You have no pending teleport requests.");
            if (request != null) {
                teleportManager.removeRequest(target);
            }
            return true;
        }

        Player from = request.from;
        if (!from.isOnline()) {
            MessageUtil.send(target, "<yellow>That player is no longer online.");
            teleportManager.removeRequest(target);
            return true;
        }

        if (teleportManager.isInCooldown(from)) {
            long left = teleportManager.getCooldownLeft(from);
            MessageUtil.send(from, "<yellow>You must wait " + left + " seconds before teleporting again.");
            MessageUtil.send(target, "<yellow>Player " + from.getName() + " is on cooldown.");
            return true;
        }

        if (teleportManager.isInWarmup(from)) {
            MessageUtil.send(from, "<red>You are already teleporting!");
            MessageUtil.send(target, "<red>That player is already teleporting.");
            return true;
        }

        // Start the warmup
        teleportManager.startTpaWarmup(from, target);
        MessageUtil.send(target, "<green>You accepted " + from.getName() + "'s teleport request.");

        teleportManager.removeRequest(target);
        return true;
    }
}
