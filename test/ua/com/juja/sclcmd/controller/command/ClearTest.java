package ua.com.juja.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.Clear;
import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Print;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
public class ClearTest {

    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Clear(view, manager);

    }

    @Test
    public void clearTableTest(){
        //when
        try {
            command.process("clear");
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("incorrect number of parameters. Expected 1, but is 0",e.getMessage());
        }
        //then
    }

    @Test
    public void clearTableTestWithoutParameters(){
        //given
        Mockito.when(manager.clear("user")).thenReturn("The table 'user' cleared");
        //when
        command.process("clear user");
        //then
        shouldPrint("[The table 'user' cleared]");
    }


    @Test
    public void clearNotExistingTable(){
        //given
        Mockito.when(manager.clear("notexisting")).thenReturn("Table 'notexisting' does not existing");
        //when
        command.process("clear notexisting");
        //then
        shouldPrint("[Table 'notexisting' does not existing]");
    }

    @Test
    public void testCanProcessClearString(){
        //when
        boolean canProcess = command.canProcess("clear test");
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

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

}
