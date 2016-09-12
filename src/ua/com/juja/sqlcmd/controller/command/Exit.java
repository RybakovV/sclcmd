package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class Exit implements Command {

    private View view;

    public Exit(View view){
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("See you soon!!!");
        throw new ExitException();

    }
}