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
            List<String> columnNames = dataSets.get(0).getColumnNames();
            for (String name : columnNames) {
                if (name.length() > result) {
                    result = name.length();
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
            result += "║";
            for (int column = 0; column < columnCount; column++) {
                int valuesLength = String.valueOf(values.get(column)).length();
                if (valuesLength % 2 == 0) {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += String.valueOf(values.get(column));
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += "║";
                } else {
                    for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += String.valueOf(values.get(column));
                    for (int j = 0; j <= (maxColumnSize - valuesLength) / 2; j++) {
                        result += " ";
                    }
                    result += "║";
                }
            }
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
        int columnCount = getColumnCount(dataSets);
        List<String> columnNames = dataSets.get(0).getColumnNames();
        String result = "║";
        for (int column = 0; column < columnCount; column++) {
            int columnNamesLength = columnNames.get(column).length();
            if (columnNamesLength % 2 == 0) {
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
                result += columnNames.get(column);
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
            } else {
                for (int j = 0; j < (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
                result += columnNames.get(column);
                for (int j = 0; j <= (maxColumnSize - columnNamesLength) / 2; j++) {
                    result += " ";
                }
            }
            result += "║";
        }
        result += "\n";
        return result;
    }
}
