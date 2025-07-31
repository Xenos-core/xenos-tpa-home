package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpDenyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("Oɴʟу ᴘʟᴀуᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.");
            return true;
        }

        TeleportManager manager = teleportplugin.getInstance().getTeleportManager();
        TeleportManager.TpaRequest request = manager.getRequest(target);

        if (request == null) {
            MessageUtil.send(target, "<red>🚫 Yᴏᴜ ʜᴀᴠᴇ ɴᴏ ᴘᴇɴᴅɪɴɢ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ ᴛᴏ ᴅᴇɴу.");
            return true;
        }

        Player from = request.from;

        if (from != null && from.isOnline()) {
            MessageUtil.send(from, "<red><bold>" + target.getName() + "</bold> ᴅᴇɴɪᴇᴅ уᴏᴜʀ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ.");
        }

        MessageUtil.send(target, "<gray>Yᴏᴜ ᴅᴇɴɪᴇᴅ ᴛʜᴇ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ.");
        manager.removeRequest(target);
        return true;
    }
}
