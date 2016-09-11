package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.Console;
import ua.com.juja.sqlcmd.viuw.View;

import java.util.Arrays;


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
        try{
            while (true) {
                view.write("Enter command (or command 'help' for help): ");
                String input = view.read();
                if (input.equals("connect")){
                    doConnect();
                }else {
                    for (Command command: commands) {
                        try {
                            if (command.canProcess(input)){
                                command.process(input);
                                break;
                            }
                        }catch (Exception e){
                            if (e instanceof ExitException) {
                                throw e;
                            }
                            printError(e);
                            break;
                        }
                    }
                }
            }
        }catch (ExitException e){
            //do nothing
            //System.exit(0);
        }

    }

    private static void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null){
            message += " " + e.getCause().getMessage();
        }
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }

    private static void doConnect() {
        while (true){
            view.write("Enter Database name: ");
            String databaseName = view.read();
            view.write("Enter userName");
            String userName = view.read();
            view.write("Enter password");
            String userPassword = view.read();
            try{
                manager.connectToDataBase(databaseName, userName, userPassword);
                if (manager.getVersionDatabase().equals("MySQL")){
                    manager = new MysqlDatabaseManager();
                    commands = new Command[]{
                            new Help(view),
                            new Exit(view),
                            new List(view, manager),
                            new Print(view, manager),
                            new Edit(view, manager),
                            new Insert(view, manager),
                            new NonExisten(view)};
                    manager.connectToDataBase(databaseName, userName, userPassword);
                }
                if (manager.getVersionDatabase().equals(("PostgreSQL"))){
                    manager = new PostgresqlDatabaseManager();
                    commands = new Command[]{
                            new Help(view),
                            new Exit(view),
                            new List(view, manager),
                            new Print(view, manager),
                            new Edit(view, manager),
                            new Insert(view, manager),
                            new NonExisten(view)};
                    manager.connectToDataBase(databaseName, userName, userPassword);
                }
                break;
            }catch (Exception e){
                String message = e.getMessage();
                if (e.getCause() != null){
                    message += " " + e.getCause().getMessage();
                }
                view.write("You do not connected to database. Because: " + message);
                view.write("Try again");
            }
        }
        view.write("You connected to " + manager.getVersionDatabase() + " database" );
    }

}



