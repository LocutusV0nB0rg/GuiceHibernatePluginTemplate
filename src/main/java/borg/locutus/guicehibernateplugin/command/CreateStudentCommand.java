package borg.locutus.guicehibernateplugin.command;

import borg.locutus.guicehibernateplugin.hibernate.entities.student.Student;
import borg.locutus.guicehibernateplugin.hibernate.entities.student.StudentRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class CreateStudentCommand implements CommandExecutor {
    private final StudentRepository studentRepository;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Du musst einen Namen und ein Alter angeben.");
            return true;
        }

        String name = args[0];
        String ageString = args[1];

        int age;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException numberFormatException) {
            sender.sendMessage("Das Alter muss eine ganze Zahl sein");
            return true;
        }

        Student student = new Student();
        student.setAge(age);
        student.setName(name);
        Integer id = studentRepository.save(student).join();

        sender.sendMessage("Der Student " + student + " (" + age + ") hat die ID " + id + " erhalten.\n");

        return true;
    }
}
