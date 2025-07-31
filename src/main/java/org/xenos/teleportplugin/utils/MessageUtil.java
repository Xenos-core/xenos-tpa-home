package org.xenos.teleportplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.xenos.teleportplugin.TeleportPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static Component prefixComponent;
    private static final Map<Character, Character> smallCapsMap = new HashMap<>();

    static {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String smalls = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡxʏᴢ";
        for (int i = 0; i < upper.length(); i++) {
            smallCapsMap.put(upper.charAt(i), smalls.charAt(i));
            smallCapsMap.put(lower.charAt(i), smalls.charAt(i));
        }
    }

    public static void init(TeleportPlugin plugin) {
        String prefixString = plugin.getConfig().getString("messages.prefix", "<i><b><gradient:#34213C:#C32ABF:#545eb6>🇽‌🇪‌🇳‌🇴‌🇸‌-🇨‌🇴‌🇷‌🇪‌ >></gradient></b></i> ");
        prefixComponent = mm.deserialize(prefixString);
    }

    private static String toSmallCaps(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(smallCapsMap.getOrDefault(c, c));
        }
        return sb.toString();
    }

    private static Component transformComponentText(Component component) {
        // 1. Create a new component with the transformed text content
        Component newComponent = component;
        if (component instanceof TextComponent) {
            TextComponent textComponent = (TextComponent) component;
            String originalContent = textComponent.content();
            if (!originalContent.isEmpty()) {
                String newContent = toSmallCaps(originalContent);
                newComponent = Component.text(newContent, component.style());
            }
        }

        // 2. Recursively transform the children and append them
        List<Component> children = component.children();
        if (!children.isEmpty()) {
            List<Component> newChildren = new ArrayList<>();
            for (Component child : children) {
                newChildren.add(transformComponentText(child));
            }
            newComponent = newComponent.children(newChildren);
        }

        return newComponent;
    }

    public static void send(CommandSender sender, String message) {
        Component parsedMessage = mm.deserialize(message);
        Component transformedMessage = transformComponentText(parsedMessage);
        sender.sendMessage(prefixComponent.append(transformedMessage));
    }

    public static void sendRaw(CommandSender sender, String message) {
        sender.sendMessage(mm.deserialize(message));
    }
}
