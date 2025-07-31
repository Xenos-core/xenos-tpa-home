package org.xenos.teleportplugin.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    private static final String PREFIX = "<b><gradient:#9863E7:#E43A96>хᴇɴᴏꜱ-ᴄᴏʀᴇ >></gradient></b>";

    public static void send(CommandSender sender, String message) {
        Component component = mm.deserialize(PREFIX + message);
        sender.sendMessage(component);
    }
}
