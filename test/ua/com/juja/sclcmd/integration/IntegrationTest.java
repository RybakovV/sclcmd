package ua.com.juja.sclcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.model.PostgresqlDatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private DatabaseManager manager;


    @Before
    public void setup(){
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        manager = new MysqlDatabaseManager();
        manager.connectToDataBase("mysqlcmd", "root", "root");
        String tableName = "user";
        manager.clear(tableName);
        DataSet data = new DataSet();
        data.put("id", 13);
        data.put("password", "pswd");
        data.put("name", "Pupkin");
        manager.insert(tableName, data);

        manager = new PostgresqlDatabaseManager();
        manager.connectToDataBase("pgsqlcmd", "postgres", "postgres");
        tableName = "user";
        manager.clear(tableName);
        manager.insert(tableName, data);

    }

    @Test
    public void testInputNullFail() {
        in.add("");

    }
    @Test
    public void testInsertFail(){
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("insert");
        in.add("insert users");
        in.add("insert user");
        in.add("19");
        in.add("MYSQL");
        in.add("PASSWORD");
        in.add("print user");

        in.add("connect");
        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("insert");
        in.add("insert users");
        in.add("insert user");
        in.add("22");
        in.add("POSTGRES");
        in.add("PASSWORD");
        in.add("print user");

        in.add("exit");
        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //inser user
                "Enter the data when you want to insert.\n" +
                "Input id:\n" +
                //19
                "Input name:\n" +
                //MYSQL
                "Input password:\n" +
                //PASSWORD
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    19    ║  MYSQL   ║ PASSWORD ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You connected to PostgreSQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //inser user
                "Enter the data when you want to insert.\n" +
                "Input id:\n" +
                //22
                "Input name:\n" +
                //POSTGRES
                "Input password:\n" +
                //PASSWORD
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    22    ║ POSTGRES ║ PASSWORD ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(actusal, expected);


    }

    @Test
    public void testEditNotExistTable(){
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("edit");
        in.add("edit users");
        in.add("exit");
        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                "See you soon!!!\n";
        assertEquals(expected, actusal);


    }

    @Test
    public void testExit(){
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("print test-sql");
        in.add("exit");
        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //print test-sql
                "╔════════════╦════════════╗\n" +
                "║    ids     ║ first-name ║\n" +
                "╚════════════╩════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }

    @Test
    public void testNoTableExit(){
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("print test");
        in.add("exit");
        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                "Enter userName\n" +
                "Enter password\n" +
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                "╔═════════════════════════════════════════╗\n" +
                "║ Table 'test' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }


    @Test
    public void testAll(){
        in.add("list");
        in.add("helps");
        in.add("help");
        in.add("connect");
        in.add("mycmd");
        in.add("root");
        in.add("root");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("list");
        in.add("print user");
        in.add("print test-sql");
        in.add("print empty");
        in.add("insert");
        in.add("insert users");
        in.add("insert user");
        in.add("18");
        in.add("MYSQL");
        in.add("PASSWORD");
        in.add("print user");
        in.add("edit users");
        in.add("edit user");
        in.add("18");
        in.add("18");
        in.add("Pukin Stiven");
        in.add("Pasword");
        in.add("print user");
        in.add("connect");
        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("list");
        in.add("print USERS");
        in.add("print super user");
        in.add("print user");
        in.add("print test");
        in.add("print empty");
        in.add("insert");
        in.add("insert users");
        in.add("insert user");
        in.add("18");
        in.add("POSTGRES");
        in.add("PASSWORD");
        in.add("print user");
        in.add("edit users");
        in.add("edit user");
        in.add("18");
        in.add("18");
        in.add("Pukin Stiven in pgsqlcmd");
        in.add("Pasword");
        in.add("print user");

        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("list sqlcmd");
        in.add("list");
        in.add("print user");

        in.add("exit");


        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //list
                "You must connected to database with command 'connected'\n" +
                "Enter command (or command 'help' for help): \n" +
                //helps
                "You must connected to database with command 'connected'\n" +
                "Enter command (or command 'help' for help): \n"+
                //help
                "Possible commands:\n" +
                "\tconnect\n" +
                "\t\tfor connect to database\n" +
                "\tlist\n" +
                "\t\tprint all tables of the connected database\n" +
                "\tprint tableName\n" +
                "\t\tprint contents of the table 'tableName'\n" +
                "\tinsert tableName\n" +
                "\t\tinsert data to the table 'tableName'\n" +
                "\tupdate tableName\n" +
                "\t\tupdate data of the table 'tableName'\n" +
                "\texit\n" +
                "\t\tto exit from the program\n" +
                "\thelp\n" +
                "\t\tprint possible commands\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mycmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You do not connected to database. Because: Can't connect to Database: mycmd by User: root or Password: root.  FATAL: password authentication failed for user \"root\"\n" +
                "Try again\n"+
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //list
                "[empty, test-sql, user]\n" +
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //print test-sql
                "╔════════════╦════════════╗\n" +
                "║    ids     ║ first-name ║\n" +
                "╚════════════╩════════════╝\n" +
                "\n"+
                "Enter command (or command 'help' for help): \n" +
                //print empty
                "╔════╗\n" +
                "║ id ║\n" +
                "╚════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //inser user
                "Enter the data when you want to insert.\n" +
                "Input id:\n" +
                //18
                "Input name:\n" +
                //MYSQL
                "Input password:\n" +
                //PASSWORD
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    18    ║  MYSQL   ║ PASSWORD ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n"+
                "Enter command (or command 'help' for help): \n" +
                //edit user
                "Enter the data when you want to change (edit).\n" +
                "Input id:\n" +
                //18
                "Input name:\n" +
                //Pupkin Stiven in pgsqlcmd
                "Input password:\n" +
                //Pasword
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════════╦══════════════╦══════════════╗\n" +
                "║      id      ║     name     ║   password   ║\n" +
                "╠══════════════╬══════════════╬══════════════╣\n" +
                "║      13      ║    Pupkin    ║     pswd     ║\n" +
                "╠══════════════╬══════════════╬══════════════╣\n" +
                "║      18      ║ Pukin Stiven ║   Pasword    ║\n" +
                "╚══════════════╩══════════════╩══════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You connected to PostgreSQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //list
                "[empty, test, user]\n" +
                "Enter command (or command 'help' for help): \n" +
                //print USERS
                "╔══════════════════════════════════════════╗\n" +
                "║ Table 'USERS' is empty or does not exist ║\n" +
                "╚══════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                //print super user
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 2\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //print test
                "╔════════════╦════════════╗\n" +
                "║    idp     ║ first_name ║\n" +
                "╚════════════╩════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //print empty
                "╔══════════════════════════════════════════╗\n" +
                "║ Table 'empty' is empty or does not exist ║\n" +
                "╚══════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //insert users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //inser user
                "Enter the data when you want to insert.\n" +
                "Input id:\n" +
                //22
                "Input name:\n" +
                //POSTGRES
                "Input password:\n" +
                //PASSWORD
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    18    ║ POSTGRES ║ PASSWORD ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit users
                "Command failed. Because: Table 'users' doesn't exist\n" +
                "Try again\n"+
                "Enter command (or command 'help' for help): \n" +
                //edit user
                "Enter the data when you want to change (edit).\n" +
                "Input id:\n" +
                //18
                "Input name:\n" +
                //Pupkin Stiven in pgsqlcmd
                "Input password:\n" +
                //Pasword
                "Enter command (or command 'help' for help): \n" +
                "╔══════════════════════════╦══════════════════════════╦══════════════════════════╗\n" +
                "║            id            ║           name           ║         password         ║\n" +
                "╠══════════════════════════╬══════════════════════════╬══════════════════════════╣\n" +
                "║            13            ║          Pupkin          ║           pswd           ║\n" +
                "╠══════════════════════════╬══════════════════════════╬══════════════════════════╣\n" +
                "║            18            ║ Pukin Stiven in pgsqlcmd ║         Pasword          ║\n" +
                "╚══════════════════════════╩══════════════════════════╩══════════════════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //list sqlcmd
                "non-existent command: list sqlcmd\n"+
                "Enter command (or command 'help' for help): \n" +
                //list
                "[empty, test-sql, user]\n"+
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔══════════════╦══════════════╦══════════════╗\n" +
                "║      id      ║     name     ║   password   ║\n" +
                "╠══════════════╬══════════════╬══════════════╣\n" +
                "║      13      ║    Pupkin    ║     pswd     ║\n" +
                "╠══════════════╬══════════════╬══════════════╣\n" +
                "║      18      ║ Pukin Stiven ║   Pasword    ║\n" +
                "╚══════════════╩══════════════╩══════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8").replace("\r\n","\n");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
