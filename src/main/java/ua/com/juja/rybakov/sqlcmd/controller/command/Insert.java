package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Set;

import static ua.com.juja.rybakov.sqlcmd.controller.command.ParseCommand.parseCommand;

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
        String[] command = parseCommand(input);
        String tableName = command[1];
        Set<String> columnName = manager.getColumnNames(tableName);

        if (columnName.size() > 0) {
            view.write("Enter the data when you want to insert.");
            DataSet insertData = new DataSetImpl();
            for (String name : columnName) {
                view.write("Input " + name + ":");
                Object value = view.read();
                insertData.put(name, value);
            }
            manager.insert(tableName, insertData);
            view.write("Data inserting successfully.");
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }
}
