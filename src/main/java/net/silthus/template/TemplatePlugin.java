package dev.svvo.plugins.template;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class RaidClaims extends JavaPlugin {
    public static RaidClaims instance;
    public HashMap<String, UUID> claimedChunks = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        getCommand("claim").setExecutor(new ClaimCommand());
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getLogger().info("RaidClaims plugin enabled.");
    }

    public static RaidClaims getInstance() {
        return instance;
    }
}
