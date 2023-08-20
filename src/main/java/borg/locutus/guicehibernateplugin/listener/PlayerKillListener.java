package borg.locutus.guicehibernateplugin.listener;

import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerData;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerDataRepository;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerkill.PlayerKill;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerkill.PlayerKillRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class PlayerKillListener implements Listener {
    private final PlayerDataRepository playerDataRepository;
    private final PlayerKillRepository playerKillRepository;

    @EventHandler
    public void onPlayerKillPlayer(PlayerDeathEvent playerDeathEvent) {
        Player killed = playerDeathEvent.getEntity();
        Player killer = killed.getKiller();

        CompletableFuture.runAsync(() -> {
            try {
                PlayerData killedPlayerData = playerDataRepository.findById(killed.getUniqueId().toString()).join();
                PlayerData killerPlayerData = playerDataRepository.findById(killer.getUniqueId().toString()).join();

                PlayerKill playerKill = new PlayerKill();
                playerKill.setKilled(killedPlayerData);
                playerKill.setKiller(killerPlayerData);

                playerKill.setX(killed.getLocation().getBlockX());
                playerKill.setY(killed.getLocation().getBlockY());
                playerKill.setZ(killed.getLocation().getBlockZ());
                playerKill.setWorld(killed.getWorld().getName());

                ItemStack killerItemInHand = killer.getItemInHand();
                playerKill.setWeapon(killerItemInHand == null ? "" : killerItemInHand.getType().toString());
                playerKillRepository.save(playerKill).join();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
