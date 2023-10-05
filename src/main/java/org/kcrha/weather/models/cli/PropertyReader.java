package org.kcrha.weather.models.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final String SECRETS_PROPERTIES_FILE = "secrets.properties";

    public static Properties getSecretProperties() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(SECRETS_PROPERTIES_FILE);
        prop.load(stream);
        return prop;
    }
}
