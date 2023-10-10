package org.kcrha.weather.models.cli;

import org.apache.commons.io.input.CharSequenceReader;
import org.kcrha.weather.BaseFileReader;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader extends BaseFileReader {

    private static final String SECRETS_PROPERTIES_FILE = "secrets.properties";
    private static Properties PROPERTIES;

    public static Properties getSecretProperties() throws IOException {
        if (PROPERTIES == null) {
            String fileContents = readFile(SECRETS_PROPERTIES_FILE);
            Properties prop = new Properties();
            try (CharSequenceReader csr = new CharSequenceReader(fileContents)) {
                prop.load(csr);
            }
            PROPERTIES = prop;
        }

        return PROPERTIES;
    }

    public static String getSecretProperty(String propertyName) throws IOException {
        Properties props = PropertyReader.getSecretProperties();
        return (String) props.get(propertyName);
    }
}
