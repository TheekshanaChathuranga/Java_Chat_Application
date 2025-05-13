package lk.chat.admin;

import lk.chat.db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CreateChat extends JFrame {
    private JPanel createChatPanel;
    private JTextField textFieldChatName;
    private JButton addButton;
    private JButton resetButton;
    private JTextField textFieldChatDiscription;
    private JButton cHomeButton;
    private JTable viewChatTable;

    private void LoadTable(){

        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from ChatInfo");
        List<ChatInfo> chatInfoList = query.list();
        session.close();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Chat ID");
        model.addColumn("Chat Name");
        model.addColumn("Chat Description");
        for (ChatInfo chatInfo : chatInfoList) {
            model.addRow(new Object[]{chatInfo.getChat_id(), chatInfo.getChatName(), chatInfo.getChatDescription()});
        }
        viewChatTable.setModel(model);
    }

    public CreateChat() {
        super("Create Chat");
        this.setContentPane(createChatPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        LoadTable();
        cHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateChat.this.dispose();
                new Dashboard();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldChatName.setText("");
                textFieldChatDiscription.setText("");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chatName = textFieldChatName.getText();
                String chatDiscription = textFieldChatDiscription.getText();
                if (chatName.isEmpty() || chatDiscription.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
                    ChatInfo chatInfo = new ChatInfo(chatName, chatDiscription);
                    Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                    session.beginTransaction();
                    Query query = session.createQuery("from ChatInfo where chatName = :chatName and chatDescription = :chatDescription");
                    query.setParameter("chatName", chatName);
                    query.setParameter("chatDescription", chatDiscription);
                    List<ChatInfo> chatInfoList = query.list();
                    if(chatInfoList.size() > 0){
                        JOptionPane.showMessageDialog(null, "Chat already exists");
                    }else{
                        session.persist(chatInfo);
                        session.getTransaction().commit();
                        session.close();
                        JOptionPane.showMessageDialog(null, "Chat added successfully");
                    }
                }
                textFieldChatName.setText("");
                textFieldChatDiscription.setText("");
                LoadTable();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldChatName.setText("");
                textFieldChatDiscription.setText("");
                LoadTable();
            }
        });

    }


}
