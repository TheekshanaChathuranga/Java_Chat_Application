package lk.chat.user;

import lk.chat.admin.ChatInfo;
import lk.chat.admin.SubscribeuserEntity;
import lk.chat.db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserChatSubscribe extends JFrame {
    private JLabel tableHeadingJLable;
    private JTable sUserTable;
    private JTextField sSCNTextField;
    private JButton sSCResetButton;
    private JButton sSCSearchButton;
    private JTextField sSUChatIDtextField;
    private JButton resetButton;
    private JButton subscribeButton;
    private JButton sHomeButton;
    private JPanel subscribeUserPanel;
    private String userEmail;
    private String userNickName;
    private String userProPic;

    private void SubscribeUserTable(){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("select s.chatId, c.chatName from SubscribeuserEntity s, ChatInfo c, User u where s.chatId = c.chat_id and s.userId = u.user_id and u.email = :email");
        query.setParameter("email", userEmail);
        List<Object[]> rows = query.list();
        session.close();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Chat ID");
        model.addColumn("Chat Name");
        for (Object[] row : rows) {
            model.addRow(row);
        }
        tableHeadingJLable.setText("Subscribed Chats");
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
    public UserChatSubscribe(String email, String nickname, String user_image) {
        super("Subscribe to Chat");
        this.setContentPane(subscribeUserPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.userEmail = email;
        this.userNickName = nickname;
        this.userProPic = user_image;

        SubscribeUserTable();
        sSCResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sSCNTextField.setText("");
                SubscribeUserTable();
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
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sSUChatIDtextField.setText("");
                SubscribeUserTable();
            }
        });
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chatID = Integer.parseInt(sSUChatIDtextField.getText());
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


                if(sSUChatIDtextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
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
                    JOptionPane.showMessageDialog(null, "User subscribed to chat");
                }
                session.close();
            }
        });
        sHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserChatSubscribe.this.dispose();
                new Dashboard(userEmail, userNickName, userProPic);
            }
        });
    }
}
