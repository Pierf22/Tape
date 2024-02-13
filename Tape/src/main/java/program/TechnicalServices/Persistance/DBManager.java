package program.TechnicalServices.Persistance;

import program.TechnicalServices.Persistance.Dao.ConversationDao;
import program.TechnicalServices.Persistance.Dao.Postgres.ConversationDaoPostgres;
import program.TechnicalServices.Persistance.Dao.Postgres.UserDaoPostgres;
import program.TechnicalServices.Persistance.Dao.UserDao;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 public class DBManager { // Singleton connection to the database
    private static DBManager instance = null;
    private DBManager(){}
    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }
    Connection con = null;

    public Connection getConnection(){
        if (con == null){
            try {
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TapeDatabase", "postgres", "0000");
            } catch (SQLException e) {
               showDatabaseError();
            }
        }
        return con;
    }
    public UserDao getUserDao(){
        return new UserDaoPostgres(getConnection());
    }

    public ConversationDao getConversationDao(){
        return new ConversationDaoPostgres(getConnection());
    }
     public void showDatabaseError() {
         JOptionPane.showMessageDialog(null, "Impossible to contact the Database","Error",JOptionPane.ERROR_MESSAGE );
         System.exit(1);
     }
 }
