package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.sql.SQLException; //TODO Потрібно віддати клієнту

import static ua.com.juja.rybakov.sqlcmd.controller.command.ParseCommand.parseCommand;

public class Clear implements Command {
    private View view;
    private DatabaseManager manager;

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
        String[] command = parseCommand(input);
        String tableName = command[1];
        String message = "The table '" + tableName + "' is cleared.";
        try {
            view.write("All data will be deleted from the table. Do you really want to do it? (Y/N):");
            String answer = view.read();
            confirmClear(answer, tableName);
        } catch (SQLException e) {
            message = "The table '" + tableName + "' is not cleared. Because: " + e.getMessage();
        }
        view.write(message);
    }

    private void confirmClear(String answer, String tableName) throws SQLException {
        int attemptsCount = 0;
        while (attemptsCount < 3) {
            attemptsCount++;

            if (answer.equals("y") | answer.equals("Y")) {
                manager.clear(tableName);
                break;
            }

            if (answer.equals("n") | answer.equals("N")) {
                view.write("The table is not cleared");
                break;
            }

            view.write("Enter Y (if yes) or N (if no):");
            answer = view.read();
        }
        if (attemptsCount == 3) {
            throw new SQLException("too many attempts");
        }
    }

}
