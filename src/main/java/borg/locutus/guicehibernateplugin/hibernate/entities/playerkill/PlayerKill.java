package borg.locutus.guicehibernateplugin.hibernate.entities.playerkill;

import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerData;
import lombok.Data;
import org.hibernate.annotations.CacheModeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Data
@NamedQueries({
        @NamedQuery(
                name = "getNumberOfKills",
                query = "select count(*) from PlayerKill where killer = :killer",
                cacheMode = CacheModeType.GET,
                fetchSize = 1
        ),
        @NamedQuery(
                name = "getNumberOfDeaths",
                query = "select count(*) from PlayerKill where killed = :killed",
                cacheMode = CacheModeType.GET,
                fetchSize = 1
        )
})
public class PlayerKill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int killId;

    @ManyToOne
    private PlayerData killer;

    @ManyToOne
    private PlayerData killed;

    private Timestamp killTime = Timestamp.from(Instant.now());

    private String weapon;

    private int x;
    private int y;
    private int z;
    private String world;
}
