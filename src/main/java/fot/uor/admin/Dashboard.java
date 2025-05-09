package lk.chat.admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {

    private JPanel dashboardPanel;
    private JButton createChatButton;
    private JButton subscribeUsersButton;
    private JButton unsubscribeUsersButton;
    private JButton removeUsersButton;

    public Dashboard() {
        super("Dashboard");
        this.setContentPane(dashboardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        createChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.dispose();
                new CreateChat();
            }
        });

        subscribeUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.dispose();
                new AddUsersChat();
            }
        });

        unsubscribeUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.dispose();
                new RemoveUsersChat();
            }
        });

        removeUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.dispose();
                new RemoveUsersApplication();
            }
        });


    }

    public static void main(String[] args) {
        new Dashboard().setVisible(true);
    }
}