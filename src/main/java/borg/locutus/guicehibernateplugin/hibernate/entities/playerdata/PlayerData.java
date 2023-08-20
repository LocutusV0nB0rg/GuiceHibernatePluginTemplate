package borg.locutus.guicehibernateplugin.hibernate.entities.playerdata;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class PlayerData {
    @Id
    private String uuid;

    private String name;

    private Timestamp lastJoin;
}
