package ua.com.juja.rybakov.sclcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.rybakov.sqlcmd.model.DatabaseManager;
import ua.com.juja.rybakov.sqlcmd.model.DataSet;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public abstract class DatabaseManagerTest {
    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void sutup() throws SQLException {
        manager = getDatabaseManager();

        String databaseName = manager.getDatabaseName();
        String userName = manager.getUserName();
        String userPassword = manager.getUserPassword();
        manager.connectToDataBase(databaseName, userName, userPassword);

        String tableName = "user";
        manager.clear(tableName);

        DataSet data = new DataSet();
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
            assertEquals("[test, user, empty]", tables.toString());
        } else {
            assertEquals("[empty, test-sql, user]", tables.toString());
        }
    }

    @Test
    public void testUpdate() {
        String tableName = "user";

        DataSet data = new DataSet();
        data.put("id", 17);
        data.put("name", "testUpdate");
        data.put("password", "pswd-testUpdate");
        manager.update(tableName, 13, data);

        assertEquals("[DataStr{\n" +
                "columnNames: [id, name, password]\n" +
                "value: [17, testUpdate, pswd-testUpdate]\n" +
                "}]", Arrays.toString(manager.getTableData(tableName)));
    }

    @Test
    public void testClear() throws SQLException {
        String tableName = "user";
        manager.clear(tableName);
        assertEquals("[]", Arrays.toString(manager.getTableData(tableName)));
    }

    @Test
    public void testGetColumnNames() {
        String tableName = "user";
        assertEquals("[id, name, password]", Arrays.toString(manager.getColumnNames(tableName)));
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }
}
