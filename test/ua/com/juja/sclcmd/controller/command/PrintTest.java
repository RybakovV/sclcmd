package ua.com.juja.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Print;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
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
    public void PrintTable(){
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
        Mockito.when(manager.getTableData("users"))
                .thenReturn(dataSets);
        //when
        command.process("print users");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[╔════════════════╦════════════════╦════════════════╗\n" +
                      "║       id       ║      name      ║    password    ║\n" +
                      "╠════════════════╬════════════════╬════════════════╣\n" +
                      "║       1        ║ Stiven Pupkin  ║     123456     ║\n" +
                      "╠════════════════╬════════════════╬════════════════╣\n" +
                      "║       2        ║  Eva Pupkina   ║     789456     ║\n" +
                      "╚════════════════╩════════════════╩════════════════╝\n" + "]",
                captor.getAllValues().toString());
    }

    @Test
    public void PrintNotExistingTable(){
        //given
        DataSet[] test = new DataSet[0];
        Mockito.when(manager.getTableData("test")).
                thenReturn(test);
        //when
        command.process("print test");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[╔═════════════════════════════════════════╗\n" +
                      "║ Table 'test' is empty or does not exist ║\n" +
                      "╚═════════════════════════════════════════╝]",
                captor.getAllValues().toString());
    }



    @Test
    public void testCanProcessPrintString(){
        //when
        boolean canProcess = command.canProcess("print test");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweCommand(){
        //when
        boolean canProcess = command.canProcess("qwe");
        //then
        assertFalse(canProcess);
    }

}
