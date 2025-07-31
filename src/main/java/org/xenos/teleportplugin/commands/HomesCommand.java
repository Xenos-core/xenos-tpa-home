package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.utils.MessageUtil;

import java.util.Set;

public class HomesCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public HomesCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        Set<String> homeNames = homeManager.getHomeNames(player);

        if (homeNames.isEmpty()) {
            MessageUtil.send(player, "<yellow>You have no homes set. Use /sethome <name> to set one.");
            return true;
        }

        String homesList = String.join("<gray>, <white>", homeNames);
        MessageUtil.send(player, "<green>Your homes: <white>" + homesList);

        return true;
    }
}
