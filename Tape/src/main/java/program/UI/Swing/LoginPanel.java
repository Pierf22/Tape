package program.UI.Swing;
import program.Application.Controller.LoginController;

import javax.swing.*;
import java.util.Objects;


public class LoginPanel extends JFrame {
    private JTextField usernameField;

    private JPasswordField passwordField;
    private LoginController controller;

    public LoginPanel() {}
    public void generateUI(){ //generates the login page
        setTitle("Login");
        setSize(300, 300);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/tape/logo.png"))));
        logoPanel.add(logoLabel);

        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);


        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        loginButton.addActionListener(e -> controller.checkUser());
        registerButton.addActionListener(e -> controller.registerUser());

        mainPanel.add(logoPanel);
        mainPanel.add(usernamePanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }
    public void addController(LoginController controller) {
        this.controller=controller;
    }
    public void showWrongCredentialError() {
        JOptionPane.showMessageDialog(null, "Incorrect password or username","Warning",JOptionPane.WARNING_MESSAGE );
        usernameField.setText("");
        passwordField.setText("");
    }
    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}

