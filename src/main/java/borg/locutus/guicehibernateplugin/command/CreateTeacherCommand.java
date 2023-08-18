package borg.locutus.guicehibernateplugin.command;

import borg.locutus.guicehibernateplugin.hibernate.entities.student.Student;
import borg.locutus.guicehibernateplugin.hibernate.entities.student.StudentRepository;
import borg.locutus.guicehibernateplugin.hibernate.entities.teacher.Teacher;
import borg.locutus.guicehibernateplugin.hibernate.entities.teacher.TeacherRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class CreateTeacherCommand implements CommandExecutor {
    private final TeacherRepository teacherRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Du musst einen Namen  angeben.");
            return true;
        }

        String name = args[0];

        Teacher teacher = new Teacher();
        teacher.setName(name);
        Integer id = teacherRepository.save(teacher).join();

        sender.sendMessage("Der Lehrer " + name + " hat die ID " + id + " erhalten.\n");

        return true;
    }
}
