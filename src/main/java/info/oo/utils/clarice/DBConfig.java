package info.oo.utils.clarice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static String url;
    private static String user;
    private static String password;
    
    static {
        try (
            InputStream configFile = getResourceAsStream("config.properties");
        ) {
            loadConfigFile(configFile);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    private static InputStream getResourceAsStream(String res) {
        return DBConfig.class
            .getClassLoader()
            .getResourceAsStream(res);
    }

    private static void loadConfigFile(InputStream configFile) throws IOException {
        Properties props = new Properties();
        props.load(configFile);
        parseConfigProperties(props);
    }

    private static void parseConfigProperties(Properties props) {

        user = props.getProperty("DB_USER");
        password = props.getProperty("DB_PASSWORD");

        url = composeDatabaseURL(
            props.getProperty("DB_URL"),
            props.getProperty("DB_SCHEMA")
        );
    }

    private static String composeDatabaseURL(String url, String schema) {
        return url + "/" + schema;
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
