package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import java.sql.SQLException;

public class Clear implements Command{
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
        if (command.length != 2){
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
        }
        String tableName = command[1];
        String message= "The table '" + tableName + "' is cleared.";
        try {
            view.write("All data will be deleted from the table. Do you really want to do it? (Y/N):");
            String answer = view.read();
            //TODO TryCount
            int tryCount = 0;
            while (tryCount<3){
                tryCount++;
                if (answer.equals("y")|answer.equals("Y")) {
                    manager.clear(tableName);
                    break;
                }else if (answer.equals("n")|answer.equals("N")){
                    message = "The table is not cleared";
                    break;
                } else {
                    view.write("Enter Y (if yes) or N (if no):");
                    answer = view.read();
                }
            }
            if (tryCount==3) {
                throw new SQLException("Too many attempts");
            }
        } catch (SQLException e) {
            message = "The table '" + tableName + "' is not cleared. Because: " + e.getMessage();
        }
        view.write(message);
    }
}
