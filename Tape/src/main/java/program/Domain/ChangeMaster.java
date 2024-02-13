package program.Domain;

import program.ClientThread;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.Dao.ConversationDao;
import program.TechnicalServices.Persistance.Dao.UserDao;
import program.TechnicalServices.Persistance.IDBrokerMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class ChangeMaster { //singleton mediator class that manages the communication and the shared data
    private final HashMap<Observer, ClientThread> clientThreadHashMap=new HashMap<>(); //the clientThread is the thread that is associated with the user
    private final HashMap<Subject, Set<Observer>> subjectObserverHashMap=new HashMap<>(); //subject to observer mapping
    private ChangeMaster(){} //private constructor
    private final HashMap<Conversation, Set<Message>> conversations=new HashMap<>(); //shared data
    private final Set<User> users=new HashSet<>();
    private static final ChangeMaster instance=new ChangeMaster(); //singleton instance
    public static synchronized ChangeMaster getInstance(){
        return instance;
    } //returns the singleton instance

    public synchronized void  addAClient(ClientThread clientThread, User loggeduser) {
        clientThreadHashMap.put(loggeduser, clientThread); //maps the threads that are active
    }
    public synchronized Set<Conversation> getConversations(User user) { //returns the conversations that the user is part of
        ConversationDao conversationDao= DBManager.getInstance().getConversationDao();
        for(Conversation conversation: conversationDao.getUserConversation(user)){
            if(!conversations.containsKey(conversation))
                conversations.put(conversation, null);
        }
        return conversations.keySet().stream().filter(conversation -> conversation.getParticipants().contains(user.getUsername())).collect(Collectors.toSet());
    }
    public synchronized void register(Subject subject, Observer observer) { //registers an observer to a subject
        if(!subjectObserverHashMap.containsKey(subject))
            subjectObserverHashMap.put(subject, new HashSet<>());
        if(subject instanceof Conversation){
            if(!conversations.containsKey(subject)){
                conversations.put((Conversation) subject, null);
            }
        }
        subjectObserverHashMap.get(subject).add(observer);
    }
    public synchronized User getUserWithUsernameAndPassword(String usernameText, String pass) {
        Optional<User> existingUser = users.stream()
                .filter(user -> user.getUsername().equals(usernameText) && user.getPassword().equals(pass))
                .findFirst(); //checks if the user is already in the list of users
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        UserDao userDao = DBManager.getInstance().getUserDao();
        User user = userDao.findByUsernameAndPassword(usernameText, pass);
        if (user != null) {
            users.add(user);
        }
        return user;
    }
    public synchronized Set<Message> getAllMessagesForAConversation(Conversation conversation) {
        if(conversations.get(conversation)==null){ //if the messages are not in the shared data, it adds it
            conversations.put(conversation, new HashSet<>());
            ConversationDao conversationDao=DBManager.getInstance().getConversationDao();
            conversations.get(conversation).addAll(conversationDao.findAllMessageByConversationId(conversation.getId()));
        }
        return conversations.get(conversation);
    }
    public synchronized boolean getUserWithUsername(String username) {
        UserDao userDao=DBManager.getInstance().getUserDao();
        return userDao.findByPrimaryKey(username) != null;
    }
    public synchronized void addAUser(User user) {
        UserDao userDao=DBManager.getInstance().getUserDao();
        userDao.saveOrUpdate(user);
        users.add(user);
    }
    public synchronized void notify(Subject subject, Object ob) {
        if(subject instanceof Conversation conversation && ob instanceof Message){
            if(conversations.get(conversation)==null){
                getAllMessagesForAConversation(conversation);
            }
            conversations.get(conversation).add((Message) ob); //adds the message to the shared data
            for(Observer observer: subjectObserverHashMap.get(subject)){ //notifies all the observers of the shared data
                if(clientThreadHashMap.containsKey(observer))
                    clientThreadHashMap.get(observer).update(subject, ob);
            }

        }

    }
    public synchronized void notifyCreation(Subject subject) { //notifies the observers of the creation of a new subject attached to them
        if(subject instanceof Conversation ){
            for(Observer observer: subjectObserverHashMap.get(subject)){
                if(clientThreadHashMap.containsKey(observer))
                    clientThreadHashMap.get(observer).updateCreation(subject);
            }
        }
    }

    public synchronized void unRegister(Subject subject, Observer observer) {
        if(subject instanceof Conversation conversation && observer instanceof User){
            ConversationDao conversationDao=DBManager.getInstance().getConversationDao();
            if(conversationDao.getNumberOfParticipant(conversation)==1){ //if the conversation has only one observer, it deletes the conversation
                subjectObserverHashMap.remove(subject);
                conversationDao.deleteAConversation( conversation);
                conversations.remove(conversation);
        }else {
                User user=(User) observer; //if the conversation has more than one observer, it removes the observer, and sends a message to the other observers
                subjectObserverHashMap.get(subject).remove(observer);
                Message message=new Message();
                message.setId(IDBrokerMessage.getId(DBManager.getInstance().getConnection()));
                message.setIdConv(conversation.getId());
                message.setText(user.getUsername());
                message.setSender("SYSTEM"); //the sender is the system to notify that the user has left the conversation
                conversation.sendAMessage(message);
        }
    }}
    public synchronized void removeAObserver(Observer observer) {
        clientThreadHashMap.remove(observer);
    } //removes the thread from the list of active threads (no more notifications will be sent)
}
