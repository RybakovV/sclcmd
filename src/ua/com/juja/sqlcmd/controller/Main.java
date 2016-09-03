package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.Console;
import ua.com.juja.sqlcmd.viuw.View;


public class Main {
    public static void main(String[] args) {
        View view = new Console();
        //DatabaseManager manager = new PostgresqlDatabaseManager();
        DatabaseManager manager = new MysqlDatabaseManager();
        MainController mainController = new MainController(view, manager);
        mainController.run();
    }
}
