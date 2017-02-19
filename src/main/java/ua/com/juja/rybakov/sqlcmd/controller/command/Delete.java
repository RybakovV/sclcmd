package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Set;

import static ua.com.juja.rybakov.sqlcmd.controller.command.ParseCommand.parseCommand;

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
        String[] command = parseCommand(input);
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

}
