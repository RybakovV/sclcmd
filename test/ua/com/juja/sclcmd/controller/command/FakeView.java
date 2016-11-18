package ua.com.juja.sclcmd.controller.command;

import ua.com.juja.sqlcmd.viuw.View;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
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
