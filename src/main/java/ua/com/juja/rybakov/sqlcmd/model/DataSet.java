package ua.com.juja.rybakov.sqlcmd.model;

import java.util.Arrays;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
 */
public class DataSet {

    static class Data{

        private String columnName;
        private Object value;

        public Data(String columnName, Object value){
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

    Data data[] = new Data[100]; //TODO bad magic number

    public int index = 0;

    public void put(String columnName, Object value) {
        data[index] = new Data(columnName, value);
        index++;
    }

    public String[] getColumnNames(){
        String[] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getColumnName();
        }
        return result;
    }

    public Object[] getValues(){
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i]=data[i].getValue();
        }
        return result;
    }

    public String getValuesString(){
        String result = "";
        for (int i = 0; i < getValues().length; i++) {
            Object obj = getValues()[i];
            if (obj instanceof String){
                result += "'" + obj + "'";
            }else {
                result += obj;
            }
            result += ", ";
        }
        result = result.substring(0,result.length()-2);
        return result;
    }

    @Override
    public String toString(){
        return "DataStr{\n" +
            "columnNames: " + Arrays.toString(getColumnNames()) + "\n" +
            "value: " + Arrays.toString((getValues())) +"\n" + "}";
    }

}
