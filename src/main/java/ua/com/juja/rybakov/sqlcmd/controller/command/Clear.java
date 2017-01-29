package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.sql.SQLException; //TODO Потрібно віддати клієнту

public class Clear implements Command {
    private final View view;
    private final DatabaseManager manager;

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear");
    }

    @Override
    public void process(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        String tableName = command[1];
        String message = "The table '" + tableName + "' is cleared.";
        try {
            view.write("All data will be deleted from the table. Do you really want to do it? (Y/N):");
            String answer = view.read();
            int attemtsCount = 0;
            while (attemtsCount < 3) {
                if (answer.equals("y") | answer.equals("Y")) {
                    manager.clear(tableName);
                    break;
                }
                if (answer.equals("n") | answer.equals("N")) {
                    message = "The table is not cleared";
                    break;
                }

                view.write("Enter Y (if yes) or N (if no):");
                answer = view.read();
                attemtsCount++;
            }
            if (attemtsCount == 3) {
                throw new SQLException("too many attempts");
            }
        } catch (SQLException e) {
            message = "The table '" + tableName + "' is not cleared. Because: " + e.getMessage();
        }
        view.write(message);
    }
}
