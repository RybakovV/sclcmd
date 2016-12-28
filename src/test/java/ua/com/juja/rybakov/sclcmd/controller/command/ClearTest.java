package ua.com.juja.rybakov.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.com.juja.rybakov.sqlcmd.controller.command.Clear;
import ua.com.juja.rybakov.sqlcmd.controller.command.Command;
import ua.com.juja.rybakov.sqlcmd.controller.command.Print;
import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.MysqlDatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.sql.SQLException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

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
    public void clearTableTestWithoutParameter(){
        //when
        try {
            command.process("clear");
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("incorrect number of parameters. Expected 1, but is 0",e.getMessage());
        }
        //then
    }

    //TODO mockito c подтверждением
/*
    @Test
    public void clearTableTest() throws SQLException {
        //when
        command.process("clear user");
        //then
        Mockito.verify(manager).clear("user");
        Mockito.verify(view).write("The table 'user' cleared");
    }
*/


/*
    @Test
    public void clearNotExistingTable(){
        //given
        command.process("clear notexisting");
        //then
        String expected = "The table 'notexisting' ";
        try {
            Mockito.verify(manager).clear("notexisting");
            expected += "cleared";
        } catch (SQLException e) {
            expected += "' not cleared. Because: " + e.getMessage();
        }
        Mockito.verify(view).write(expected);
    }
*/

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
}
