package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

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

        view.write("\tlistdb");
        view.write("\t\tprint all databases of the type connected database");

        view.write("\tprint tableName");
        view.write("\t\tprint contents of the table 'tableName'");

        view.write("\tinsert tableName");
        view.write("\t\tinsert data to the table 'tableName'");

        view.write("\tupdate tableName");
        view.write("\t\tupdate data of the table 'tableName'");

        view.write("\tdelete tableName");
        view.write("\t\tdelete data from the table 'tableName'");

        view.write("\tclear tableName");
        view.write("\t\tclear data of the table 'tableName'");

        view.write("\texit");
        view.write("\t\tto exit from the program");

        view.write("\thelp");
        view.write("\t\tprint possible commands");
    }
}
