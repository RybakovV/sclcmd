package ua.com.juja.rybakov.sqlcmd.model;

import ua.com.juja.rybakov.sqlcmd.controller.command.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Vitaliy Ryvakov on 05.12.2016.
 */
public class Configuration {
    public static final String CONFIGURATION_FILE = "config/default.properties";
    private Properties properties;

    public Configuration() {
        loadProperties(new File(CONFIGURATION_FILE));
    }

    private void loadProperties(File file) {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        } catch (Exception e) {
            System.out.println("Error loading configuration" + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    String getMysqlServer() {
        return properties.getProperty("mysql.server");
    }

    String getMysqlPort() {
        return properties.getProperty("mysql.port");
    }

    String getMysqlUseSsl() {
        return properties.getProperty("mysql.usessl");
    }

    String getPostgreSqlServer() {
        return properties.getProperty("postgresql.server");
    }

    String getPostgresqlPort() {
        return properties.getProperty("postgresql.port");
    }

}
