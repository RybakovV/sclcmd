package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by Vitaliy Ryvakov on 20.11.2016.
 */
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
        view.write(manager.clear(tableName));
    }
}
