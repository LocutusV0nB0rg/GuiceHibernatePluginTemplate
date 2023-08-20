package borg.locutus.guicehibernateplugin.hibernate.entities.playerdata;

import borg.locutus.guicehibernateplugin.hibernate.entities.Repository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;

@Singleton
public class PlayerDataRepository extends Repository<PlayerData, String> {
    @Inject
    protected PlayerDataRepository(SessionFactory factory) {
        super(PlayerData.class, factory);
    }
}
