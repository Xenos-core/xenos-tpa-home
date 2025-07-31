package org.xenos.teleportplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;
import org.xenos.teleportplugin.utils.SoundUtil;

public class TpaCommand implements CommandExecutor {

    private final TeleportPlugin plugin;
    private final TeleportManager teleportManager;

    public TpaCommand(TeleportPlugin plugin, TeleportManager teleportManager) {
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
            MessageUtil.send(player, "<red>Usage: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtil.send(player, "<red>No player found with that name.");
            return true;
        }

        if (target.equals(player)) {
            MessageUtil.send(player, "<red>You can't teleport to yourself!");
            return true;
        }

        if (!teleportManager.isTeleportEnabled(target)) {
            MessageUtil.send(player, "<yellow>This player has disabled teleport requests.");
            return true;
        }

        if (teleportManager.isIgnored(target, player)) {
            MessageUtil.send(player, "<yellow>This player is ignoring you.");
            return true;
        }

        teleportManager.sendRequest(player, target);

        MessageUtil.send(player, "<green>Request sent to <white>" + target.getName() + "</white>!");
        SoundUtil.playSound(player, "tpa-send", plugin);

        MessageUtil.send(target, "<gold><bold>" + player.getName() + "</bold> wants to teleport to you. Use <green>/tpaccept</green> to accept.");
        SoundUtil.playSound(target, "tpa-receive", plugin);

        return true;
    }
}
