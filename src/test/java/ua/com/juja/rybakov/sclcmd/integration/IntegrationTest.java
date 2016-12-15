package ua.com.juja.rybakov.sclcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.rybakov.sqlcmd.controller.Main;
import ua.com.juja.rybakov.sqlcmd.model.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;

public class IntegrationTest {
    private static final String SERVICE_NAME_POSTGRES = "PostgreSQL 9.5 Server";
    private static final String SERVICE_NAME_MYSQL = "MySQL57";
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private DatabaseManager manager;


    @Before
    public void setup() throws SQLException {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        String tableName = "user";
        DataSet data = new DataSetImpl();
        data.put("id", 13);
        data.put("password", "pswd");
        data.put("name", "Pupkin");
        try {
            manager = new MysqlDatabaseManager();
            manager.connectToDataBase("mysqlcmd", "root", "root");
            manager.clear(tableName);
            manager.insert(tableName, data);

            manager = new PostgresqlDatabaseManager();
            manager.connectToDataBase("pgsqlcmd", "postgres", "postgres");
            tableName = "user";
            manager.clear(tableName);
            manager.insert(tableName, data);

        } catch (RuntimeException e) {
            //do noting
        }
    }

    @Test
    public void testInputNullFail() {
        in.add("");
    }

    @Test
    public void testListDatabases() {
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("listdb");

        in.add("connect");
        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");
        in.add("listdb");

        in.add("exit");

        Main.main(new String[0]);
        String actual = getData();
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
                //listdb
                "[information_schema, kitchenkonstructor, mysql, mysqlcmd, performance_schema, sakila, sys, world]\n"+
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
                //listdb
                "[pgsqlcmd, postgres, test]\n"+
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actual);

    }


    @Test
    public void testConnect() {
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");

        in.add("connect");
        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");

        in.add("exit");

        Main.main(new String[0]);
        String actual = getData();
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
                //connect
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You connected to PostgreSQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actual);

    }

    @Test
    public void testConnectToStoppedDB() {
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");

        in.add("mysqlcmd");
        in.add("root");
        in.add("root");

        in.add("mysqlcmd");
        in.add("root");
        in.add("root");

        in.add("connect");
        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");

        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");

        in.add("pgsqlcmd");
        in.add("postgres");
        in.add("postgres");

        in.add("exit");


        ServiceStartStop.main(new String[]{"stop", SERVICE_NAME_POSTGRES});
        ServiceStartStop.main(new String[]{"stop", SERVICE_NAME_MYSQL});

        Main.main(new String[0]);

        ServiceStartStop.main(new String[]{"start", SERVICE_NAME_POSTGRES});
        ServiceStartStop.main(new String[]{"start", SERVICE_NAME_MYSQL});


        String actual = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You can't connect to the database. Because: Can't connect to Database: mysqlcmd by User: root or Password: root.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения.\n" +
                "Try again\n" +
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You can't connect to the database. Because: Can't connect to Database: mysqlcmd by User: root or Password: root.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения.\n" +
                "Try again\n" +
                "Enter Database name: \n" +
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You can't connect to the database. Because: Can't connect to Database: mysqlcmd by User: root or Password: root.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения." +
                "\n" +
                "Enough try\n" +
                "Enter command (or command 'help' for help): \n" +
                //connect
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You can't connect to the database. Because: Can't connect to Database: pgsqlcmd by User: postgres or Password: postgres.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения.\n" +
                "Try again\n" +
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You can't connect to the database. Because: Can't connect to Database: pgsqlcmd by User: postgres or Password: postgres.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения.\n" +
                "Try again\n" +
                "Enter Database name: \n" +
                //pgsqlcmd
                "Enter userName\n" +
                //postgres
                "Enter password\n" +
                //postgres
                "You can't connect to the database. Because: Can't connect to Database: pgsqlcmd by User: postgres or Password: postgres.  Подсоединение по адресу localhost:5432 отклонено. Проверьте что хост и порт указаны правильно и что postmaster принимает TCP/IP-подсоединения.\n" +

                "Enough try\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testInsert() {
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");

        in.add("insert");
        in.add("insert users");

        in.add("insert user");
        in.add("qwe");
        in.add("qwe");
        in.add("qwe");

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
        in.add("qwe");
        in.add("qwe");
        in.add("qwe");

        in.add("insert user");
        in.add("22");
        in.add("POSTGRES");
        in.add("PASSWORD");
        in.add("print user");

        in.add("exit");
        Main.main(new String[0]);
        String actual = getData();
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
                //qwe
                "Input name:\n" +
                //qwe
                "Input password:\n" +
                //qwe
                "Command failed. Because:  Incorrect integer value: 'qwe' for column 'id' at row 1\n" +
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
                //qwe
                "Input name:\n" +
                //qwe
                "Input password:\n" +
                //qwe
                "Command failed. Because:  ERROR: invalid input syntax for integer: \"qwe\"\n" +
                "  Позиция: 53\n" +
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
        assertEquals(expected, actual);
    }

    @Test
    public void testEdit() {
        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("print user");

        in.add("edit user");
        in.add("13");
        in.add("13");
        in.add("Pupkin Stiven in mysqlcmd");
        in.add("passwordinmysqlcmd");
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
                //print user
                "╔══════════╦══════════╦══════════╗\n" +
                "║    id    ║   name   ║ password ║\n" +
                "╠══════════╬══════════╬══════════╣\n" +
                "║    13    ║  Pupkin  ║   pswd   ║\n" +
                "╚══════════╩══════════╩══════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit user
                "Enter 'id' row when you want to change (edit): \n" +
                //13
                "Input new id:\n" +
                //13
                "Input new name:\n" +
                //Pupkin Stiven in pgsqlcmd
                "Input new password:\n" +
                //Pasword
                //TODO "Data edited\n" +
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔════════════════════════════╦════════════════════════════╦════════════════════════════╗\n" +
                "║             id             ║            name            ║          password          ║\n" +
                "╠════════════════════════════╬════════════════════════════╬════════════════════════════╣\n" +
                "║             13             ║ Pupkin Stiven in mysqlcmd  ║     passwordinmysqlcmd     ║\n" +
                "╚════════════════════════════╩════════════════════════════╩════════════════════════════╝\n" +
                "\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }

    @Test
    public void testEditNotExistTable() {
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
    public void testExit() {
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
                "╔═════════════════════════════════════════════╗\n" +
                "║ Table 'test-sql' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }

    @Test
    public void testPrintNotExistTable() {
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
                //mysqlcmd
                "Enter userName\n" +
                //root
                "Enter password\n" +
                //root
                "You connected to MySQL database\n" +
                "Enter command (or command 'help' for help): \n" +
                //print test
                "╔═════════════════════════════════════════╗\n" +
                "║ Table 'test' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                //print exit
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }


    @Test
    public void testAll() {
        in.add("cleare");
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

        in.add("clear");
        in.add("clear user");
        in.add("y");

        in.add("connect");
        in.add("mysqlcmd");
        in.add("root");
        in.add("root");
        in.add("list sqlcmd");
        in.add("list");
        in.add("print user");
        in.add("clear");
        in.add("clear qwe");
        in.add("т");
        in.add("Т");
        in.add("Н");
        in.add("н");
        in.add("clear user");
        in.add("н");
        in.add("y");
        in.add("print user");

        in.add("exit");


        Main.main(new String[0]);
        String actusal = getData();
        String expected = "Hello\n" +
                "Enter command (or command 'help' for help): \n" +
                //cleare
                "You must connected to database with command 'connect'\n" +
                "Enter command (or command 'help' for help): \n" +
                //list
                "You must connected to database with command 'connect'\n" +
                "Enter command (or command 'help' for help): \n" +
                //helps
                "You must connected to database with command 'connect'\n" +
                "Enter command (or command 'help' for help): \n" +
                //help
                "Possible commands:\n" +
                "\tconnect\n" +
                "\t\tfor connect to database\n" +
                "\tlist\n" +
                "\t\tprint all tables of the connected database\n" +
                "\tlistdb\n" +
                "\t\tprint all databases of the type connected database\n"+
                "\tprint tableName\n" +
                "\t\tprint contents of the table 'tableName'\n" +
                "\tinsert tableName\n" +
                "\t\tinsert data to the table 'tableName'\n" +
                "\tupdate tableName\n" +
                "\t\tupdate data of the table 'tableName'\n" +
                "\tclear tableName\n" +
                "\t\tclear data of the table 'tableName'\n" +
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
                "You can't connect to the database. Because: Can't connect to Database: mycmd by User: root or Password: root.  FATAL: password authentication failed for user \"root\"\n" +
                "Try again\n" +
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
                "╔═════════════════════════════════════════════╗\n" +
                "║ Table 'test-sql' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════════╝\n" +
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
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit user
                "Enter 'id' row when you want to change (edit): \n" +
                "Input new id:\n" +
                //18
                "Input new name:\n" +
                //Pupkin Stiven in pgsqlcmd
                "Input new password:\n" +
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
                "╔═════════════════════════════════════════╗\n" +
                "║ Table 'test' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════╝\n" +
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
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //edit user
                "Enter 'id' row when you want to change (edit): \n" +
                "Input new id:\n" +
                //18
                "Input new name:\n" +
                //Pupkin Stiven in pgsqlcmd
                "Input new password:\n" +
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
                //clear
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //clear user
                "All data will be deleted from the table. Do you really want to do it? (Y/N):\n"+
                //y
                "The table 'user' is cleared.\n" +
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
                "non-existent command: list sqlcmd\n" +
                "Enter command (or command 'help' for help): \n" +
                //list
                "[empty, test-sql, user]\n" +
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
                //clear
                "Command failed. Because: incorrect number of parameters. Expected 1, but is 0\n" +
                "Try again\n" +
                "Enter command (or command 'help' for help): \n" +
                //clear qwe
                "All data will be deleted from the table. Do you really want to do it? (Y/N):\n"+
                //т
                "Enter Y (if yes) or N (if no):\n" +
                //Т
                "Enter Y (if yes) or N (if no):\n" +
                //Н
                "Enter Y (if yes) or N (if no):\n" +
                //н
                "The table 'qwe' is not cleared. Because: Too many attempts\n"+
                "Enter command (or command 'help' for help): \n" +
                //clear user
                "All data will be deleted from the table. Do you really want to do it? (Y/N):\n"+
                //н
                "Enter Y (if yes) or N (if no):\n" +
                //y
                "The table 'user' is cleared.\n" +
                "Enter command (or command 'help' for help): \n" +
                //print user
                "╔═════════════════════════════════════════╗\n" +
                "║ Table 'user' is empty or does not exist ║\n" +
                "╚═════════════════════════════════════════╝\n" +
                "Enter command (or command 'help' for help): \n" +
                //exit
                "See you soon!!!\n";
        assertEquals(expected, actusal);
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8").replace("\r\n", "\n");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
