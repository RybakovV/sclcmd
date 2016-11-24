package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

public class Insert implements Command {
    private DatabaseManager manager;
    private View view;

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert");
    }

    @Override
    public void process(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        String tableName = command[1];
        String[] columnName = manager.getColumnNames(tableName);
        if (columnName.length > 0) {
            view.write("Enter the data when you want to insert.");
            DataSet insertData = new DataSet();
            for (String aColumnName : columnName) {
                view.write("Input " + aColumnName + ":");
                Object value = view.read();
                insertData.put(aColumnName, value);
            }
            manager.insert(tableName, insertData);
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }
}
