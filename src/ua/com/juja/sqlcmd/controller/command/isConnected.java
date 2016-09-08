package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by MEBELBOS-2 on 05.09.2016.
 */
public class IsConnected implements Command {
    private View view;
    private static DatabaseManager manager;

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
        view.write("You mast connected to database with command 'connected'");
    }
}
