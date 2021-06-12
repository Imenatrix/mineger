package info.oo.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static String url;
    private static String user;
    private static String password;
    
    static {
        try (InputStream configFile = DBConfig.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties properties = new Properties();
            properties.load(configFile);

            url = properties.getProperty("DB_URL") + "/" + properties.getProperty("DB_SCHEMA");
            user = properties.getProperty("DB_USER");
            password = properties.getProperty("DB_PASSWORD");

        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getURL() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

}
