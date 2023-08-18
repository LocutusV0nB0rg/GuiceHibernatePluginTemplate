package borg.locutus.guicehibernateplugin.entities.student;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Student {
    @Id
    private int id;

    private String name;

    private int age;
}
