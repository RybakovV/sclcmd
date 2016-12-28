package ua.com.juja.rybakov.sclcmd.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Vitaliy Ryvakov on 10.12.2016.
 */
class ServiceStartStop {
    public static void main(String args[]) {
        String[] script = {"sc", args[0], args[1]};
        try {
            new ProcessBuilder(script).start();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
