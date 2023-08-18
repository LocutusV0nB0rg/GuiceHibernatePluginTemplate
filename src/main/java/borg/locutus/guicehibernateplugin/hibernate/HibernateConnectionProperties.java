package borg.locutus.guicehibernateplugin.hibernate;

import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;

import java.util.Properties;

@Data
public class HibernateConnectionProperties {
    private final String url;
    private final String username;
    private final String password;

    public Properties toProperties() {
        final Properties properties = new Properties();

        properties.put(Environment.SHOW_SQL, "false");
        properties.put(Environment.FORMAT_SQL, "false");

        properties.put(Environment.ORDER_INSERTS, "true");
        properties.put(Environment.ORDER_UPDATES, "true");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        properties.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        properties.put(Environment.URL, this.url);
        properties.put(Environment.USER, this.username);
        properties.put(Environment.PASS, this.password);

        return properties;
    }
}
