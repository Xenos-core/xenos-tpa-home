package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("Oɴʟу ᴘʟᴀуᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.");
            return true;
        }

        TeleportManager manager = teleportplugin.getInstance().getTeleportManager();
        TeleportManager.TpaRequest request = manager.getRequest(target);

        if (request == null) {
            MessageUtil.send(target, "<red>🚫 Yᴏᴜ ʜᴀᴠᴇ ɴᴏ ᴘᴇɴᴅɪɴɢ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ.");
            return true;
        }

        Player from = request.from;
        if (!from.isOnline()) {
            MessageUtil.send(target, "<yellow>⚠️ Tʜᴀᴛ ᴘʟᴀуᴇʀ ɪꜱ ɴᴏ ʟᴏɴɢᴇʀ ᴏɴʟɪɴᴇ.");
            manager.removeRequest(target);
            return true;
        }

        from.teleport(target.getLocation());
        MessageUtil.send(from, "<green>✅ Tᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ ᴀᴄᴄᴇᴘᴛᴇᴅ! Sᴇɴᴅɪɴɢ уᴏᴜ ᴛᴏ <bold>" + target.getName() + "</bold>.");
        MessageUtil.send(target, "<green>✅ Yᴏᴜ ᴀᴄᴄᴇᴘᴛᴇᴅ <bold>" + from.getName() + "</bold>'ꜱ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛ.");

        manager.removeRequest(target);
        return true;
    }
}
