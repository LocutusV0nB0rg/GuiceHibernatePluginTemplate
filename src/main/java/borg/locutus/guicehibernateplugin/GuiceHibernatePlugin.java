package borg.locutus.guicehibernateplugin;

import borg.locutus.guicehibernateplugin.entities.student.Student;
import borg.locutus.guicehibernateplugin.entities.student.StudentRepository;
import borg.locutus.guicehibernateplugin.guice.ExampleModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuiceHibernatePlugin extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new ExampleModule(this));

        StudentRepository studentRepository = injector.getInstance(StudentRepository.class);
        Student student = new Student();
        student.setAge(19);
        student.setName("Toby");
        studentRepository.saveOrUpdate(student);
    }

    @Override
    public void onDisable() {
    }
}
