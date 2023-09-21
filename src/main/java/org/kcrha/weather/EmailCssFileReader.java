package org.kcrha.weather;

public class EmailCssFileReader extends BaseFileReader {
    public static final String EMAIL_CSS_FILE_PATH = "style.css";

    public static String getStyle() {

        return EmailCssFileReader.readFile(EMAIL_CSS_FILE_PATH);
    }
}
