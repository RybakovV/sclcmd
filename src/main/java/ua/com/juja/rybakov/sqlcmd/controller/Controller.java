package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.controller.command.*;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgreSqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

/**
 * Created by Vitaliy Ryvakov on 20.02.2017.
 */
public class Controller {
    private View view;
    private DatabaseManager manager;
    private Command[] commands;

    public Controller(View view) {
        this.view = view;
        init();
    }

    public void run() {
        try {
            while (true) {
                view.write("Enter command (or command 'help' for help): ");
                String input = view.read();
                if (input.equals("connect")) {
                    doConnect();
                    continue;
                }
                //commands = initializeCommands();
                processCommand(input);
            }
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void init() {
        manager = new MysqlDatabaseManager();
        commands = new Command[]{
                new Help(view),
                new Exit(view),
                new IsConnected(view, manager)};
        view.write("Hello");
    }

    private void processCommand(String input) {
        for (Command command : commands) {
            try {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            } catch (Exception e) {
                if (e instanceof ExitException) {
                    throw e;
                }
                printError(e);
                break;
            }
        }
    }

    private void printError(Exception e) {
        String message = buildMessage(e);
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }

    private void doConnect() {
        int countTry = 0;
        while (countTry < 3) {
            countTry++;
            Sign sign = new SignReader().read(view);
            try {
                connect(sign);
                view.write("You connected to " + manager.getVersionDatabase() + " database");
                break;
            } catch (Exception e) {
                procesErrore(countTry, e);
            }
        }
    }

    private void connect(Sign sign) {
        manager.connectToDataBase(sign);
        if (manager.getVersionDatabase().equals(("PostgreSQL"))) {
            manager = new PostgreSqlDatabaseManager();
        } else {
            manager = new MysqlDatabaseManager();
        }
        commands = initializeCommands();
        manager.connectToDataBase(sign);

    }

    private void procesErrore(int countTry, Exception e) {
        String message = buildMessage(e);
        checkTry(countTry, message);
    }

    private void checkTry(int countTry, String message) {
        view.write("You can't connect to the database. Because: " + message);
        if (countTry < 3) {
            view.write("Try again");
        } else {
            view.write("Enough try");
        }
    }

    private static String buildMessage(Exception e) {
        String result = e.getMessage();
        if (e.getCause() != null) {
            result += " " + e.getCause().getMessage();
        }
        return result;
    }

    private Command[] initializeCommands() {
        return new Command[]{
                new Help(view),
                new Exit(view),
                //new Connect(view, manager),
                new ListDatabase(view, manager),
                new ListTables(view, manager),
                new Print(view, manager),
                new Edit(view, manager),
                new Insert(view, manager),
                new Delete(view, manager),
                new Clear(view, manager),
                new NonExisten(view)};
    }

}
