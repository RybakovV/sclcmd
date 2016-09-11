package ua.com.juja.sclcmd.integration;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MEBELBOS-2 on 08.09.2016.
 */
public class ConfigurableInputStream extends InputStream{
    private String line;
    private boolean endLine = false;

    @Override
    public int read() throws IOException {
        if (line.length() == 0){
            return  -1;
        }

        if (endLine) {
            endLine = false;
            return -1;
        }

        char ch = line.charAt(0);
        line = line.substring(1);

        if (ch == '\n'){
            endLine = true;
        }
        return (int)ch;
    }

    public void add(String line){
        if (this.line == null){
            this.line = line;
        }else{
            this.line += "\n" + line;
        }
    }
}
