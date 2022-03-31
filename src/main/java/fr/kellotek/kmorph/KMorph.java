package fr.kellotek.kmorph;

import fr.kellotek.kmorph.commands.CommandMorph;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class KMorph extends JavaPlugin {

    public static Map<UUID, UUID> morphed = new HashMap<>();

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("morph")).setExecutor(new CommandMorph());

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Map.Entry<UUID, UUID> morphs : KMorph.morphed.entrySet()) {
                    if(Bukkit.getOfflinePlayer(morphs.getKey()).isOnline()) {
                        Player player = Bukkit.getPlayer(morphs.getKey());
                        LivingEntity le = (LivingEntity) Bukkit.getEntity(morphs.getValue());
                        assert player != null;
                        assert le != null;
                        le.teleport(player.getLocation());
                    }
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }
}
