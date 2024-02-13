package program.UI.Swing;

import program.Application.Controller.HomeController;
import program.Domain.Conversation;
import program.Domain.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class HomePanel extends JFrame {
    private JLabel usersInConversation; //Label for visualizing the users in the conversation
    private HomeController controller;
    private final HashMap<Conversation, JPanel> conversationJPanelHashMap=new HashMap<>(); //HashMap for adding and removing conversations
    private JPanel conversationsList;
    private JTextArea messageText;
    private JList<JPanel> chatView;
    private DefaultListModel<JPanel> chatViewModel;
    private final HashMap<Long, JLabel> conversationJLabelHashMap=new HashMap<>(); //HashMap for adding and removing notifications
    private ImageIcon notificationLogo; //Image for the notification
    public HomePanel() {}
    public void generateUI() {
        setTitle("Home");
        setSize(800, 600);
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        //generate the title
        JLabel titleLabel = new JLabel("User: " + controller.getCurrentUser().getUsername()+"  ");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel.getFont().getStyle(), 20));
        //generate the button for adding a conversation
        JButton addConversation = new JButton("New Conversation");
        addConversation.addActionListener(e -> controller.loadAddConversationScene());

        leftPanel.add(titleLabel, BorderLayout.LINE_START);
        leftPanel.add(addConversation, BorderLayout.EAST);


        //generate the label for the users in the conversation
        JPanel rightPanel = new JPanel(new BorderLayout());
        usersInConversation = new JLabel();
        usersInConversation.setHorizontalAlignment(SwingConstants.CENTER);
        usersInConversation.setFont(new Font(usersInConversation.getFont().getName(), usersInConversation.getFont().getStyle(), 20));
        rightPanel.add(usersInConversation, BorderLayout.CENTER);

        titlePanel.add(leftPanel, BorderLayout.LINE_START);
        titlePanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        //generate the list of conversations
        JScrollPane scrollChatPane = new JScrollPane();
        scrollChatPane.setPreferredSize(new Dimension(288, 557));
        conversationsList = new JPanel();
        conversationsList.setLayout(new BoxLayout(conversationsList, BoxLayout.Y_AXIS));

        scrollChatPane.setViewportView(conversationsList);
        for (Conversation conversation : controller.getConversationsForCurrentUser()) {
            insertAConversation(conversation); //insert the conversation in the list
        }

        mainPanel.add(scrollChatPane, BorderLayout.WEST);
        //generate the chat
        JPanel chatAndInputPanel = new JPanel(new BorderLayout());
        chatViewModel = new DefaultListModel<>();
        chatView = new JList<>();
        chatView.setModel(chatViewModel);
        chatView.setCellRenderer(new MessageCellRenderer());
        JScrollPane scrollPane = new JScrollPane(chatView);
        chatAndInputPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new FlowLayout());
        messageText = new JTextArea();
        messageText.setLineWrap(true);
        messageText.setPreferredSize(new Dimension(200, 38));
        messagePanel.add(messageText);

        JButton sendMessageButton = new JButton("Send Message");

        messagePanel.add(sendMessageButton);
        sendMessageButton.addActionListener(e -> controller.sendMessage());

        chatAndInputPanel.add(messagePanel, BorderLayout.SOUTH);

        mainPanel.add(chatAndInputPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }
    private void insertAConversation(Conversation conversation) {

        JPanel conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.setMaximumSize(new Dimension(300, 50));

        JLabel conversationLabel = new JLabel(conversation.getName());
        conversationLabel.setFont(new Font(conversationLabel.getFont().getName(), conversationLabel.getFont().getStyle(), 20));
        conversationPanel.add(conversationLabel, BorderLayout.WEST);
        conversationJLabelHashMap.put(conversation.getId(), conversationLabel);

        JButton deleteButton = new JButton("Delete");
        conversationPanel.add(deleteButton, BorderLayout.EAST);
        conversationJPanelHashMap.put(conversation, conversationPanel);
        deleteButton.addActionListener(e -> {
            controller.deleteConversation(conversation);
            usersInConversation.setText("");
        });

        // mouse listener for selecting a conversation and change color when the mouse is over the conversation
        conversationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                conversationPanel.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                conversationPanel.setBackground(null);
            }
            @Override
            public void mouseClicked(MouseEvent e){
                chatViewModel.clear();
                setParticipants(conversation);
                Set<Message> messages = controller.getMessageForConversation(conversation);
                conversationJLabelHashMap.get(conversation.getId()).setIcon(null); //remove the notification
                for (Message message : messages) {
                    addAMessage(message);
                }
            }
        });
        conversationsList.add(conversationPanel);

        conversationsList.revalidate();
        conversationsList.repaint();
    }

    private void setParticipants(Conversation conversation) {
        // get the users in the conversation

        StringBuilder g = new StringBuilder();
        for (String h : conversation.getParticipants()) {
            if (!Objects.equals(h, controller.getCurrentUser().getUsername()))
                g.append(" ").append(h);

        }
        usersInConversation.setText(conversation.getName()+": "+ g);
    }

    public void addController(HomeController homeController) {
        this.controller=homeController;
    }

    public void addANotification(Long idConv) {
        JLabel label=conversationJLabelHashMap.get(idConv);
        if(notificationLogo==null)
            loadNotificationLogo();
        label.setIcon(notificationLogo);
    }

    private void loadNotificationLogo() { //load the notification logo only once
        notificationLogo=new ImageIcon(Objects.requireNonNull(getClass().getResource("/tape/new-email.png")));
        Image im = notificationLogo.getImage();
        Image logoS = im.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        notificationLogo = new ImageIcon(logoS);
    }

    public void addAConversation(Conversation conversation) {
        insertAConversation(conversation);
    }

    public void removeAConversation(Conversation conversation) { //remove a conversation from its container
        Container container=conversationJPanelHashMap.get(conversation).getParent();
        container.remove(conversationJPanelHashMap.get(conversation));
        conversationJPanelHashMap.remove(conversation);
        conversationJLabelHashMap.remove(conversation.getId());
        container.repaint();
        container.revalidate();
    }
    public void clearChatView() {
        chatViewModel.clear();
    }
    private static class MessageCellRenderer extends DefaultListCellRenderer { //useful for rendering a JList of JPanel
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof JPanel panel) {
                panel.setOpaque(false);
                panel.setPreferredSize(new Dimension(400, 40));
                return panel;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
    public void addAMessage(Message message) {
        String sender = message.getSender();
        String content = message.getText();

        JPanel messagePanel = new JPanel();

        JLabel messageLabel = new JLabel();

        FlowLayout flowLayout = new FlowLayout();
        if(Objects.equals(sender, "SYSTEM")){ //if the sender is the system, it means that the message is a user leaving the conversation
            flowLayout.setAlignment(FlowLayout.CENTER);
            messageLabel.setBackground(Color.LIGHT_GRAY);
            messageLabel.setText(content+" has left the chat");
            setParticipants(controller.getCurrentConversation());
        } else if (sender.equals(controller.getCurrentUser().getUsername())) {
            messageLabel.setBackground(Color.CYAN);
            flowLayout.setAlignment(FlowLayout.RIGHT);
            messageLabel.setText(content);
        } else {
            flowLayout.setAlignment(FlowLayout.LEFT);
            messageLabel.setBackground(Color.GREEN);
            messageLabel.setText(sender+": "+content);

        }
        messagePanel.setLayout(flowLayout);
        messageLabel.setOpaque(true);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), messageLabel.getFont().getStyle(), 25));

        messagePanel.add(messageLabel);


        chatViewModel.addElement(messagePanel);

        chatView.setModel(chatViewModel);
    }
    public JTextArea getMessageText() {
        return messageText;
    }

}

