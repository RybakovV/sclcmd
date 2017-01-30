package ua.com.juja.rybakov.sqlcmd.controller.command;

import ua.com.juja.rybakov.sqlcmd.model.DataSet;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.viuw.View;

import java.util.List;

public class Print implements Command {

    private View view;
    private DatabaseManager manager;
    private String tableName;

    public Print(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("print ");
    }

    @Override
    public void process(String input) {
        String[] command = parseCommand(input);
        tableName = command[1];
        List<DataSet> data = manager.getTableData(tableName);
        view.write(getTableString(data));
    }

    private String[] parseCommand(String input) {
        String[] command = input.split(" ");
        if (command.length != 2) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + (command.length - 1));
        }
        return command;
    }

    private String getTableString(List<DataSet> data) {
        if (data.size() == 0) {
            return getEmptyTable(tableName);
        } else {
            return getHeaderOfTheTable(data) + getStringTableData(data);
        }
    }

    private String getEmptyTable(String tableName) {
        String textEmptyTable = "║ Table '" + tableName + "' is empty or does not exist ║";
        String result = "╔";
        for (int i = 0; i < textEmptyTable.length() - 2; i++) {
            result += "═";
        }
        result += "╗\n";
        result += textEmptyTable + "\n";
        result += "╚";
        for (int i = 0; i < textEmptyTable.length() - 2; i++) {
            result += "═";
        }
        result += "╝";
        return result;
    }

    private int getMaxColumnSize(List<DataSet> dataSets) {
        int maxColumnSize;
        int maxLengthHeader = getMaxColumnSizeHeader(dataSets);
        int maxLengthData = getMaxColumnSizeData(dataSets);
        if (maxLengthHeader > maxLengthData) {
            maxColumnSize = maxLengthHeader;
        }else {
            maxColumnSize = maxLengthData;
        }
        if (maxColumnSize % 2 == 0) {
            maxColumnSize += 2;
        } else {
            maxColumnSize += 3;
        }
        return maxColumnSize;
    }

    public int getMaxColumnSizeHeader(List<DataSet> dataSets) {
        int result = 0;
        if (dataSets.size() > 0) {
            List<Object> columnNames = dataSets.get(0).getColumnNames();
            for (Object name : columnNames) {
                if (String.valueOf(name).length() > result) {
                    result = String.valueOf(name).length();
                }
            }
        }
        return result;
    }

    public int getMaxColumnSizeData(List<DataSet> dataSets) {
        int result = 0;
        if (dataSets.size() > 0) {
            for (DataSet dataSet : dataSets) {
                List<Object> values = dataSet.getValues();
                for (Object value : values) {
                    if (String.valueOf(value).length() > result) {
                        result = String.valueOf(value).length();
                    }
                }
            }
        }
        return result;
    }

    private String getStringTableData(List<DataSet> dataSets) {
        String result = "";
        result += getDataString(dataSets);
        result += getLastString(dataSets);
        return result;
    }

    private String getDataString(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        int columnCount = getColumnCount(dataSets);
        String result = "";
        int rowsCount = dataSets.size();
        for (int row = 0; row < rowsCount; row++) {
            List<Object> values = dataSets.get(row).getValues();
            result += getStringData(values, maxColumnSize);
            result += "\n";
            if (row < rowsCount - 1) {
                result += getSeparatorString(dataSets);
            }
        }
        return result;
    }

    private String getSeparatorString(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        int columnCount = getColumnCount(dataSets);
        String result = "╠";
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
        return result;
    }

    private String getLastString(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        int columnCount = getColumnCount(dataSets);

        String result = "╚";
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
        return result;
    }

    private int getColumnCount(List<DataSet> dataSets) {
        int result = 0;
        if (dataSets.size() > 0) {
            return dataSets.get(0).getColumnNames().size();
        }
        return result;
    }

    private String getHeaderOfTheTable(List<DataSet> dataSets) {
        String result = "";
        result += getFirstString(dataSets);
        result += getColumnNamesString(dataSets);
        result += getLastStringHeaderTable(dataSets);
        return result;
    }

    private String getLastStringHeaderTable(List<DataSet> dataSets) {
        String result = "";
        if (dataSets.size() > 0) {
            result += getSeparatorString(dataSets);
        } else {
            result += getLastString(dataSets);
        }
        return result;
    }

    private String getFirstString(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        int columnCount = getColumnCount(dataSets);
        String result = "╔";
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
        return result;
    }

    private String getColumnNamesString(List<DataSet> dataSets) {
        int maxColumnSize = getMaxColumnSize(dataSets);
        List<Object> columnNames = dataSets.get(0).getColumnNames();
        String result = getStringData(columnNames, maxColumnSize);
        result += "\n";
        return result;
    }

    private String getStringData(List<Object> columnNames, int maxColumnSize) {
        String result = "║";

        for (int column = 0; column < columnNames.size(); column++) {
            int columnNamesLength = String.valueOf(columnNames.get(column)).length();
            int countSpace = (maxColumnSize - columnNamesLength) / 2;
            if (columnNamesLength % 2 == 0) {
                result += addSpace(countSpace);
                result += String.valueOf(columnNames.get(column));
                result += addSpace(countSpace);
            } else {
                result += addSpace(countSpace);
                result += columnNames.get(column);
                result += addSpace(countSpace+1);
            }
            result += "║";
        }
        return result;
    }

    private String addSpace(int count) {
        String result = "";
        for (int j = 0; j < count; j++) {
            result += " ";
        }
        return result;
    }
}
