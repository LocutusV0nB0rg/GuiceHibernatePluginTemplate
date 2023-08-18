package borg.locutus.guicehibernateplugin.guice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class GsonProvider implements Provider<Gson> {
    private Gson gson;

    @Override
    public Gson get() {
        if (gson != null) return gson;

        return gson = new GsonBuilder().setPrettyPrinting().create();
    }
}
