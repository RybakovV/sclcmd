package ua.com.juja.rybakov.sqlcmd.controller.command;


import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

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
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        String tableName = command[1];
        String[] columnName = manager.getColumnNames(tableName);
        if (columnName.length > 0) {
            view.write("Enter 'id' row when you want to change (edit): ");
            DataSet insertData = new DataSet();
            int dataChange = Integer.parseInt(view.read());
            for (String aColumnName : columnName) {
                view.write("Input new " + aColumnName + ":");
                Object value = view.read();
                insertData.put(aColumnName, value);
            }
            manager.update(tableName, dataChange, insertData);
        } else {
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist");
        }
    }

}
