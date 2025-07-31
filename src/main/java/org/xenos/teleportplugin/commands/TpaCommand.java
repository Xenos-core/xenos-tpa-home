package org.xenos.teleportplugin.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Oɴʟу ᴘʟᴀуᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.send(player, "<red>Uꜱᴀɢᴇ:<gray>/tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtil.send(player, "<red> Nᴏ ᴘʟᴀуᴇʀ ғᴏᴜɴᴅ ᴡɪᴛʜ ᴛʜᴀᴛ ɴᴀᴍᴇ.");
            return true;
        }

        if (target.equals(player)) {
            MessageUtil.send(player, "<red>Yᴏᴜ ᴄᴀɴ'ᴛ ᴛᴇʟᴇᴘᴏʀᴛ ᴛᴏ уᴏᴜʀꜱᴇʟғ!");
            return true;
        }

        TeleportManager manager = teleportplugin.getInstance().getTeleportManager();

        if (!manager.isTeleportEnabled(target)) {
            MessageUtil.send(player, "<yellow>🚫 Tʜɪꜱ ᴘʟᴀуᴇʀ ʜᴀꜱ ᴅɪꜱᴀʙʟᴇᴅ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛꜱ.");
            return true;
        }

        if (manager.isIgnored(target, player)) {
            MessageUtil.send(player, "<yellow>🚫 Tʜɪꜱ ᴘʟᴀуᴇʀ ɪꜱ ɪɢɴᴏʀɪɴɢ уᴏᴜ.");
            return true;
        }

        manager.sendRequest(player, target);

        MessageUtil.send(player, "<green>✅ Rᴇǫᴜᴇꜱᴛ ꜱᴇɴᴛ ᴛᴏ <bold>" + target.getName() + "</bold>!");
        MessageUtil.send(target, "<gold><bold>" + player.getName() + "</bold> ᴡᴀɴᴛꜱ ᴛᴏ ᴛᴇʟᴇᴘᴏʀᴛ ᴛᴏ уᴏᴜ.</gold> Uꜱᴇ <green>/tpaccept</green> ᴛᴏ ᴀᴄᴄᴇᴘᴛ.");

        return true;
    }
}
