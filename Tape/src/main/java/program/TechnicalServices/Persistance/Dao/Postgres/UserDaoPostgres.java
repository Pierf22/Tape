package program.TechnicalServices.Persistance.Dao.Postgres;

import program.Domain.User;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.Dao.UserDao;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDaoPostgres implements UserDao {
    Connection connection;
    public UserDaoPostgres(Connection con){this.connection=con;}
    @Override
    public User findByPrimaryKey(String username) {
        User user = null;
        String query = "select * from users where username = ?";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
        return user;
    }

    @Override
    public void saveOrUpdate(User user) {
        String insertString="INSERT INTO users VALUES (?,?)";
        PreparedStatement st;
        try {
            st=connection.prepareStatement(insertString);
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());

            st.executeUpdate();


        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }

    @Override
    public Set<User> findAll() {
        Set<User> users=new HashSet<>();
        String query = "select * from users";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                User user =new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                if(!Objects.equals(user.getUsername(), "SYSTEM")) //SYSTEM is a user that is used to send messages to all users
                    users.add(user);

            }
        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
        return users;
    }

    @Override
    public User findByUsernameAndPassword(String usernameText, String pass) {
        User user = null;
        String query = "select * from users where username = ? and password=?";
        try {
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, usernameText);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(usernameText);
                user.setPassword(pass);
            }

        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
        return user;
    }
}
