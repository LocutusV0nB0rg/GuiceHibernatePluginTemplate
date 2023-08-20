package borg.locutus.guicehibernateplugin.listener;

import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerData;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerDataRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class PlayerJoinListener implements Listener {
    private final PlayerDataRepository playerDataRepository;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();

        PlayerData playerData = new PlayerData();
        playerData.setUuid(player.getUniqueId().toString());
        playerData.setName(player.getName());
        playerData.setLastJoin(Timestamp.from(Instant.now()));
        CompletableFuture<String> save = playerDataRepository.save(playerData);
        save.whenComplete((string, throwable) -> {
            if (throwable != null) {
                try {
                    playerDataRepository.update(playerData).join();
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        });
    }
}
