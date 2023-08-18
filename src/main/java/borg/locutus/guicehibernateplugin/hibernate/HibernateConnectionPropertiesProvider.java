package borg.locutus.guicehibernateplugin.hibernate;

import borg.locutus.guicehibernateplugin.GuiceHibernatePlugin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
public class HibernateConnectionPropertiesProvider implements Provider<HibernateConnectionProperties> {
    private final GuiceHibernatePlugin plugin;
    private final Gson gson;
    @Override
    public HibernateConnectionProperties get() {
        File directory = plugin.getDataFolder();

        if (directory.mkdirs()) {
            System.out.println("Plugindirectory created!");
        }

        try {
            final Path path = Paths.get(directory.toString(), "database.json");

            if (Files.notExists(path)) {
                @Cleanup
                InputStream in = HibernateConnectionPropertiesProvider.class.getClassLoader().getResourceAsStream("database.json");
                if (in != null) {
                    Files.copy(in, path);
                    System.out.println("Copied database.json from resources");
                } else {
                    System.out.println("Couldnt copy database.json from resources");
                }
            }

            @Cleanup final FileReader reader = new FileReader(path.toFile());
            final JsonObject object = gson.fromJson(reader, JsonObject.class);

            return new HibernateConnectionProperties(
                    object.get("url").getAsString(),
                    object.get("username").getAsString(),
                    object.get("password").getAsString()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
