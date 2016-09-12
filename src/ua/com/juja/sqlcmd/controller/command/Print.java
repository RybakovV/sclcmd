package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class Print implements Command {

    private View view;
    private DatabaseManager manager;

    public Print(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("print");
    }

    @Override
    public void process(String input) {
        try {
            String[] command = input.split(" ");
            if (command.length != 2) {
                throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
            }
            String tableName = command[1];
            view.write(manager.getTableString(tableName));
        }catch (Exception e){
            printError(e);
        }

    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null){
            message += " " + e.getCause().getMessage();
        }
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }
}
