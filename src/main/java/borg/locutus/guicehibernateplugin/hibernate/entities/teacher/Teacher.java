package borg.locutus.guicehibernateplugin.hibernate.entities.teacher;

import borg.locutus.guicehibernateplugin.hibernate.entities.student.Student;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private List<Student> students;

}
