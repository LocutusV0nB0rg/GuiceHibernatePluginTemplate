package borg.locutus.guicehibernateplugin;

import borg.locutus.guicehibernateplugin.commands.PlayerStatsCommand;
import borg.locutus.guicehibernateplugin.guice.ExampleModule;
import borg.locutus.guicehibernateplugin.listener.PlayerJoinListener;
import borg.locutus.guicehibernateplugin.listener.PlayerKillListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuiceHibernatePlugin extends JavaPlugin {
    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new ExampleModule(this));

        this.getCommand("playerstats").setExecutor(injector.getInstance(PlayerStatsCommand.class));

        Bukkit.getPluginManager().registerEvents(injector.getInstance(PlayerJoinListener.class), injector.getInstance(GuiceHibernatePlugin.class));
        Bukkit.getPluginManager().registerEvents(injector.getInstance(PlayerKillListener.class), injector.getInstance(GuiceHibernatePlugin.class));
    }
}
