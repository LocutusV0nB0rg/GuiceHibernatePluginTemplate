package borg.locutus.guicehibernateplugin.entities.student;

import borg.locutus.guicehibernateplugin.entities.Repository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;

@Singleton
public class StudentRepository extends Repository<Student, Integer> {

    @Inject
    public StudentRepository(SessionFactory factory) {
        super(Student.class, factory);
    }
}
