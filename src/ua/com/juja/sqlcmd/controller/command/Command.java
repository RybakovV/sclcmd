package ua.com.juja.sqlcmd.controller.command;

/**
 * Created by MEBELBOS-2 on 04.09.2016.
 */
public interface Command {
    boolean canProcess(String command);
    void process(String command);


}
