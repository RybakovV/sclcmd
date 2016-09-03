package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.Console;
import ua.com.juja.sqlcmd.viuw.View;

import java.util.Arrays;


class MainController {


    private View view;
    private DatabaseManager manager;

    MainController(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
    }

    void run(){
        connectToDb();

        label:
        while (true) {
            view.write("Enter command (or command 'help' for help): ");
            String command = view.read();
            switch (command) {
                case "list":
                    doList();
                    break;
                case "help":
                    doHelp();
                    break;
                case "connect":
                    connectToDb();
                    break;
                case "exit":
                    break label;
                default:
                    view.write("non-existent command: " + command);
                    break;
            }

        }
    }

    private void doList(){
        String[] tables = manager.getAllTablesOfDataBase();
        view.write(Arrays.toString(tables));
    }

    private void doHelp(){
        view.write("Possible commands:");
        view.write("\tlist");
        view.write("\t\tprint all tables of the connected database");
        view.write("\texit");
        view.write("\t\tto exit from the program");
        view.write("\tconnect");
        view.write("\t\tto connect to another database");
        view.write("\thelp");
        view.write("\t\tprint possible commands");

    }

    private void connectToDb() {
        view.write("Hello");
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
                    manager.connectToDataBase(databaseName, userName, userPassword);
                }
                if (manager.getVersionDatabase().equals(("PostgreSQL"))){
                    manager = new PostgresqlDatabaseManager();
                    manager.connectToDataBase(databaseName, userName, userPassword);
                }
                break;
            }catch (Exception e){
                String message = e.getMessage();
                if (e.getCause() != null){
                    message += " " + e.getCause().getMessage();
                }
                view.write("Yuo do't connected to database. Because: " + message);
                view.write("Try again");
            }
        }
        view.write("You connected to " + manager.getVersionDatabase() + " database" );
    }

}
