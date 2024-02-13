package program.TechnicalServices.Persistance.Dao.Postgres;



import program.Domain.Conversation;
import program.Domain.Message;
import program.Domain.User;
import program.TechnicalServices.Persistance.DBManager;
import program.TechnicalServices.Persistance.Dao.ConversationDao;

import java.sql.*;
import java.util.*;

 public class ConversationDaoPostgres implements ConversationDao {
    Connection connection;
    public ConversationDaoPostgres(Connection con){this.connection=con;}
     public void saveOrUpdate(Conversation conversation) {
        String insertString="INSERT INTO conversation VALUES (?,?)";
        PreparedStatement st;
        try {
            st=connection.prepareStatement(insertString);
            st.setString(1, conversation.getName());
            st.setLong(2, conversation.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }
    public void connectUsersToConversation(Conversation conversation, Set<User> usernames) {
        String insertString="INSERT INTO user_conversation VALUES (?,?)";
        PreparedStatement st;
        try {
            for(User g:usernames){
                st=connection.prepareStatement(insertString);
                st.setString(1, g.getUsername());
                st.setLong(2, conversation.getId());

                st.executeUpdate();
            }
        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }
     void deleteById(long id) {
        String insertString1="DELETE FROM user_conversation WHERE id=?";
        String insertString2="DELETE FROM conversation WHERE id=?";
        PreparedStatement st;
        try {
                st=connection.prepareStatement(insertString1);
                st.setLong(1, id);

                st.executeUpdate();
                st=connection.prepareStatement(insertString2);
                st.setLong(1, id);
                st.executeUpdate();

        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }
    @Override
    public void addAMessage(Message message) {
        String insertString="INSERT INTO message VALUES (?,?,?,?)";
        PreparedStatement st;
        try {
            st=connection.prepareStatement(insertString);
            st.setLong(1, message.getId());
            st.setString(2, message.getText());
            st.setString(3, message.getSender());
            st.setLong(4, message.getIdConv());
            st.executeUpdate();

        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }
    @Override
    public Set<Message> findAllMessageByConversationId(long id) {
        Set<Message> messageList =new HashSet<>();
        String query="select * from message where id_conv=?";
        try{
            PreparedStatement ps= connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet set= ps.executeQuery();
            while(set.next()){
                Message message =new Message();
                message.setId(set.getLong("id"));
                message.setSender(set.getString("username"));
                message.setText(set.getString("text"));
                message.setIdConv(set.getLong("id_conv"));
                messageList.add(message);

            }
        }catch (SQLException e){
            DBManager.getInstance().showDatabaseError();
        }
        return messageList;
    }
    void deleteAllMessageOfAConversation(long id) {
        String insertString="DELETE FROM message WHERE id_conv=?";
        PreparedStatement st;
        try {
            st=connection.prepareStatement(insertString);
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
    }

    @Override
    public void createConversation(Conversation conversation, Set<User> users) {
            saveOrUpdate(conversation);
            connectUsersToConversation(conversation, users);
    }

    @Override
    public void deleteAConversation(Conversation conversation){
        deleteAllMessageOfAConversation(conversation.getId());
        deleteById(conversation.getId());
    }

    @Override
    public Set<Conversation> getUserConversation(User currentUser) {
        Set<Conversation> conversations=new HashSet<>();

        String query="select * from user_conversation, conversation where username=? and conversation.id=user_conversation.id";
        try{
            PreparedStatement st=connection.prepareStatement(query);
            st.setString(1, currentUser.getUsername());
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                Conversation conversation =new Conversation();
                conversation.setId(rs.getLong("id"));
                conversation.setName(rs.getString("name"));
                conversation.setParticipants(getParticipant(conversation.getId()));
                conversations.add(conversation);
            }
        } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        }
        return conversations;
    }

     @Override
     public void removeAParticipant(String username, Long id) {
         String query="DELETE FROM user_conversation WHERE username=? and id=?";
         try{
             PreparedStatement st=connection.prepareStatement(query);
             st.setString(1, username);
             st.setLong(2, id);
             st.executeUpdate();
         } catch (SQLException e) {
                DBManager.getInstance().showDatabaseError();
         }
     }

     @Override
     public int getNumberOfParticipant(Conversation conversation) {
        int count=0;
        String query="select count(*) from user_conversation where id=?";
        try{
            PreparedStatement st=connection.prepareStatement(query);
            st.setLong(1, conversation.getId());
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                count=rs.getInt("count");
            }
     } catch (SQLException e) {
            DBManager.getInstance().showDatabaseError();
        } return count;
    }

         private Set<String> getParticipant(long id) {
         Set<String> participants=new HashSet<>();

         String query="select * from user_conversation where id=? ";
         try{
             PreparedStatement st=connection.prepareStatement(query);
             st.setLong(1, id);
             ResultSet rs=st.executeQuery();
             while(rs.next()){
                 participants.add(rs.getString("username"));
             }
         } catch (SQLException e) {
             DBManager.getInstance().showDatabaseError();
         }
         return participants;
     }
 }
