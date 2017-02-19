package ua.com.juja.rybakov.sqlcmd.viuw;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */

public class Console implements View {

    @Override
    public void write(String massage) {
        System.out.println(massage);
    }

    @Override
    public String read() {
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return "";
        }
    }
}
