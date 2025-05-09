package lk.chat.user;

import lk.chat.db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserChatUnsubscribe extends JFrame{
    private JTextField uUChatIDtextField;
    private JButton resetButton;
    private JButton unsubscribeButton;
    private JLabel tableHeadingJLable;
    private JTable uSubscribeUserTable;
    private JPanel unsubscribePanel;
    private JButton uHomeButton;
    private String email;
    private String nickname;
    private String user_image;

    private void SubscribeUserTable(){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("select s.chatId, c.chatName from SubscribeuserEntity s, ChatInfo c, User u where s.chatId = c.chat_id and s.userId = u.user_id and u.email = :email");
        query.setParameter("email", email);
        List<Object[]> rows = query.list();
        session.close();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Chat ID");
        model.addColumn("Chat Name");
        for (Object[] row : rows) {
            model.addRow(row);
        }
        uSubscribeUserTable.setModel(model);
    }

    public UserChatUnsubscribe(String email, String nickname, String user_image) {
        super("Unsubscribe Users from Chat");
        this.setContentPane(unsubscribePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.email = email;
        this.nickname = nickname;
        this.user_image = user_image;

        SubscribeUserTable();
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uUChatIDtextField.setText("");
                SubscribeUserTable();
            }
        });
        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(uUChatIDtextField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter Chat ID");
                    return;
                }else{
                Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                session.beginTransaction();
                Query query = session.createQuery("delete from SubscribeuserEntity where chatId = :chatId and userId = (select user_id from User where email = :email)");
                query.setParameter("chatId", Integer.parseInt(uUChatIDtextField.getText()));
                query.setParameter("email", email);
                query.executeUpdate();
                session.getTransaction().commit();
                session.close();
                JOptionPane.showMessageDialog(null, "Unsubscribed Successfully");
                SubscribeUserTable();
                }
            }
        });
        uHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserChatUnsubscribe.this.dispose();
                new Dashboard(email, nickname, user_image);
            }
        });
    }
}
