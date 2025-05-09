package lk.chat.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Profile extends JFrame {
    private JPanel Profile;
    private JButton updateDetailsBtn;
    private JLabel userNicknameLabel;
    private JLabel userEmailLabel;
    private JLabel userImageLabel;
    private JButton cHomeButton;
    private String userEmail;
    private String userNickName;
    private String userProPic;


    public Profile(String email, String nickname, String user_image) {
        super("Dashboard");

        this.userEmail = email;
        this.userNickName = nickname;
        this.userProPic = user_image;

        this.setContentPane(Profile);
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

        updateDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateData(userEmail, userNickName, userProPic);
                Profile.this.dispose();
            }
        });
        cHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Profile.this.dispose();
                new Dashboard(userEmail, userNickName, userProPic);
            }
        });
    }
}
