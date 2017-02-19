package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

/**
 * Created by Vitaliy Ryvakov on 15.02.2017.
 */
public class Sign {
    private String databaseName;
    private String userName;
    private String userPassword;

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Sign invoke(View view) {
        view.write("Enter Database name: ");
        databaseName = view.read();

        view.write("Enter userName");
        userName = view.read();

        view.write("Enter password");
        userPassword = view.read();
        return this;
    }
}
