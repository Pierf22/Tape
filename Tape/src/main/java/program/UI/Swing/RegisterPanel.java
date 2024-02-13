package program.UI.Swing;

import program.Application.Controller.RegisterController;

import javax.swing.*;
import java.awt.*;


public class RegisterPanel extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private RegisterController controller;

    public RegisterPanel() {}
    public void generateUI(){
        setTitle("Registration Form");
        setSize(300, 200);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        JLabel usernameLabel = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        JLabel passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);


        JButton submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);
        submitButton.addActionListener(e -> controller.registerAUser());
        add(panel);
        setVisible(true);
    }


    public void addController(RegisterController controller) {
        this.controller=controller;
    }
    public void showUserAlreadyExistsError() {
        JOptionPane.showMessageDialog(null, "User already exists","Error",JOptionPane.ERROR_MESSAGE );
        usernameField.setText("");
        passwordField.setText("");
    }

    public void showMandatoryFieldError() {
        JOptionPane.showMessageDialog(null, "username and password are mandatory fields","Error",JOptionPane.ERROR_MESSAGE );
        usernameField.setText("");
        passwordField.setText("");
    }

    public void showRegistrationComplete() {
        JOptionPane.showMessageDialog(null, "registration complete!","OK",JOptionPane.INFORMATION_MESSAGE );
        this.dispose();
    }
    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getPasswordField() {
        return passwordField;
    }
}
