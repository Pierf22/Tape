package program;

import program.Application.Controller.AddConversationController;
import program.Application.Controller.HomeController;
import program.Application.Controller.LoginController;
import program.Application.Controller.RegisterController;
import program.Domain.*;
import program.Domain.ChangeMaster;
import program.Domain.Model.LoginModel;
import program.Domain.Model.RegisterModel;
import program.UI.Swing.AddConversationPanel;
import program.UI.Swing.HomePanel;
import program.UI.Swing.LoginPanel;
import program.UI.Swing.RegisterPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientThread implements Runnable{
    private User user; //the user who logged in via this thread
    @Override
    public void run() { //upon startup, it creates the login page
        LoginPanel loginPanel=new LoginPanel();
        LoginModel loginModel=new LoginModel();
        LoginController controller=new LoginController(loginPanel, loginModel, this);
        loginModel.addController(controller);
        loginPanel.addController(controller);
        loginPanel.generateUI();
    }
    public void loadRegisterScene() { //upon clicking the register button, it creates the register page
        RegisterPanel registerPanel=new RegisterPanel();
        RegisterModel registerModel=new RegisterModel();
        RegisterController controller=new RegisterController(registerPanel, registerModel);
        registerModel.addController(controller);
        registerPanel.addController(controller);
        registerPanel.generateUI();
    }

    public void loadHomeScreen(User loggeduser) { //upon logging in, it creates the home page
        ChangeMaster.getInstance().addAClient(this, loggeduser);
        HomePanel homePanel=new HomePanel();
        HomeController homeController=new HomeController(homePanel, loggeduser, this);
        loggeduser.addController(homeController);
        homePanel.addController(homeController);
        homePanel.generateUI();
        this.user=loggeduser;
        homePanel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ChangeMaster.getInstance().removeAObserver(loggeduser); //when the user closes the window, it removes the user from the list of observers
            }
        });                                                             //this means he will not receive any notifications from the ChangeMaster
    }

    public void update(Subject subject, Object ob) { //the ChargeMaster contacts the thread and it contacts its observer
        user.update(subject, ob);
    }
    public void updateCreation(Subject subject){
        user.updateCreation(subject);
    }

    public void loadAddConversationScene(User loggedUser) { //upon clicking the add conversation button, it creates the add conversation page
        AddConversationPanel addConversationPanel=new AddConversationPanel();
        AddConversationController addConversationController=new AddConversationController(loggedUser);
        addConversationPanel.addController(addConversationController);
        addConversationPanel.generateUI();

    }
}
