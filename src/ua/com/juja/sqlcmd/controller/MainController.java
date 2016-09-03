package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.Console;
import ua.com.juja.sqlcmd.viuw.View;


public class MainController {


    private View view;
    private DatabaseManager manager;

    MainController(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
    }

    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new PostgresqlDatabaseManager();
    }


    void run(){
        connectToDb();


    }

    private void connectToDb() {
        view.write("Hello");
        while (true){
            view.write("Enter Database name: ");
            String databaseName = view.read();
            //manager.setDatabaseName(databaseName);

            view.write("Enter userName");
            String userName = view.read();
            //manager.setUserName(userName);

            view.write("Enter password");
            String userPassword = view.read();
            //manager.setUserPassword(userPassword);

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
