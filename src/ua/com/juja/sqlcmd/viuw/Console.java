package ua.com.juja.sqlcmd.viuw;

import java.util.Scanner;

/**
 * Created by MEBELBOS-2 on 01.09.2016.
 */
public class Console implements View {

    @Override
    public void write(String massage) {
        System.out.println(massage);

    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
