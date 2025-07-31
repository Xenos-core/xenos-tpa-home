package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpDenyCommand implements CommandExecutor {

    private final TeleportManager teleportManager;

    public TpDenyCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        TeleportManager.TpaRequest request = teleportManager.getRequest(target);

        if (request == null) {
            MessageUtil.send(target, "<red>You have no pending teleport request to deny.");
            return true;
        }

        Player from = request.from;
        if (from != null && from.isOnline()) {
            MessageUtil.send(from, "<red>" + target.getName() + " denied your teleport request.");
        }

        MessageUtil.send(target, "<green>You denied the teleport request.");
        teleportManager.removeRequest(target);
        return true;
    }
}
