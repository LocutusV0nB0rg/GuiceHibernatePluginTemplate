package borg.locutus.guicehibernateplugin.commands;

import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerData;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerdata.PlayerDataRepository;
import borg.locutus.guicehibernateplugin.hibernate.entities.playerkill.PlayerKillRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class PlayerStatsCommand implements CommandExecutor {
    private final PlayerKillRepository playerKillRepository;
    private final PlayerDataRepository playerDataRepository;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("guicehibernateplugin.playerstats")) {
            sender.sendMessage("You have no permission to execute this command");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("You must add the uuid of the player you want to look up!");
            return true;
        }

        String uuid = args[0];

        CompletableFuture.runAsync(() -> {
            try {
                PlayerData playerData = playerDataRepository.findById(uuid).join();

                Long numberOfKills = playerKillRepository.getNumberOfKills(playerData).join();
                Long numberOfDeathsByKill = playerKillRepository.getNumberOfDeaths(playerData).join();

                sender.sendMessage("-----");
                sender.sendMessage("Killed targets: " + numberOfKills);
                sender.sendMessage("Deaths by kill: " + numberOfDeathsByKill);
                sender.sendMessage("-----");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });

        return true;
    }
}
