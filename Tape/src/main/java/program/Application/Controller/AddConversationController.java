package program.Application.Controller;

import program.Domain.Conversation;
import program.Domain.User;
import program.Domain.ChangeMaster;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.Dao.ConversationDao;
import program.TechnicalServices.Persistance.Dao.UserDao;
import program.TechnicalServices.Persistance.IDBrokerConversation;

import java.util.Set;
import java.util.stream.Collectors;

public record AddConversationController(User loggedUser) {

    public Set<User> getUsersToSelect() {
        UserDao userDao = DBManager.getInstance().getUserDao();
        return userDao.findAll();
    }

    public void createConversation(String name, Set<User> selectedUser) {
        selectedUser.add(loggedUser);
        Conversation conversation = new Conversation();
        ConversationDao conversationDao = DBManager.getInstance().getConversationDao();
        conversation.setParticipants(selectedUser.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet()));
        conversation.setId(IDBrokerConversation.getId(DBManager.getInstance().getConnection()));
        conversation.setName(name);
        for (User user : selectedUser) {
            ChangeMaster.getInstance().register(conversation, user);
        }
        conversationDao.createConversation(conversation, selectedUser);
        conversation.notifyCreation();


    }
}
