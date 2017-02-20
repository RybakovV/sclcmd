package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.controller.command.*;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.PostgreSqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.Console;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class Main {
    public static void main(String[] args) {
        new Controller(new Console()).run();
    }
}



