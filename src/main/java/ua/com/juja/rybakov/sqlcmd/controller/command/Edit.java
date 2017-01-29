package ua.com.juja.rybakov.sqlcmd.controller.command;


import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Set;

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
        String[] command = parseCommand(input);
        String tableName = command[1];
        Set<String> columnName = manager.getColumnNames(tableName);

        if (columnName.size() > 0) {
            view.write("Enter 'id' row when you want to change (edit): ");
            DataSet insertData = new DataSetImpl();
            int dataChange = Integer.parseInt(view.read());
            for (String name : columnName) {
                view.write("Input new " + name + ":");
                Object value = view.read();
                insertData.put(name, value);
            }
            manager.update(tableName, dataChange, insertData);
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }

    private String[] parseCommand(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        return command;
    }

}
