package dev.svvo.plugins.template;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ExplosionListener implements Listener {

    private final int RADIUS = 6;
    private final long REGEN_DELAY = 20L * 30; // 30 seconds

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        Location loc = event.getLocation();
        Chunk chunk = loc.getChunk();
        String key = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();

        if (!RaidClaims.getInstance().claimedChunks.containsKey(key)) return;

        List<BlockStateSnapshot> snapshotList = new ArrayList<>();

        for (Block block : getNearbyBlocks(loc, RADIUS)) {
            if (block.getType() != Material.AIR)
                snapshotList.add(new BlockStateSnapshot(block));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (BlockStateSnapshot snap : snapshotList) {
                    snap.restore();
                }
                loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 40);
            }
        }.runTaskLater(RaidClaims.getInstance(), REGEN_DELAY);
    }

    private List<Block> getNearbyBlocks(Location center, int radius) {
        List<Block> blocks = new ArrayList<>();
        World world = center.getWorld();

        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(world.getBlockAt(cx + x, cy + y, cz + z));
                }
            }
        }

        return blocks;
    }

    private static class BlockStateSnapshot {
        private final Location location;
        private final Material type;

        public BlockStateSnapshot(Block block) {
            this.location = block.getLocation();
            this.type = block.getType();
        }

        public void restore() {
            Block block = location.getBlock();
            block.setType(type, false);
        }
    }
}
