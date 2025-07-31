package org.xenos.teleportplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.xenos.teleportplugin.commands.*;
import org.xenos.teleportplugin.listeners.WarmupListener;
import org.xenos.teleportplugin.managers.HomeManager;
import org.xenos.teleportplugin.managers.TeleportManager;

public class teleportplugin extends JavaPlugin {

    private static teleportplugin instance;
    private TeleportManager teleportManager;
    private HomeManager homeManager;

    @Override
    public void onEnable() {
        instance = this;

        teleportManager = new TeleportManager();
        homeManager = new HomeManager();

        // ثبت دستورات
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpAcceptCommand());
        getCommand("tpdeny").setExecutor(new TpDenyCommand());
        getCommand("tptoggle").setExecutor(new TpToggleCommand());
        getCommand("tpignore").setExecutor(new TpIgnoreCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());

        // ثبت لیسنر برای لغو تلپورت هنگام دمیج
        getServer().getPluginManager().registerEvents(new WarmupListener(), this);



        getLogger().info("✅ Xenos-TPA-HOME enabled! for updates check out ");
    }

    @Override
    public void onDisable() {
        getLogger().info("❌ XenosTeleportPlugin disabled.");
    }

    public static teleportplugin getInstance() {
        return instance;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }
}
