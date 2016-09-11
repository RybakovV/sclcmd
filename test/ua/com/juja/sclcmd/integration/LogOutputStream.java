package ua.com.juja.sclcmd.integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by MEBELBOS-2 on 08.09.2016.
 */
public class LogOutputStream extends OutputStream {

    private String log = "";

    @Override
    public void write(int b) throws IOException {
        log += String.valueOf((char)b);
    }

    public String getData(){
        return log;
    }
}
