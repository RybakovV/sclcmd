package ua.com.juja.sclcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by MEBELBOS-2 on 10.08.2016.
 */
public abstract class DatabaseManagerTest {

    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

   @Before
    public void sutup(){
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
    public void testGetTableHead(){
        String tableHead = manager.getHeaderOfTheTable("empty");
        if (manager.getVersionDatabase().equals("PostgreSQL")) {
            assertEquals("╔════════════════════════╗\n" +
                         "║ Table 'empty' is empty ║\n" +
                         "╚════════════════════════╝",
                    tableHead);
        }else {
            assertEquals("╔════╗\n" +
                         "║ id ║\n" +
                         "╚════╝\n",
                    tableHead);
        }
    }

    @Test
    public void testGetTableStringTableTest(){
        String tableName = "test";
        assertEquals(
                        "╔════╗\n" +
                        "║ id ║\n" +
                        "╚════╝\n", manager.getTableString(tableName));
    }


    @Test
    public void testGetTableString(){
        assertEquals(
                "╔════════════╦════════════╦════════════╗\n" +
                "║     id     ║    name    ║  password  ║\n" +
                "╠════════════╬════════════╬════════════╣\n" +
                "║     13     ║ Stivennnn  ║    pswd    ║\n" +
                "╚════════════╩════════════╩════════════╝\n", manager.getTableString("user"));
    }

    @Test
    public void testGetColumnCount(){
        if (manager.getVersionDatabase().equals("PostgreSQL")){
            assertEquals(0,manager.getColumnCount("empty"));

        }else{
            assertEquals(1,manager.getColumnCount("empty"));
        }
        assertEquals(1,manager.getColumnCount("test"));
        assertEquals(3,manager.getColumnCount("user"));
    }

    @Test
    public void testGetMaxColumnSize(){
        if (manager.getVersionDatabase().equals("PostgreSQL")){
            assertEquals(0,manager.getColumnCount("empty"));

        }else{
            assertEquals(1,manager.getColumnCount("empty"));
        }
    }
    @Test
    public void testPrintTablesData(){

        String tablesString = manager.getTableString("empty");
        if (manager.getVersionDatabase().equals("PostgreSQL")) {
            assertEquals("╔════════════════════════╗\n" +
                         "║ Table 'empty' is empty ║\n" +
                         "╚════════════════════════╝",
                    tablesString);
        }else {
            assertEquals("╔════╗\n" +
                         "║ id ║\n" +
                         "╚════╝\n",
                    tablesString);
        }
    }

    @Test
    public void testGetAllTablesOfDataBase(){
        String[] tables = manager.getAllTablesOfDataBase();
        Arrays.sort(tables);
        assertEquals("[empty, test, user]", Arrays.toString(tables));
    }

    @Test
    public void testUpdate(){
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
    public void testClear(){
        String tableName = "user";
        manager.clear(tableName);
        assertEquals("[]", Arrays.toString(manager.getTableData(tableName)));
    }

    @Test
    public void testGetColumnNames(){
        String tableName = "user";
        assertEquals("[id, name, password]", Arrays.toString(manager.getColumnNames(tableName)));
    }


}
