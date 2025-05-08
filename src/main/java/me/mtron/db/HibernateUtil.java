package me.mtron.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static HibernateUtil instance;
    private final SessionFactory sessionFactory;

    private HibernateUtil() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    public static synchronized HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
