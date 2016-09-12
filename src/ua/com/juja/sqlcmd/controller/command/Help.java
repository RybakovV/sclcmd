package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class Help implements Command {

    private View view;

    public Help(View view){ this.view = view; }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Possible commands:");

        view.write("\tconnect");
        view.write("\t\tfor connect to database");

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

        view.write("\thelp");
        view.write("\t\tprint possible commands");
    }
}
