package borg.locutus.guicehibernateplugin.hibernate.entities.teacher;

import borg.locutus.guicehibernateplugin.hibernate.entities.Repository;
import com.google.inject.Inject;
import org.hibernate.SessionFactory;

public class TeacherRepository  extends Repository<Teacher, Integer> {

    @Inject
    protected TeacherRepository(SessionFactory factory) {
        super(Teacher.class, factory);
    }
}
