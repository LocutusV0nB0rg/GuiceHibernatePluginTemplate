package borg.locutus.guicehibernateplugin.hibernate.entities.student;

import borg.locutus.guicehibernateplugin.hibernate.entities.teacher.Teacher;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;

    private int age;

    @ManyToOne
    private Teacher teacher;
}
