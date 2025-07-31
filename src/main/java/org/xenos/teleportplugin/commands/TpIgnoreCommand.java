package org.xenos.teleportplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpIgnoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("Oɴʟу ᴘʟᴀуᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.send(target, "<yellow>Uꜱᴀɢᴇ: <gray>/tpignore <player>");
            return true;
        }

        Player ignored = Bukkit.getPlayerExact(args[0]);
        if (ignored == null || !ignored.isOnline()) {
            MessageUtil.send(target, "<red>🚫 Pʟᴀуᴇʀ ɴᴏᴛ ғᴏᴜɴᴅ ᴏʀ ᴏғғʟɪɴᴇ.");
            return true;
        }

        if (ignored.equals(target)) {
            MessageUtil.send(target, "<red>❌ Yᴏᴜ ᴄᴀɴ'ᴛ ɪɢɴᴏʀᴇ уᴏᴜʀꜱᴇʟғ.");
            return true;
        }

        TeleportManager manager = teleportplugin.getInstance().getTeleportManager();
        manager.ignorePlayer(target, ignored);

        MessageUtil.send(target, "<gray>Yᴏᴜ ᴀʀᴇ ɴᴏᴡ ɪɢɴᴏʀɪɴɢ <red><bold>" + ignored.getName() + "</bold></red>. Tʜᴇу ᴄᴀɴ'ᴛ ꜱᴇɴᴅ уᴏᴜ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛꜱ ᴀɴуᴍᴏʀᴇ.");
        return true;
    }
}
