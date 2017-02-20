package ua.com.juja.rybakov.sqlcmd.controller.command;


import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Set;

import static ua.com.juja.rybakov.sqlcmd.controller.command.ParseCommand.parseCommand;

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
            int dataChange = Integer.parseInt(view.read());
            manager.update(tableName, dataChange, inputData(columnName));
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }

    private DataSet inputData(Set<String> columnName) {
        DataSet insertData = new DataSetImpl();
        for (String name : columnName) {
            view.write("Input new " + name + ":");
            Object value = view.read();
            insertData.put(name, value);
        }
        return insertData;
    }
}
