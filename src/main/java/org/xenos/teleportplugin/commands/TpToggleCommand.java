package org.xenos.teleportplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.xenos.teleportplugin.teleportplugin;
import org.xenos.teleportplugin.managers.TeleportManager;
import org.xenos.teleportplugin.utils.MessageUtil;

public class TpToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Oɴʟу ᴘʟᴀуᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ ᴄᴏᴍᴍᴀɴᴅ.");
            return true;
        }

        TeleportManager manager = teleportplugin.getInstance().getTeleportManager();
        manager.toggleTeleport(player);

        boolean enabled = manager.isTeleportEnabled(player);

        if (enabled) {
            MessageUtil.send(player, "<green>✅ Yᴏᴜ ᴀʀᴇ ɴᴏᴡ <bold>ᴀᴄᴄᴇᴘᴛɪɴɢ</bold> ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛꜱ.");
        } else {
            MessageUtil.send(player, "<red>❌ Yᴏᴜ ᴀʀᴇ ɴᴏᴡ <bold>ɪɢɴᴏʀɪɴɢ</bold> ᴀʟʟ ᴛᴇʟᴇᴘᴏʀᴛ ʀᴇǫᴜᴇꜱᴛꜱ.");
        }

        return true;
    }
}
