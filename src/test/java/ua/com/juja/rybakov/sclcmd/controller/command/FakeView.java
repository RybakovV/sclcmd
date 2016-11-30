package ua.com.juja.rybakov.sclcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.viuw.View;

public class FakeView implements View {

    private String messages = "";
    @Override
    public void write(String message) {
        messages += message + "\n";
    }

    @Override
    public String read() {
        return null;
    }

    public String getContent() {
        return messages;
    }
}
