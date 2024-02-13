package program.Domain.Model;

import program.Application.Controller.RegisterController;
import program.Domain.ChangeMaster;
import program.Domain.User;

import java.util.Objects;

public class RegisterModel {
    private RegisterController controller;
    public void addController(RegisterController controller) {
        this.controller=controller;
    }
    public void registerAUser(String username, String pass) {
        if(ChangeMaster.getInstance().getUserWithUsername(username)){
            controller.userAlreadyExists();
        } else if (Objects.equals(username, "") || Objects.equals(pass, "")) {
            controller.mandatoryField();
        }else {
            User user=new User();
            user.setUsername(username);
            user.setPassword(pass);
            ChangeMaster.getInstance().addAUser(user); //the changeMaster manages the list of users
            controller.registrationComplete();
        }
    }
}
