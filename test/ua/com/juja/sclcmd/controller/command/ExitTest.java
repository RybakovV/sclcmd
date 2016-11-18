package ua.com.juja.sclcmd.controller.command;

import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.controller.command.ExitException;

import static junit.framework.TestCase.*;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
public class ExitTest {

    private FakeView view = new FakeView();

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
        assertEquals("See you soon!!!\n", view.getContent());
    }

}
