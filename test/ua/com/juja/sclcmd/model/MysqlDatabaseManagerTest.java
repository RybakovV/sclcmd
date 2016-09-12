package ua.com.juja.sclcmd.model;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.MysqlDatabaseManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rybakov Vitaliy on 12.09.2016.
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
