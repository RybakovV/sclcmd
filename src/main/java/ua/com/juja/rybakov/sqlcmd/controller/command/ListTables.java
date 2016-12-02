package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Arrays;

public class ListTables implements Command {

    private View view;
    private DatabaseManager manager;

    public ListTables(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        String[] tables = manager.getAllTablesOfDataBase();
        view.write(Arrays.toString(tables));
    }

}
