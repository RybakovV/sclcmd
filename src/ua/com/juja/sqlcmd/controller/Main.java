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
        while (true) {
            view.write("Enter command (or command 'help' for help): ");
            String input = view.read();
            if (input.equals("connect")){
                doConnect();
            }else {
                for (Command command: commands) {
                    if (command.canProcess(input)){
                        command.process(input);
                        break;
                    }
                }
            }
        }

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



