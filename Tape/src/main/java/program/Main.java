package program;

import javax.swing.*;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

            JFrame mainFrame =new JFrame();
            mainFrame.setLayout(new BorderLayout());
            JButton addButton = new JButton("Add a new client");
            mainFrame.setResizable(false);

            mainFrame.add(addButton, BorderLayout.CENTER);
            mainFrame.setVisible(true);
            mainFrame.setSize(200, 100);
            ExecutorService executorService = Executors.newCachedThreadPool();
            //generates a new client every click
            addButton.addActionListener(e -> executorService.execute(new ClientThread()));


    }}