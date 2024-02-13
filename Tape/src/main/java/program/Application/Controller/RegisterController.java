package program.Application.Controller;

import program.Domain.Model.RegisterModel;
import program.UI.Swing.RegisterPanel;


public class RegisterController {
    private final RegisterPanel panel;
    private final RegisterModel model;
    public RegisterController(RegisterPanel registerPanel, RegisterModel registerModel) {
        this.panel=registerPanel;
        this.model=registerModel;
    }

    public void registerAUser() {
        String username=panel.getUsernameField().getText();
        String pass=panel.getPasswordField().getText();
        model.registerAUser(username, pass);
    }
    public void userAlreadyExists() {
        panel.showUserAlreadyExistsError();
    }

    public void mandatoryField() {
        panel.showMandatoryFieldError();
    }

    public void registrationComplete() {
        panel.showRegistrationComplete();
    }
}
