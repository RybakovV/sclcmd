package ua.com.juja.sqlcmd.controller.command;

import sun.nio.ch.FileChannelImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;



/**
 * Created by MEBELBOS-2 on 04.09.2016.
 */
public class Connect implements Command {
    private View view;
    private DatabaseManager manager;


    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("connect");
    }

    @Override
    public void process(String command) {
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
                view.write("You do not connected to database. Because: " + message);
                view.write("Try again");
            }
        }
        view.write("You connected to " + manager.getVersionDatabase() + " database" );
    }
}
