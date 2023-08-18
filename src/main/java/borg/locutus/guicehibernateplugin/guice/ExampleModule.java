package borg.locutus.guicehibernateplugin.guice;

import borg.locutus.guicehibernateplugin.GuiceHibernatePlugin;
import borg.locutus.guicehibernateplugin.hibernate.HibernateConnectionProperties;
import borg.locutus.guicehibernateplugin.hibernate.HibernateConnectionPropertiesProvider;
import borg.locutus.guicehibernateplugin.hibernate.SessionFactoryProvider;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

@RequiredArgsConstructor
public class ExampleModule extends AbstractModule {
    private final GuiceHibernatePlugin plugin;

    @Override
    protected void configure() {
        bind(GuiceHibernatePlugin.class).toInstance(plugin);

        bind(Gson.class).toProvider(GsonProvider.class);

        bind(HibernateConnectionProperties.class).toProvider(HibernateConnectionPropertiesProvider.class);
        bind(SessionFactory.class).toProvider(SessionFactoryProvider.class);
    }
}
