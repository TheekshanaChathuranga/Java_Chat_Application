package lk.chat.user;

import lk.chat.client.ChatArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel Dashboard;
    private JButton chatAreaButton;
    private JButton userProfileBtn;
    private JButton logoutButton;
    private JLabel userImageLabel;
    private JLabel userNicknameLabel;
    private JLabel userEmailLabel;
    private JButton subscribeToChatsButton;
    private JButton unsubsribeFromChatsButton;
    private String userEmail;
    private String userNickName;
    private String userProPic;

    public Dashboard(String email, String nickname, String user_image) {
        super("Dashboard");

        this.userEmail = email;
        this.userNickName = nickname;
        this.userProPic = user_image;

        this.setContentPane(Dashboard);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1280, 720));
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

        ImageIcon originalIcon = new ImageIcon(userProPic);
        Image originalImage = originalIcon.getImage();
        ImageIcon icon = new ImageIcon(originalImage.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH));

        this.userImageLabel.setIcon(icon);
        this.userNicknameLabel.setText("Hi, " + userNickName + "!");
        this.userEmailLabel.setText(userEmail);

        this.setVisible(true);
        chatAreaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChatArea(userEmail, userNickName, userProPic);
                Dashboard.this.dispose();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login(null);
                Dashboard.this.dispose();
            }
        });
        userProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Profile(userEmail, userNickName, userProPic);
                Dashboard.this.dispose();
            }
        });
        subscribeToChatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserChatSubscribe(userEmail, userNickName, userProPic);
                Dashboard.this.dispose();
            }
        });
        unsubsribeFromChatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserChatUnsubscribe(userEmail, userNickName, userProPic);
                Dashboard.this.dispose();
            }
        });
    }
}
