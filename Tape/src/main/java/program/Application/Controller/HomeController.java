package program.Application.Controller;

import program.ClientThread;
import program.Domain.Conversation;
import program.Domain.Message;
import program.Domain.User;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.IDBrokerMessage;
import program.UI.Swing.HomePanel;

import java.util.Set;

public class HomeController {
    private Conversation currentConversation;
    private final HomePanel panel;
    private final User loggedUser;
    private final ClientThread clientThread;
    public HomeController(HomePanel homePanel, User loggeduser, ClientThread clientThread) {
        this.panel=homePanel;
        this.loggedUser=loggeduser;
        this.clientThread=clientThread;
    }

    public void sendMessage() {
        if(currentConversation==null) //if there is no conversation selected, it does nothing
            return;
        Message message=new Message();
        message.setId(IDBrokerMessage.getId(DBManager.getInstance().getConnection()));
        message.setText(panel.getMessageText().getText());
        message.setIdConv(currentConversation.getId());
        message.setSender(loggedUser.getUsername());
        currentConversation.sendAMessage(message);
        panel.getMessageText().setText("");
    }
    public Set<Conversation> getConversationsForCurrentUser() {
        return loggedUser.getConversations();
    }
    public Set<Message> getMessageForConversation(Conversation conversation) {
        currentConversation=conversation;
        return conversation.getAllMessages();
    }

    public User getCurrentUser() {
        return loggedUser;
    }

    public void addMessage(Message message) {
        if(currentConversation!=null && currentConversation.getId()==message.getIdConv())
            panel.addAMessage(message);
        else
            panel.addANotification(message.getIdConv());
    }
    public void loadAddConversationScene() {
        clientThread.loadAddConversationScene(loggedUser); //it contacts the client thread to load the add conversation page
    }

    public void addAConversation(Conversation conversation) {
        panel.addAConversation(conversation);
    }

    public void deleteConversation(Conversation conversation) {
        conversation.detach(loggedUser); //it detaches the user (Observer) from the conversation
        panel.removeAConversation(conversation);
        if(currentConversation==conversation)
            panel.clearChatView();
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }
}
