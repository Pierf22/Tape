package program.TechnicalServices.Persistance.Dao;

import program.Domain.User;

import java.util.Set;

public interface UserDao {
     User findByPrimaryKey(String username);
     void saveOrUpdate(User user);
    Set<User> findAll();
    User findByUsernameAndPassword(String usernameText, String pass);
}
