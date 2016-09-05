package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;


class MainController {


    private View view;
    private DatabaseManager manager;
    private Command[] commands;

    MainController(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{
                new Exit(view),
                new Help(view),
                new List(view, manager),
                new Print(view, manager),
                new Insert(view, manager),
                new Edit(view, manager),
                new Connect(view, manager),
                new NonExisten(view)};
    }

    void run(){
        while (true) {
            view.write("Enter command (or command 'help' for help): ");
            String input = view.read();
            for (Command command : commands) {
                if (command.canProcess(input)){
                    command.process(input);
                    break;
                }
            }
        }
    }
}
