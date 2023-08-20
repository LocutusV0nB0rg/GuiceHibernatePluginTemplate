package borg.locutus.guicehibernateplugin.hibernate.entities.playerkill;

import borg.locutus.guicehibernateplugin.hibernate.entities.Repository;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerData;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.concurrent.CompletableFuture;

@Singleton
public class PlayerKillRepository extends Repository<PlayerKill, Integer> {
    @Inject
    protected PlayerKillRepository(SessionFactory factory) {
        super(PlayerKill.class, factory);
    }

    public CompletableFuture<Long> getNumberOfKills(PlayerData killer) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.openSession()) {
                final Query query = session.getNamedQuery("getNumberOfKills");
                query.setParameter("killer", killer);
                query.setMaxResults(1);
                return (Long) query.uniqueResult();
            }
        });
    }

    public CompletableFuture<Long> getNumberOfDeaths(PlayerData killed) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.openSession()) {
                final Query query = session.getNamedQuery("getNumberOfDeaths");
                query.setParameter("killed", killed);
                query.setMaxResults(1);
                return (Long) query.uniqueResult();
            }
        });
    }
}
