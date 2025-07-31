package org.xenos.teleportplugin.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;

public class DelHomeCommand implements CommandExecutor {

    private final String prefix = "<i><b><gradient:#34213C:#C32ABF:#545eb6>🇽‌🇪‌🇳‌🇴‌🇸‌-🇨‌🇴‌🇷‌🇪‌ >> </gradient></b></i>";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!teleportplugin.getInstance().getHomeManager().hasHome(player)) {
            player.sendMessage(Component.text("You don’t have a home set.", NamedTextColor.RED));
            return true;
        }

        teleportplugin.getInstance().getHomeManager().deleteHome(player);
        player.sendMessage(Component.text("Your home has been deleted.", NamedTextColor.GRAY));
        return true;
    }
}
