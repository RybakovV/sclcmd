package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.viuw.View;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class Print implements Command {

    private View view;
    private DatabaseManager manager;
    private String tableName;

    public Print(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("print ");
    }

    @Override
    public void process(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length-1));
        }
        tableName = command[1];
        DataSet[] data = manager.getTableData(tableName);
        view.write(getTableString(data));
    }

    public String getTableString(DataSet[] data) {
        int maxColumnSize;
        maxColumnSize = getMaxColumnSize(data);
        if (maxColumnSize==0){
            return getEmptyTable(tableName);
        }else{
            return getHeaderOfTheTable(data) + getStringTableData(data);
        }
    }

    private String getEmptyTable(String tableName) {
        String textEmptyTable="║ Table '" + tableName + "' is empty or does not exist ║";
        String result = "╔";
        for (int i = 0; i < textEmptyTable.length()-2; i++) {
            result += "═";
        }
        result += "╗\n";
        result += textEmptyTable + "\n";
        result += "╚";
        for (int i = 0; i < textEmptyTable.length()-2; i++) {
            result += "═";
        }
        result += "╝";
        return result;
    }

    private int getMaxColumnSize(DataSet[] dataSets) {
        int maxLength = 0;
        if (dataSets.length > 0) {
            String[] columnNames = dataSets[0].getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                if (columnNames[i].length() > maxLength) {
                    maxLength = columnNames[i].length();
                }
            }

            for (int i = 0; i < dataSets.length; i++) {
                Object[] values = dataSets[i].getValues();
                for (int j = 0; j < values.length; j++) {
                    if (values[j] instanceof String) {
                        if (String.valueOf(values[j]).length() > maxLength) {
                            maxLength = String.valueOf(values[j]).length();
                        }
                    }
                }
            }
        }
        return maxLength;
    }

    public String getStringTableData(DataSet[] dataSets) {
        int rowsCount;
        rowsCount = dataSets.length;
        int maxColumnSize = getMaxColumnSize(dataSets);
        String result = "" ;
        if (maxColumnSize%2==0){
            maxColumnSize+=2;
        }else{
            maxColumnSize+=3;
        }
        int columnCount = getColumnCount(dataSets);

        for (int row = 0; row < rowsCount; row++) {
            Object[] values = dataSets[row].getValues();
            result += "║";
            for (int column = 0; column < columnCount; column++) {
                int valuesLength = String.valueOf(values[column]).length();
                if (valuesLength % 2 == 0) {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += String.valueOf(values[column]);
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += "║";
                } else {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += String.valueOf(values[column]);
                    for (int j = 0; j <= (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += "║";
                }
            }
            result +="\n";
            if (row < rowsCount-1){
                result += "╠";
                for (int j = 1; j < columnCount ; j++) {
                    for (int i = 0; i < maxColumnSize; i++) {
                        result += "═";
                    }
                    result += "╬";
                }
                for (int i = 0; i < maxColumnSize; i++) {
                    result += "═";
                }
                result += "╣\n";
            }
        }
        result += "╚";
        for (int j = 1; j < columnCount ; j++) {
            for (int i = 0; i < maxColumnSize; i++) {
                result += "═";
            }
            result += "╩";
        }
        for (int i = 0; i < maxColumnSize; i++) {
            result += "═";
        }
        result += "╝\n";

        return result;
    }

    private int getColumnCount(DataSet[] dataSets) {
        int result = 0;
        if (dataSets.length > 0){
            return dataSets[0].getColumnNames().length;
        }
        return result;
    }

    public String getHeaderOfTheTable(DataSet[] dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        String result = "";
        int columnCount = getColumnCount(dataSets);
        if (maxColumnSize % 2 == 0) {
            maxColumnSize += 2;
        } else {
            maxColumnSize += 3;
        }
        result += "╔";
        for (int j = 1; j < columnCount; j++) {
            for (int i = 0; i < maxColumnSize; i++) {
                result += "═";
            }
            result += "╦";
        }
        for (int i = 0; i < maxColumnSize; i++) {
            result += "═";
        }
        result += "╗\n";
        String[] columnNames = dataSets[0].getColumnNames();
        for (int column = 0; column < columnCount; column++) {
            result += "║";
            int columnNamesLength = columnNames[column].length();
            if (columnNamesLength %2 == 0){
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
                result += columnNames[column];
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
            }else {
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
                result += columnNames[column];
                for (int j = 0; j <= (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
            }

        }

        result += "║\n";

        //last string of the header
        if (dataSets.length > 0) {
            result += "╠";
            for (int j = 1; j < columnCount; j++) {
                for (int i = 0; i < maxColumnSize; i++) {
                    result += "═";
                }
                result += "╬";

            }
            for (int i = 0; i < maxColumnSize; i++) {
                result += "═";
            }
            result += "╣\n";
        } else {
            result += "╚";
            for (int j = 1; j < columnCount; j++) {
                for (int i = 0; i < maxColumnSize; i++) {
                    result += "═";
                }
                result += "╩";

            }
            for (int i = 0; i < maxColumnSize; i++) {
                result += "═";
            }
            result += "╝\n";
        }
        return result;
    }


}
