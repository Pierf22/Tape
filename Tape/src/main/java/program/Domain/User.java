package program.Domain;
import program.Application.Controller.HomeController;

import java.util.Objects;
import java.util.Set;
public class User implements Observer{
    public Set<Conversation> getConversations() {
        Set<Conversation> conversations1=ChangeMaster.getInstance().getConversations(this); //gets the conversations that the user is part of
        for(Conversation conversation:conversations1){
                conversation.attach(this); //the user attaches itself to the conversation
            }
        return conversations1;
    }
    @Override
    public void update(Subject subject, Object ob) {  //receiving a new message
        if(subject instanceof Conversation && ob instanceof Message){
            controller.addMessage((Message) ob);
        }
    }
    @Override
    public void updateCreation(Subject subject) { //the user is added to a conversation
        if(subject instanceof Conversation conversation){
            controller.addAConversation(conversation);
        }
    }
    public String username;
    private String password;
    private HomeController controller;
    public String getUsername() {
        return username;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void addController(HomeController homeController) {
        this.controller=homeController;
    }
}
