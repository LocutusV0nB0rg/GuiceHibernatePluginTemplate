package borg.locutus.guicehibernateplugin.listener;

import borg.locutus.guicehibernateplugin.hibernate.entities.student.Student;
import borg.locutus.guicehibernateplugin.hibernate.entities.student.StudentRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class BlockBreakListener implements Listener {
    private final StudentRepository studentRepository;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Block block = blockBreakEvent.getBlock();
        int id = block.getType().getId();

        Student student = studentRepository.findById(id).join();
        blockBreakEvent.getPlayer().sendMessage(student.toString());
    }
}
