package ua.com.juja.rybakov.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;
import ua.com.juja.rybakov.sqlcmd.controller.command.Command;
import ua.com.juja.rybakov.sqlcmd.controller.command.Print;
import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class PrintTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Print(view, manager);
    }

    @Test
    public void incorrectNumberOfParameters() {

    }

    @Test
    public void printTableTest() {
        //given

        setupTable("users",
                user(1, "Stiven Pupkin", "123456"),
                user(2, "Eva Pupkina", "789456"));

        //when
        command.process("print users");

        //then
        shouldPrint("[╔════════════════╦════════════════╦════════════════╗\n" +
                     "║       id       ║      name      ║    password    ║\n" +
                     "╠════════════════╬════════════════╬════════════════╣\n" +
                     "║       1        ║ Stiven Pupkin  ║     123456     ║\n" +
                     "╠════════════════╬════════════════╬════════════════╣\n" +
                     "║       2        ║  Eva Pupkina   ║     789456     ║\n" +
                     "╚════════════════╩════════════════╩════════════════╝\n" + "]");
    }

    private void setupTable(String tableName, DataSet... users) {
        List<DataSet> dataSets = new LinkedList<>();
        dataSets.addAll(Arrays.asList(users));
        Mockito.when(manager.getTableData(tableName)).thenReturn(dataSets);
    }

    private DataSet user(int id, String name, String password) {
        DataSet user = putId(id);
        user.put("name", name);
        user.put("password", password);
        return user;
    }

    @Test
    public void printTableWithOneColumn() {
        //given
        DataSet id = putId(1);

        setupTable("test", id);

        //when
        command.process("print test");
        //then
        shouldPrint("[╔════╗\n" +
                     "║ id ║\n" +
                     "╠════╣\n" +
                     "║ 1  ║\n" +
                     "╚════╝\n" + "]");
    }

    private DataSet putId(int value) {
        DataSet user1 = new DataSetImpl();
        user1.put("id", value);
        return user1;
    }


    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void PrintNotExistingTable() {
        //given
        Mockito.when(manager.getTableData("test")).thenReturn(new LinkedList<DataSet>());
        //when
        command.process("print test");
        //then
        shouldPrint("[╔═════════════════════════════════════════╗\n" +
                     "║ Table 'test' is empty or does not exist ║\n" +
                     "╚═════════════════════════════════════════╝]");
    }

    @Test
    public void testCanProcessPrintString() {
        //when
        boolean canProcess = command.canProcess("print test");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweCommand() {
        //when
        boolean canProcess = command.canProcess("qwe");
        //then
        assertFalse(canProcess);
    }
}
