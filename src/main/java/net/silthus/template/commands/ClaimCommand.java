package dev.svvo.plugins.template;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String key = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();

        if (RaidClaims.getInstance().claimedChunks.containsKey(key)) {
            player.sendMessage("§cThis chunk is already claimed!");
            return true;
        }

        RaidClaims.getInstance().claimedChunks.put(key, player.getUniqueId());
        player.sendMessage("§aYou claimed this chunk for raiding protection.");
        return true;
    }
}
