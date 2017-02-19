package ua.com.juja.rybakov.sqlcmd.controller;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

/**
 * Created by Vitaliy Ryvakov on 15.02.2017.
 */
public class SignReader implements Sign {
    private String databaseName;
    private String userName;
    private String userPassword;

    public SignReader(String databaseName, String userName, String userPassword) {
        this.databaseName = databaseName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public SignReader() {

    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    public SignReader read(View view) {
        view.write("Enter Database name: ");
        databaseName = view.read();

        view.write("Enter userName");
        userName = view.read();

        view.write("Enter password");
        userPassword = view.read();

        return this;
    }
}
