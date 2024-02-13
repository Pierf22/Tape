package program.Domain;

public interface Subject { //the subject interface
    void attach(Observer observer);
    void notifyANewMessage(Message message);
    void notifyCreation();
    void detach(User loggedUser);
}
