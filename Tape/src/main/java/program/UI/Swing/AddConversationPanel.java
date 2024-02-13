package program.UI.Swing;
import program.Application.Controller.AddConversationController;
import program.Domain.User;

import javax.swing.*;
import java.awt.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AddConversationPanel extends JFrame {

    private JTextField nameConv;

    private AddConversationController controller;
    private final HashMap<JCheckBox, User> userHashMap=new HashMap<>();

    public AddConversationPanel() {

    }

    private void initializeComponents() {
        nameConv = new JTextField();
        JButton createButton = new JButton("Create");
        JPanel memberList=new JPanel();
        BoxLayout boxLayout=new BoxLayout(memberList, BoxLayout.Y_AXIS);
        memberList.setLayout(boxLayout);
        for(User user: controller.getUsersToSelect()){
            if(!user.equals(controller.loggedUser())){
            JCheckBox checkBox=new JCheckBox(user.getUsername());
            userHashMap.put(checkBox, user);
            memberList.add(checkBox);
        }}
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setViewportView(memberList);
        setLayout(null);
        scrollPane.setOpaque(false);

        add(nameConv).setBounds(148, 27, 132, 26);
        JLabel label=new JLabel("Name");
        Font font=new Font(label.getFont().getName(), label.getFont().getStyle(), 20);
        JLabel label1=new JLabel("Participants");
        label.setFont(font);
        label1.setFont(font);
        add(label).setBounds(14, 44, 100, 26);
        add(label1).setBounds(14, 108, 120, 26);
        add(scrollPane).setBounds(148, 69, 132, 62);
        add(createButton).setBounds(188, 154, 100, 30);
        createButton.addActionListener(e -> {
            Set<User> selectedUser=new HashSet<>();
            for(JCheckBox box: userHashMap.keySet()){
                if(box.isSelected())
                    selectedUser.add(userHashMap.get(box));
            }
            controller.createConversation(nameConv.getText(), selectedUser);
            closePanel();
        });

    }

    private void closePanel() {
        this.dispose();
    }

    private void createAndShowGUI() {
        setTitle("New conversation");
        setSize(310, 250);
        setResizable(false);
        setVisible(true);
    }

    public void addController(AddConversationController addConversationController) {
        this.controller=addConversationController;
    }

    public void generateUI() {
        initializeComponents();
        createAndShowGUI();
    }

}

