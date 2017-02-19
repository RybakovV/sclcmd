package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.controller.command.*;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgreSqlDatabaseManager;
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
                if (input.equals("connect")) {
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
        String message = buildMessage(e);
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }

    private static void doConnect() {
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

    private static void connect(Sign sign) {
        manager.connectToDataBase(sign);
        if (manager.getVersionDatabase().equals(("PostgreSQL"))) {
            manager = new PostgreSqlDatabaseManager();
            commands = initializeCommands();
            manager.connectToDataBase(sign);
        } else {
            manager = new MysqlDatabaseManager();
            commands = initializeCommands();
            manager.connectToDataBase(sign);
        }
    }

    private static void procesErrore(int countTry, Exception e) {
        String message = buildMessage(e);
        checkTry(countTry, message);
    }

    private static void checkTry(int countTry, String message) {
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

    private static Command[] initializeCommands() {
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



