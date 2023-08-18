package borg.locutus.guicehibernateplugin;

import borg.locutus.guicehibernateplugin.command.CreateStudentCommand;
import borg.locutus.guicehibernateplugin.command.CreateTeacherCommand;
import borg.locutus.guicehibernateplugin.guice.ExampleModule;
import borg.locutus.guicehibernateplugin.listener.BlockBreakListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuiceHibernatePlugin extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {
        injector = Guice.createInjector(new ExampleModule(this));

        this.getCommand("createstudent").setExecutor(injector.getInstance(CreateStudentCommand.class));
        this.getCommand("createteacher").setExecutor(injector.getInstance(CreateTeacherCommand.class));

        Bukkit.getPluginManager().registerEvents(injector.getInstance(BlockBreakListener.class), injector.getInstance(GuiceHibernatePlugin.class));
    }

    @Override
    public void onDisable() {

    }
}
