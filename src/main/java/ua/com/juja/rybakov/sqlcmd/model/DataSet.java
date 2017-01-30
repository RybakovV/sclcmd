package ua.com.juja.rybakov.sqlcmd.model;

import java.util.List;

/**
 * Created by Vitaliy Ryvakov on 15.12.2016.
 */
public interface DataSet {
    void put(String columnName, Object value);

    List<Object> getColumnNames();

    List<Object> getValues();

    String getValuesString();

}
