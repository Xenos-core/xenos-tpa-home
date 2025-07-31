package org.xenos.teleportplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.HomeManager;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        HomeManager homeManager = teleportplugin.getInstance().getHomeManager();

        if (!homeManager.hasHome(player)) {
            player.sendMessage("§c❌ You haven't set a home yet! Use §f/sethome§c.");
            return true;
        }

        if (homeManager.isInCooldown(player)) {
            long left = homeManager.getCooldownLeft(player);
            player.sendMessage("§6⏳ You must wait §e" + left + " seconds §6before using §f/home§6 again.");
            return true;
        }

        if (homeManager.isInWarmup(player)) {
            player.sendMessage("§c⚠ You are already teleporting!");
            return true;
        }

        Location home = homeManager.getHome(player);
        if (home == null) {
            player.sendMessage("§c❌ Your home location is invalid.");
            return true;
        }

        homeManager.startWarmup(player, home);
        return true;
    }
}
