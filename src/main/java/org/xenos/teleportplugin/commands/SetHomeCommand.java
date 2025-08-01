package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class SetHomeCommand implements CommandExecutor {

    private final HomeManager homeManager;

    public SetHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.send(player, "<red>Usage: /sethome <name>");
            return true;
        }

        String homeName = args[0];
        if (!homeName.matches("^[a-zA-Z0-9_-]{3,16}$")) {
            MessageUtil.send(player, "<red>Home name must be 3-16 characters long and can only contain letters, numbers, underscore, and dash.");
            return true;
        }

        homeManager.setHome(player, homeName);
        MessageUtil.send(player, "<green>Home '<white>" + homeName + "</white>' set successfully!");
        return true;
    }
}
