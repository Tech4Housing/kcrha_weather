package org.kcrha.weather.models.cli;

import org.apache.commons.io.input.CharSequenceReader;
import org.kcrha.weather.BaseFileReader;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader extends BaseFileReader {

    private static final String SECRETS_PROPERTIES_FILE = "secrets.properties";

    public static Properties getSecretProperties() throws IOException {
        String fileContents = readFile(SECRETS_PROPERTIES_FILE);
        Properties prop = new Properties();
        try (CharSequenceReader csr = new CharSequenceReader(fileContents)) {
            prop.load(csr);
        }

        return prop;
    }
}
