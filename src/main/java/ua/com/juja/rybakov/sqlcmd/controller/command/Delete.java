package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Set;

public class Delete implements Command {
    private DatabaseManager manager;
    private View view;

    public Delete(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete");
    }

    @Override
    public void process(String input) {
        String[] command = parse(input);
        String tableName = command[1];
        Set<String> columnName = manager.getColumnNames(tableName);

        if (columnName.size() > 0) {
            view.write("Enter the data when you want to delete.");
            DataSet deleteData = new DataSetImpl();
            for (String name : columnName) {
                view.write("Input " + name + ":");
                Object value = view.read();
                deleteData.put(name, value);
            }
            manager.delete(tableName, deleteData);
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }

    private String[] parse(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        return command;
    }
}
