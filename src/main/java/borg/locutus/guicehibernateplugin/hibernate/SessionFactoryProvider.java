package borg.locutus.guicehibernateplugin.hibernate;


import borg.locutus.guicehibernateplugin.GuiceHibernatePlugin;
import borg.locutus.guicehibernateplugin.entities.student.Student;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class SessionFactoryProvider implements Provider<SessionFactory> {
    private final HibernateConnectionProperties hibernateConnectionProperties;

    @Override
    public SessionFactory get() {
        final StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        registryBuilder.applySettings(hibernateConnectionProperties.toProperties());

        final StandardServiceRegistry registry = registryBuilder.build();
        final MetadataSources sources = new MetadataSources(registry);

        sources.addAnnotatedClass(Student.class);

        final MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
        metadataBuilder.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE);

        final Metadata metadata = metadataBuilder.build();
        return metadata.getSessionFactoryBuilder().build();
    }
}