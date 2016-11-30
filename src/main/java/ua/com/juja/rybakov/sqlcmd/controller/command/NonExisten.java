package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class NonExisten implements Command {
    private View view;

    public NonExisten(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("non-existent command: " + command);
    }
}
