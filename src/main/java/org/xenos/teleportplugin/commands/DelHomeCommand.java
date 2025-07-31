package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.utils.MessageUtil;
import org.xenos.teleportplugin.utils.SoundUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DelHomeCommand implements CommandExecutor, TabCompleter {

    private final TeleportPlugin plugin;
    private final HomeManager homeManager;

    public DelHomeCommand(TeleportPlugin plugin, HomeManager homeManager) {
        this.plugin = plugin;
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "<red>Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.send(player, "<red>Usage: /delhome <name>");
            return true;
        }

        String homeName = args[0];

        if (!homeManager.hasHome(player, homeName)) {
            MessageUtil.send(player, "<red>You don't have a home named '<white>" + homeName + "</white>'.");
            return true;
        }

        homeManager.deleteHome(player, homeName);
        MessageUtil.send(player, "<green>Successfully deleted home '<white>" + homeName + "</white>'.");
        SoundUtil.playSound(player, "del-home", plugin);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            Set<String> homeNames = homeManager.getHomeNames(player);
            return homeNames.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null; // Bukkit default
    }
}
