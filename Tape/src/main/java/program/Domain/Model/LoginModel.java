package program.Domain.Model;

import program.Application.Controller.LoginController;
import program.Domain.ChangeMaster;
import program.Domain.User;


public class LoginModel {
    private LoginController controller;
    public void checkUser(String usernameText, String pass) { //checks if the credentials are correct contacting the ChangeMaster
        User user= ChangeMaster.getInstance().getUserWithUsernameAndPassword(usernameText, pass);
        if(user==null){
            controller.showWrongCredentialError();
        }else
            controller.openHomeScreen(user);
    }

    public void addController(LoginController controller) {
        this.controller=controller;
    }
}
