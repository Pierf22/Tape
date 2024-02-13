package program.TechnicalServices.Persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

  public class IDBrokerMessage { // Singleton class to generate unique id for messages
    private static final String query = "SELECT nextval('message_id_seq') AS id";//postgresql

    public static Long getId(Connection connection){
        Long id = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            result.next();
            id = result.getLong("id");
        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
        return id;
    }
}
