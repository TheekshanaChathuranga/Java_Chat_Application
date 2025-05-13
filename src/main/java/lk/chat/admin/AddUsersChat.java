package lk.chat.admin;

import lk.chat.db.HibernateUtil;
import lk.chat.user.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddUsersChat extends JFrame {
    private JPanel subscribeUserPanel;
    private JButton sHomeButton;
    private JTextField sSUNTextField;
    private JButton sSUSearchButton;
    private JButton sSCResetButton;
    private JButton sSCSearchButton;
    private JTextField sSCNTextField;
    private JTextField sSUUserEmailtextField;
    private JTextField sSUChatIDtextField;
    private JButton resetButton;
    private JButton subscribeButton;
    private JButton sSUResetButton;
    private JTable sUserTable;
    private JLabel tableHeadingJLable;

    private void SubscribeUserTable(){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("select s.chatId, c.chatName, s.userId, u.userName, u.email from SubscribeuserEntity s, ChatInfo c, User u where s.chatId = c.chat_id and s.userId = u.user_id");
        List<Object[]> rows = query.list();
        session.close();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Chat ID");
        model.addColumn("Chat Name");
        model.addColumn("User ID");
        model.addColumn("User Name");
        model.addColumn("User Email");
        for (Object[] row : rows) {
            model.addRow(row);
        }
        tableHeadingJLable.setText("Subscribed Users");
        sUserTable.setModel(model);
    }

    private void SearchUserTable(String email){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where email = :email");
        query.setParameter("email", email);
        List<User> UserList = query.list();
        Query query1 = session.createQuery("from User");
        List<User> UserListAll = query1.list();
        session.close();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("User ID");
        model.addColumn("User Email");
        model.addColumn("User Name");
        model.addColumn("User Nickname");
        if(UserList.isEmpty()){
            JOptionPane.showMessageDialog(null, "User Not Found, Loading All Users");
            for (User userInfo : UserListAll) {
                model.addRow(new Object[]{userInfo.getUser_id(), userInfo.getEmail(), userInfo.getUserName(), userInfo.getNickname()});
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "User Found");
            for (User userInfo : UserList) {
                model.addRow(new Object[]{userInfo.getUser_id(), userInfo.getEmail(), userInfo.getUserName(), userInfo.getNickname()});
            }
            sSUUserEmailtextField.setText(email);
        }
        tableHeadingJLable.setText("Users Table");
        sUserTable.setModel(model);
    }

    private void SearchChatTable(String chatName){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from ChatInfo where chatName = :chatName");
        query.setParameter("chatName", chatName);
        List<ChatInfo> chatInfoList = query.list();
        Query query1 = session.createQuery("from ChatInfo");
        List<ChatInfo> chatInfoListAll = query1.list();
        session.close();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Chat ID");
        model.addColumn("Chat Name");
        model.addColumn("Chat Description");
        if(chatInfoList.isEmpty()){
            JOptionPane.showMessageDialog(null, "Chat Not Found, Loading All Chats");
            for (ChatInfo chatInfo : chatInfoListAll) {
                model.addRow(new Object[]{chatInfo.getChat_id(), chatInfo.getChatName(), chatInfo.getChatDescription()});
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Chat Found");
            for (ChatInfo chatInfo : chatInfoList) {
                model.addRow(new Object[]{chatInfo.getChat_id(), chatInfo.getChatName(), chatInfo.getChatDescription()});
            }
            sSUChatIDtextField.setText(String.valueOf(chatInfoList.get(0).getChat_id()));
        }
        tableHeadingJLable.setText("Chats Table");
        sUserTable.setModel(model);
    }

    public AddUsersChat() {
        super("Add Users to Chat");
        this.setContentPane(subscribeUserPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        SubscribeUserTable();
        sHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUsersChat.this.dispose();
                new Dashboard();
            }
        });
        sSUSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = sSUNTextField.getText();
                if(sSUNTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please fill the field");
                else {
                    SearchUserTable(email);
                }
            }
        });
        sSCSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = sSCNTextField.getText();
                if(sSCNTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please fill the field");
                else {
                    SearchChatTable(chatName);
                }
            }
        });
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chatID = Integer.parseInt(sSUChatIDtextField.getText());
                String userEmail = sSUUserEmailtextField.getText();
                boolean userExists = false;

                Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                session.beginTransaction();

                //check user exists
                Query query = session.createQuery("from User where email = :email");
                query.setParameter("email", userEmail);
                List<User> UserList = query.list();

                if(UserList.isEmpty()) {
                    userExists = false;
                }else {
                    int userID = UserList.get(0).getUser_id();
                    //check user already subscribed to chat
                    Query query2 = session.createQuery("from SubscribeuserEntity where userId = :userId and chatId = :chatId");
                    query2.setParameter("userId", userID);
                    query2.setParameter("chatId", chatID);
                    List<SubscribeuserEntity> SubscribeuserEntityList = query2.list();
                    if(SubscribeuserEntityList.isEmpty()){
                        userExists = false;
                    }else{
                        userExists = true;
                    }
                }
                //check chat exists
                Query query1 = session.createQuery("from ChatInfo where chat_id = :chat_id");
                query1.setParameter("chat_id", chatID);
                List<ChatInfo> ChatInfoList = query1.list();


                if(sSUChatIDtextField.getText().isEmpty() || sSUUserEmailtextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }else if(UserList.isEmpty()){
                    JOptionPane.showMessageDialog(null, "User not found");
                }else if(ChatInfoList.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Chat not found");
                }else if(userExists){
                    JOptionPane.showMessageDialog(null, "User already subscribed to chat");
                }else{
                    int userID = UserList.get(0).getUser_id();
                    SubscribeuserEntity subscribeuserEntity = new SubscribeuserEntity();
                    subscribeuserEntity.setChatId(Integer.parseInt(sSUChatIDtextField.getText()));
                    subscribeuserEntity.setUserId(userID);
                    session.save(subscribeuserEntity);
                    session.getTransaction().commit();
                    SubscribeUserTable();
                    sSUChatIDtextField.setText("");
                    sSUUserEmailtextField.setText("");
                    JOptionPane.showMessageDialog(null, "User subscribed to chat");
                }
                session.close();
            }
        });
        sSUResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sSUNTextField.setText("");
                SubscribeUserTable();
            }
        });
        sSCResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sSCNTextField.setText("");
                SubscribeUserTable();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sSUChatIDtextField.setText("");
                sSUUserEmailtextField.setText("");
                SubscribeUserTable();
            }
        });
    }
}
