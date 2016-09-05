package ua.com.juja.sqlcmd.controller.command;

import sun.nio.ch.FileChannelImpl;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by MEBELBOS-2 on 04.09.2016.
 */
public class Edit implements Command {
    private View view;
    private DatabaseManager manager;

    public Edit(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("edit");
    }

    @Override
    public void process(String input) {
        try{
            String[] command = input.split(" ");
            if (command.length != 2){
                throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
            }
            String tableName = command[1];
            view.write("Enter the data when you want to change (edit):");
            String[] columnName = manager.getColumnNames(tableName);
            DataSet insertData = new DataSet();
            int dataChange = Integer.parseInt(view.read());
            for (String aColumnName : columnName) {
                view.write("Input " + aColumnName + ":");
                Object value = view.read();
                insertData.put(aColumnName, value);
            }
            manager.update(tableName, dataChange, insertData);
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
