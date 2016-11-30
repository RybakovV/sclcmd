package ua.com.juja.rybakov.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;
import ua.com.juja.rybakov.sqlcmd.controller.command.Command;
import ua.com.juja.rybakov.sqlcmd.controller.command.Print;
import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;


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
        DataSet user1 = new DataSet();
        user1.put("id", 1);
        user1.put("name", "Stiven Pupkin");
        user1.put("password", "123456");
        DataSet user2 = new DataSet();
        user2.put("id", 2);
        user2.put("name", "Eva Pupkina");
        user2.put("password", "789456");
        DataSet[] dataSets = new DataSet[]{user1, user2};
        Mockito.when(manager.getTableData("users")).thenReturn(dataSets);
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

    @Test
    public void printTableWithOneColumn() {
        //given
        DataSet user1 = new DataSet();
        user1.put("id", 1);
        DataSet[] dataSets = new DataSet[]{user1};
        Mockito.when(manager.getTableData("test")).thenReturn(dataSets);
        //when
        command.process("print test");
        //then
        shouldPrint("[╔════╗\n" +
                     "║ id ║\n" +
                     "╠════╣\n" +
                     "║ 1  ║\n" +
                     "╚════╝\n" + "]");
    }


    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void PrintNotExistingTable() {
        //given
        Mockito.when(manager.getTableData("test")).thenReturn(new DataSet[0]);
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
