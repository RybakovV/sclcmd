package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.controller.command.*;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.Console;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class Main {
    private static View view;
    private static DatabaseManager manager;
    private static Command[] commands;

    public static void main(String[] args) {
        view = new Console();
        manager = new MysqlDatabaseManager();
        commands = new Command[]{
                new Help(view),
                new Exit(view),
                new IsConnected(view, manager)};
        view.write("Hello");

        try {
            while (true) {
                view.write("Enter command (or command 'help' for help): ");
                String input = view.read();
                if (input.equals("connect")){
                    doConnect();
                    continue;
                }
                //commands = initializeCommands();
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
        } catch (ExitException e) {
            //do nothing
        }
    }

    private static void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }

    private static void doConnect() {
        int countTry = 0;
        while (countTry < 3) {
            countTry++;
            view.write("Enter Database name: ");
            String databaseName = view.read();
            view.write("Enter userName");
            String userName = view.read();
            view.write("Enter password");
            String userPassword = view.read();
            try {
                manager.connectToDataBase(databaseName, userName, userPassword);
                if (manager.getVersionDatabase().equals("MySQL")) {
                    manager = new MysqlDatabaseManager();
                    commands = initializeCommands();
                    manager.connectToDataBase(databaseName, userName, userPassword);
                }
                if (manager.getVersionDatabase().equals(("PostgreSQL"))) {
                    manager = new PostgresqlDatabaseManager();
                    commands = initializeCommands();
                    manager.connectToDataBase(databaseName, userName, userPassword);
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

    private static Command[] initializeCommands() {
        return new Command[]{
                new Help(view),
                new Exit(view),
                //new Connect(view, manager),
                new List(view, manager),
                new Print(view, manager),
                new Edit(view, manager),
                new Insert(view, manager),
                new Clear(view, manager),
                new NonExisten(view)};
    }

}



