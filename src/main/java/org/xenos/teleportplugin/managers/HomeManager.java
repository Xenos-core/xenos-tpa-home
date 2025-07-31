package org.xenos.teleportplugin.managers;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.xenos.teleportplugin.TeleportPlugin;
import org.xenos.teleportplugin.utils.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeManager {

    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Set<UUID> warmupQueue = new HashSet<>();
    private final Map<UUID, BukkitRunnable> warmupTasks = new HashMap<>();

    private final TeleportPlugin plugin;
    private long cooldown;
    private long warmupTicks;

    public HomeManager(TeleportPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
        file = new File(plugin.getDataFolder(), "homes.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void setHome(Player player, String name) {
        Location loc = player.getLocation();
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid + "." + name.toLowerCase();

        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());

        save();
    }

    public void deleteHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid + "." + name.toLowerCase();
        if (config.contains(path)) {
            config.set(path, null);
            save();
        }
    }

    public Location getHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid + "." + name.toLowerCase();
        if (!config.contains(path)) return null;

        World world = Bukkit.getWorld(config.getString(path + ".world"));
        if (world == null) return null;

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public boolean hasHome(Player player, String name) {
        return config.contains("homes." + player.getUniqueId() + "." + name.toLowerCase());
    }

    public Set<String> getHomeNames(Player player) {
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid;
        if (config.getConfigurationSection(path) == null) {
            return Collections.emptySet();
        }
        return config.getConfigurationSection(path).getKeys(false);
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        cooldown = config.getLong("teleport.home.cooldown", 220) * 1000;
        warmupTicks = config.getLong("teleport.home.warmup", 30) * 20;
    }

    public boolean isInCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        return cooldowns.containsKey(uuid) && (System.currentTimeMillis() - cooldowns.get(uuid)) < cooldown;
    }

    public long getCooldownLeft(Player player) {
        UUID uuid = player.getUniqueId();
        long end = cooldowns.getOrDefault(uuid, 0L) + cooldown;
        return Math.max(0, (end - System.currentTimeMillis()) / 1000);
    }

    public boolean isInWarmup(Player player) {
        return warmupQueue.contains(player.getUniqueId());
    }

    public void startWarmup(Player player, Location home) {
        UUID uuid = player.getUniqueId();

        if (isInWarmup(player)) {
            MessageUtil.send(player, "<red>You are already teleporting!");
            return;
        }

        warmupQueue.add(uuid);
        MessageUtil.send(player, plugin.getConfig().getString("messages.home-teleport-start", "<yellow>Teleporting in {time} seconds. Don't move or take damage!").replace("{time}", String.valueOf(warmupTicks / 20)));

        BukkitRunnable task = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancelWarmup(player);
                    cancel();
                    return;
                }

                // افکت چرخشی اطراف پلیر
                Location loc = player.getLocation().add(0, 1, 0);
                for (int i = 0; i < 360; i += 60) {
                    double radians = Math.toRadians(i + ticks * 10);
                    double x = Math.cos(radians);
                    double z = Math.sin(radians);
                    loc.getWorld().spawnParticle(Particle.PORTAL, loc.clone().add(x, 0, z), 0);
                }

                ticks++;
                if (ticks >= warmupTicks) {
                    teleportPlayer(player, home);
                    cancelWarmup(player);
                    cancel();
                }
            }
        };

        warmupTasks.put(uuid, task);
        task.runTaskTimer(plugin, 0L, 1L);
    }

    private void teleportPlayer(Player player, Location home) {
        player.teleport(home);
        MessageUtil.send(player, "<green>You have been teleported to your home!");
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        // افکت هنگام رسیدن
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 60, 0.5, 1, 0.5, 0.01);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    public void cancelWarmup(Player player) {
        UUID uuid = player.getUniqueId();
        warmupQueue.remove(uuid);

        BukkitRunnable task = warmupTasks.remove(uuid);
        if (task != null) {
            if (!task.isCancelled()) {
                task.cancel();
            }
            MessageUtil.send(player, "<red>Teleport canceled.");
        }
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelAllWarmups() {
        for (BukkitRunnable task : warmupTasks.values()) {
            task.cancel();
        }
        warmupTasks.clear();
        warmupQueue.clear();
    }
}
