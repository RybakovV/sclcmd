package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.Arrays;
import java.util.Set;


public class ListDatabase implements Command {
    private View view;
    private DatabaseManager manager;

    public ListDatabase(View view, DatabaseManager manager) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("listdb");
    }

    @Override
    public void process(String command) {
        Set<String> dataBases = manager.getAllDataBases();
        view.write(dataBases.toString());

    }
}
