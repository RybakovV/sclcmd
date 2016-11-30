package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class Exit implements Command {
    private View view;

    public Exit(View view) {
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