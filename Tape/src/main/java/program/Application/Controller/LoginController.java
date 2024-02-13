package program.Application.Controller;

import program.ClientThread;
import program.Domain.Model.LoginModel;
import program.Domain.User;
import program.UI.Swing.LoginPanel;


public class LoginController {
    private final LoginPanel panel;
    private final LoginModel model;
    private final ClientThread clientThread;
    public LoginController(LoginPanel loginPanel, LoginModel loginModel, ClientThread clientThread) {
        this.panel=loginPanel;
        this.model=loginModel;
        this.clientThread=clientThread;
    }

    public void checkUser() { //when the user clicks the login button, it checks if the credentials are correct
        String usernameText = panel.getUsernameField().getText();
        char[] pass = panel.getPasswordField().getPassword();
        String password = new String(pass);
        model.checkUser(usernameText, password);
        
    }
    public void registerUser(){ //when the user clicks the register button, it loads the register page
        clientThread.loadRegisterScene();
    }
    public void showWrongCredentialError() {
        panel.showWrongCredentialError();
    }

    public void openHomeScreen(User loggeduser) { //when the user logs in, it loads the home page
        panel.dispose();
        clientThread.loadHomeScreen(loggeduser);
    }
}



