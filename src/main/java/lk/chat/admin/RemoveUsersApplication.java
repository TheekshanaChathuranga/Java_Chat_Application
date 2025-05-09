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

public class RemoveUsersApplication extends JFrame {
    private JButton uHomeButton;
    private JPanel removeUserPanel;
    private JTextField rSUNTextField;
    private JButton rSUSearchButton;
    private JButton rSUResetButton;
    private JTextField rRUETextField;
    private JButton rRUResetButton;
    private JButton rRUDeleteButton;
    private JTable rUUserTable;

    public void UserTable(){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User");
        List<User> UserList = query.list();
        session.close();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("User ID");
        model.addColumn("User Email");
        model.addColumn("User Name");
        model.addColumn("User Nickname");
        for (User userInfo : UserList) {
            model.addRow(new Object[]{userInfo.getUser_id(), userInfo.getEmail(), userInfo.getUserName(), userInfo.getNickname()});
        }

        rUUserTable.setModel(model);
    }

    private void UserSearchTable(String username){
        Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where userName = :username");
        query.setParameter("username", username);
        List<User> UserList = query.list();
        session.close();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("User ID");
        model.addColumn("User Email");
        model.addColumn("User Name");
        model.addColumn("User Nickname");
        if(UserList.isEmpty()){
            JOptionPane.showMessageDialog(null, "No user found with username: " + username);
        }
        else{
            for (User userInfo : UserList) {
                model.addRow(new Object[]{userInfo.getUser_id(), userInfo.getEmail(), userInfo.getUserName(), userInfo.getNickname()});
            }
            rRUETextField.setText(UserList.get(0).getEmail());
            rUUserTable.setModel(model);
        }
    }

    public RemoveUsersApplication() {
        super("Remove Users from Application");
        this.setContentPane(removeUserPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        UserTable();
        uHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveUsersApplication.this.dispose();
                new Dashboard();
            }
        });
        rSUResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rSUNTextField.setText("");
                UserTable();
            }
        });
        rRUResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rRUETextField.setText("");
                UserTable();
            }
        });
        rSUSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = rSUNTextField.getText();
                if(username.equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a username to search");
                }
                else{
                    UserSearchTable(username);
                    rSUNTextField.setText("");
                }
            }
        });
        rRUDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = rRUETextField.getText();
                if(email.equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a email to delete");
                }
                else{
                    Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                    session.beginTransaction();
                    Query query = session.createQuery("delete from User where email = :email");
                    query.setParameter("email", email);
                    int result = query.executeUpdate();
                    session.getTransaction().commit();
                    session.close();
                    if(result == 0){
                        JOptionPane.showMessageDialog(null, "No user found with email : " + email);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "User with email: " + email + " has been deleted");
                        UserTable();
                        rRUETextField.setText("");
                    }
                }
            }
        });
    }
}
