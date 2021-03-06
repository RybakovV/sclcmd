package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class IsConnected implements Command {
    private View view;
    private DatabaseManager manager;


    public IsConnected(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write("You must connected to database with command 'connect'");
    }

}
