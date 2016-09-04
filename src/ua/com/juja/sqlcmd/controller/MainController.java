package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import java.util.Arrays;


class MainController {


    private View view;
    private DatabaseManager manager;
    private Command[] commands;

    MainController(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{new Exit(view)};


    }

    void run(){
        connectToDb();


        while (true) {
            view.write("Enter command (or command 'help' for help): ");
            String[] command = view.read().split(" ");
            if (commands[0].canProcess(command[0])){
                commands[0].process(command[0]);
            }

            switch (command[0]) {
                case "help":
                    doHelp();
                    break;

                case "list":
                    doList();
                    break;

                case "print":
                    doPrint(command);
                    break;

                case "insert":
                    doInsert(command);
                    break;

                case "edit":
                    doEdit(command);
                    break;

                case "connect":
                    connectToDb();
                    break;

                case "exit":
                    view.write("See you soon");
                    System.exit(0);

                default:
                    view.write("non-existent command: " + Arrays.toString(command));
                    break;
            }

        }
    }

    private void doEdit(String[] command) {
        try{
            if (command.length != 2){
                throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
            }
            String tableName = command[1];
            view.write("Enter the data when you want to change (edit):");
            String[] columnName = manager.getColumnNames(tableName);
            DataSet insertData = new DataSet();
            int dataChange = Integer.parseInt(view.read());
            for (String aColumnName : columnName) {
                view.write("Input " + aColumnName + ":");
                Object value = view.read();
                insertData.put(aColumnName, value);
            }
            manager.update(tableName, dataChange, insertData);
        }catch (Exception e){
            printError(e);
        }
    }

    private void doInsert(String[] command) {
        try {
            if (command.length != 2){
                throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
            }
            String tableName = command[1];
            view.write("Enter the data when you want to insert:");
            String[] columnName = manager.getColumnNames(tableName);
            DataSet insertData = new DataSet();
            for (String aColumnName : columnName) {
                view.write("Input " + aColumnName + ":");
                Object value = view.read();
                insertData.put(aColumnName, value);
            }
            manager.insert(tableName, insertData);
        }catch (Exception e){
            printError(e);
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null){
            message += " " + e.getCause().getMessage();
        }
        view.write("Command failed. Because: " + message);
        view.write("Try again");
    }

    private void doPrint(String[] command){
        try {
            if (command.length != 2) {
                throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
            }
            String tableName = command[1];
            view.write(manager.getTableString(tableName));
        }catch (Exception e){
            printError(e);
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

        view.write("\tprint tableName");
        view.write("\t\tprint contents of the table 'tableName'");

        view.write("\tinsert tableName");
        view.write("\t\tinsert data to the table 'tableName'");

        view.write("\tupdate tableName");
        view.write("\t\tupdate data of the table 'tableName'");

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
                view.write("Yuo do not connected to database. Because: " + message);
                view.write("Try again");
            }
        }
        view.write("You connected to " + manager.getVersionDatabase() + " database" );
    }

}
