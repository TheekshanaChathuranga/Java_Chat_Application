package lk.chat.admin;

import lk.chat.db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Login extends JFrame {
    private JTextField uLoginUserEmailtextField;
    private JPasswordField uLoginUserPasswprdpasswordField;
    private JButton resetButton;
    private JButton loginButton;
    private JPanel loginPanel;

    public Login(){
        super("Login");
        this.setContentPane(loginPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = uLoginUserEmailtextField.getText();
                String password = uLoginUserPasswprdpasswordField.getText();

                if(email.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }else {
                    Session session = HibernateUtil.getInstance().getSessionFactory().openSession();
                    session.beginTransaction();
                    Query query = session.createQuery("from Adminusers where email = :email");
                    query.setParameter("email", email);
                    List<Adminusers> UserList = query.list();
                    if(UserList.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid User");
                    }else{
                        String pass = UserList.get(0).getPassword();
                        if(password.equals(pass)) {
                            JOptionPane.showMessageDialog(null, "Login Successful");
                            Login.this.dispose();
                            new Dashboard();
                        }else{
                            JOptionPane.showMessageDialog(null, "Invalid Password");
                        }
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uLoginUserEmailtextField.setText("");
                uLoginUserPasswprdpasswordField.setText("");
            }
        });


    }

    public static void main(String[] args) {
        new Login();
    }

}

