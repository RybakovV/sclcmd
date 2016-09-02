package ua.com.juja.sclcmd.model;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by MEBELBOS-2 on 10.08.2016.
 */
public class MysqlDatabaseManagerTest extends DatabaseManagerTest {

    //public String databaseName = "mysqlcmd";
    @Override
    public DatabaseManager getDatabaseManager(){
        DatabaseManager mysqlDatabaseManager = new MysqlDatabaseManager();
        mysqlDatabaseManager.setDatabaseName("mysqlcmd");
        mysqlDatabaseManager.setUserName("root");
        mysqlDatabaseManager.setUserPassword("root");
        return mysqlDatabaseManager;
    }

}
