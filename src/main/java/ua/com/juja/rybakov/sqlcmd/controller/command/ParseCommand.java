package ua.com.juja.rybakov.sqlcmd.controller.command;

/**
 * Created by Vitaliy Ryvakov on 20.02.2017.
 */
public class ParseCommand {
    static String[] parseCommand(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        return command;
    }

}
