package ua.com.juja.rybakov.sclcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;

import ua.com.juja.rybakov.sqlcmd.controller.command.Command;
import ua.com.juja.rybakov.sqlcmd.controller.command.Exit;
import ua.com.juja.rybakov.sqlcmd.controller.command.ExitException;
import ua.com.juja.rybakov.sqlcmd.viuw.View;


import static junit.framework.TestCase.*;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
public class ExitWithMockitoTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessExitString(){
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweCommand(){
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessExitCommand_throwExitException(){
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        }catch (ExitException e){
            // do nothing
        }

        //then
        Mockito.verify(view).write("See you soon!!!");
    }

}
