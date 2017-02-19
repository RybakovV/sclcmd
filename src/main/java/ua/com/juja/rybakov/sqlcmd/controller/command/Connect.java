package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.controller.Sign;
import ua.com.juja.rybakov.sqlcmd.controller.SignReader;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgreSqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class Connect implements Command {
    private final View view;
    private DatabaseManager manager;

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect");
    }

    @Override
    public void process(String input) {
        int countTry = 0;
        while (countTry < 3) {
            countTry++;
            Sign sign = new SignReader().read(view);
            try {
                manager.connectToDataBase(sign);
                if (manager.getVersionDatabase().equals("MySQL")) {
                    manager = new MysqlDatabaseManager();
                    //commands = initializeCommands();
                    manager.connectToDataBase(sign);
                }
                if (manager.getVersionDatabase().equals(("PostgreSQL"))) {
                    manager = new PostgreSqlDatabaseManager();
                    //commands = initializeCommands();
                    manager.connectToDataBase(sign);
                }
                view.write("You connected to " + manager.getVersionDatabase() + " database");
                break;
            } catch (Exception e) {
                String message = e.getMessage();
                if (e.getCause() != null) {
                    message += " " + e.getCause().getMessage();
                }
                view.write("You can't connect to the database. Because: " + message);
                if (countTry < 3) {
                    view.write("Try again");
                } else {
                    view.write("Enough try");
                }
            }
        }
    }

    private Command[] initializeCommands() {
        return new Command[]{
                new Help(view),
                new Exit(view),
                new ListTables(view, manager),
                new Print(view, manager),
                new Edit(view, manager),
                new Insert(view, manager),
                new Clear(view, manager),
                new NonExisten(view)};
    }

}
