import repository.DatabaseConnection;
import ui.MainMenuUI;
import utils.AnsiColor;
import utils.Utils;

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
            Utils.showMessageColor("\nDatabase Server not reachable!" +
                    "\nStarting app offline.", AnsiColor.YELLOW);
            MainMenuUI menu = new MainMenuUI();
            menu.run();
        } catch (Exception e) {
            Utils.showMessageColor("\nError!" +
                    "\n" + e.getMessage(), AnsiColor.RED);
        }
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties(System.getProperties());

        InputStream inputStream = new App().getClass().getClassLoader().getResourceAsStream("config/application.properties");
        properties.load(inputStream);
        inputStream.close();

        System.setProperties(properties);
    }
}
