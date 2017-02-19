package ua.com.juja.rybakov.sclcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.rybakov.sqlcmd.controller.Sign;
import ua.com.juja.rybakov.sqlcmd.controller.SignReader;
import ua.com.juja.rybakov.sqlcmd.model.DataSetImpl;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.DataSet;

import java.sql.SQLException;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public abstract class DatabaseManagerTest {
    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() throws SQLException {
        manager = getDatabaseManager();

        String databaseName = manager.getDatabaseName();
        String userName = manager.getUserName();
        String userPassword = manager.getUserPassword();
        Sign sign = new SignReader(databaseName, userName, userPassword);
        manager.connectToDataBase(sign);

        String tableName = "user";
        manager.clear(tableName);

        DataSet data = new DataSetImpl();
        data.put("id", 13);
        data.put("password", "pswd");
        data.put("name", "Stivennnn");
        manager.insert(tableName, data);
    }


    @Test
    public void testGetColumnCount() {
        if (manager.getVersionDatabase().equals("PostgreSQL")) {
            assertEquals(0, manager.getColumnCount("empty"));
            assertEquals(2, manager.getColumnCount("test"));
        } else {
            assertEquals(1, manager.getColumnCount("empty"));
            assertEquals(2, manager.getColumnCount("test-sql"));
        }
        assertEquals(3, manager.getColumnCount("user"));
    }

    @Test
    public void testGetMaxColumnSize() {
        if (manager.getVersionDatabase().equals("PostgreSQL")) {
            assertEquals(0, manager.getColumnCount("empty"));
        } else {
            assertEquals(1, manager.getColumnCount("empty"));
        }
    }

    @Test
    public void testGetAllTablesOfDataBase() {
        Set<String> tables = manager.getAllTablesOfDataBase();

        if (manager.getVersionDatabase().equals("PostgreSQL")) {
            assertEquals("[empty, test, user]", tables.toString());
        } else {
            assertEquals("[empty, test-sql, user]", tables.toString());
        }
    }

    @Test
    public void testUpdate() {
        String tableName = "user";

        DataSet data = new DataSetImpl();
        data.put("id", 17);
        data.put("name", "testUpdate");
        data.put("password", "pswd-testUpdate");
        manager.update(tableName, 13, data);

        assertEquals("[DataStr{\n" +
                "columnNames: [id, name, password]\n" +
                "value: [17, testUpdate, pswd-testUpdate]\n" +
                "}]", manager.getTableData(tableName).toString());
    }

    @Test
    public void testClear() throws SQLException {
        String tableName = "user";
        manager.clear(tableName);
        assertEquals("[]", manager.getTableData(tableName).toString());
    }

    @Test
    public void testGetColumnNames() {
        String tableName = "user";
        assertEquals("[id, name, password]", manager.getColumnNames(tableName).toString());
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }

    @Test
    public void testInsert() throws SQLException {

        String tableName = "user";
        manager.clear(tableName);

        DataSet data = new DataSetImpl();
        data.put("id", 17);
        data.put("name", "testInsert");
        data.put("password", "pswd-testInsert");

        manager.insert(tableName, data);

        assertEquals("[DataStr{\n" +
                "columnNames: [id, name, password]\n" +
                "value: [17, testInsert, pswd-testInsert]\n" +
                "}]", manager.getTableData(tableName).toString());

    }


}


