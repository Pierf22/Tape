package program.TechnicalServices.Persistance.Dao;



import program.Domain.Conversation;
import program.Domain.Message;
import program.Domain.User;

import java.util.Set;

public interface ConversationDao {
    void addAMessage(Message message);
     Set<Message> findAllMessageByConversationId(long id);
    void createConversation(Conversation conversation, Set<User> users);
     void deleteAConversation(Conversation conversation);
    Set<Conversation> getUserConversation(User currentUser);
    void removeAParticipant(String username, Long id);

    int getNumberOfParticipant(Conversation conversation);
}
