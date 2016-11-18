package ua.com.juja.sclcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Print;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vitaliy Ryvakov on 18.11.2016.
 */
public class PrintTest {

    private View view;
    private DatabaseManager manager;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);

    }

    @Test
    public void test(){
        //given
        Command command = new Print(view, manager);
        Mockito.when(manager.getTableString("test")).
                thenReturn(new String(
                        "╔═════════════════════════════════════════╗\n" +
                        "║ Table 'test' is empty or does not exist ║\n" +
                        "╚═════════════════════════════════════════╝\n" ));
        //when
        command.process("print test");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[╔═════════════════════════════════════════╗\n" +
                      "║ Table 'test' is empty or does not exist ║\n" +
                      "╚═════════════════════════════════════════╝\n]",
                captor.getAllValues().toString());
    }
}
