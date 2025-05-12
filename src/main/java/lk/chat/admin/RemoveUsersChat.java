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

public class RemoveUsersChat extends JFrame {
    private JPanel unsubscribeUserPanel;
    private JButton uHomeButton;
    private JButton unsubscribeButton;
    private JButton resetButton;
    private JTextField uUChatIDtextField;
    private JTextField uUUserEmailtextField;
    private JTable uSubscribeUserTable;
    private JTextField uSUNTextField;
    private JTextField uSCNTextField;
    private JButton uSUSearchButton;
    private JButton uSUResetButton;
    private JButton uSCResetButton;
    private JButton uSCSearchButton;
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
        uSubscribeUserTable.setModel(model);
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
            uUUserEmailtextField.setText(email);
        }
        tableHeadingJLable.setText("Users Table");
        uSubscribeUserTable.setModel(model);
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
            uUChatIDtextField.setText(String.valueOf(chatInfoList.get(0).getChat_id()));
        }
        tableHeadingJLable.setText("Chats Table");
        uSubscribeUserTable.setModel(model);
    }

    public RemoveUsersChat() {
        super("Unsubscribe Users from Chat");
        this.setContentPane(unsubscribeUserPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        SubscribeUserTable();
        uHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveUsersChat.this.dispose();
                new Dashboard();
            }
        });
        uSUSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = uSUNTextField.getText();
                if(uSUNTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please fill the field");
                else {
                    SearchUserTable(email);
                }
            }
        });
        uSUResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uSUNTextField.setText("");
                SubscribeUserTable();
            }
        });
        uSCSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = uSCNTextField.getText();
                if(uSCNTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please fill the field");
                else {
                    SearchChatTable(chatName);
                }
            }
        });

        uSCResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uSCNTextField.setText("");
                SubscribeUserTable();
            }
        });

        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(uUChatIDtextField.getText().isEmpty() || uUUserEmailtextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please fill the fields");
                else {
                    Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                    session.beginTransaction();
                    Query query = session.createQuery("from User where email = :email");
                    query.setParameter("email", uUUserEmailtextField.getText());
                    List<User> UserList = query.list();
                    Query query1 = session.createQuery("from ChatInfo where chat_id = :chat_id");
                    query1.setParameter("chat_id", Integer.parseInt(uUChatIDtextField.getText()));
                    List<ChatInfo> chatInfoList = query1.list();
                    if(UserList.isEmpty() || chatInfoList.isEmpty())
                        JOptionPane.showMessageDialog(null, "User or Chat Not Found");
                    else{
                        Query query2 = session.createQuery("from SubscribeuserEntity where chatId = :chatId and userId = :userId");
                        query2.setParameter("chatId", Integer.parseInt(uUChatIDtextField.getText()));
                        query2.setParameter("userId", UserList.get(0).getUser_id());
                        List<SubscribeuserEntity> subscribeuserEntityList = query2.list();
                        if(subscribeuserEntityList.isEmpty())
                            JOptionPane.showMessageDialog(null, "User is not subscribed to this chat");
                        else{
                            Query query3 = session.createQuery("delete from SubscribeuserEntity where chatId = :chatId and userId = :userId");
                            query3.setParameter("chatId", Integer.parseInt(uUChatIDtextField.getText()));
                            query3.setParameter("userId", UserList.get(0).getUser_id());
                            query3.executeUpdate();
                            session.getTransaction().commit();
                            session.close();
                            JOptionPane.showMessageDialog(null, "User Unsubscribed");
                            SubscribeUserTable();
                        }
                    }
                }
            }
        });
    }

}
