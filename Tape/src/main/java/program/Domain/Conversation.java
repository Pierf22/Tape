package program.Domain;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.Dao.ConversationDao;


import java.util.Objects;
import java.util.Set;
public class Conversation implements Subject{
    public void sendAMessage(Message message) {
        ConversationDao conversationDao= DBManager.getInstance().getConversationDao();
        conversationDao.addAMessage(message);
        notifyANewMessage(message);
    }
    public Set<Message> getAllMessages() {
        return ChangeMaster.getInstance().getAllMessagesForAConversation(this);
    }
    @Override
    public void attach(Observer observer) {
        ChangeMaster.getInstance().register(this, observer);
    }
    @Override
    public void notifyANewMessage(Message message) {
        ChangeMaster.getInstance().notify(this,message );
    }

    @Override
    public void notifyCreation() {
        ChangeMaster.getInstance().notifyCreation(this);
    }
    @Override
    public void detach(User loggedUser) {
        ChangeMaster.getInstance().unRegister(this, loggedUser);
        removeParticipant(loggedUser.getUsername());
        ConversationDao conversationDao=DBManager.getInstance().getConversationDao();
        conversationDao.removeAParticipant(loggedUser.getUsername(), id);
    }
    private void removeParticipant(String username) {
        participants.remove(username);
    }
    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    private long id;
    private Set<String> participants;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

