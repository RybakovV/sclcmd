package ua.com.juja.rybakov.sqlcmd.model;

import java.util.*;


/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class DataSetImpl implements DataSet {

    static class Data {
        private String columnName;
        private Object value;

        public Data(String columnName, Object value) {
            this.columnName = columnName;
            this.value = value;
        }

        public String getColumnName() {
            return columnName;
        }

        public Object getValue() {
            return value;
        }
    }

    List<Data> data = new LinkedList<>();

    @Override
    public void put(String columnName, Object value) {
        data.add(new Data(columnName, value));
    }

    @Override
    public List<String> getColumnNames() {
        List<String> result = new LinkedList<>();
        for (Data d : data) {
            result.add(d.getColumnName());
        }
        return result;
    }

    @Override
    public List<Object> getValues() {
        List<Object> result = new LinkedList<>();
        for (Data d : data) {
            result.add(d.getValue());
        }
        return result;
    }

    @Override
    public String getValuesString() {
        String result = "";
        List<Object> values = getValues();
        for (Object value: values) {
            if (value instanceof String) {
                result += "'" + value + "'";
            } else {
                result += value;
            }
            result += ", ";
        }
        return result.substring(0, result.length() - 2);
    }

    @Override
    public String getColumnNamesString() {
        String result = "";
        List<String> columnNames = getColumnNames();
        result = columnNames.toString();
        result = result.substring(1,result.length()-1);
        return result;
    }

    @Override
    public String toString() {
        return "DataStr{\n" +
                "columnNames: " + getColumnNames().toString() + "\n" +
                "value: " + getValues().toString() + "\n" + "}";
    }

}
