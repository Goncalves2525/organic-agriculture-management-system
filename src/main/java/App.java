import ui.MainMenuUI;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        try {
            loadProperties();

            String ipAddress = System.getProperty("database.inet");
            InetAddress inet = InetAddress.getByName(ipAddress);

            MainMenuUI menu = new MainMenuUI();
            menu.run();

            DatabaseConnection.getInstance().closeConnection();
        } catch (UnknownHostException e) {
            System.out.println("\nDatabase Server not reachable!");

            MainMenuUI menu = new MainMenuUI();
            menu.run();

        } catch (Exception e) {
            System.out.println("App properties not loaded!");
        }
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties(System.getProperties());

        InputStream inputStream = new App().getClass().getClassLoader().getResourceAsStream("application.properties");
        properties.load(inputStream);
        properties.load(inputStream);
        inputStream.close();

        System.setProperties(properties);
    }
}
