package Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import Entity.Housing;

public class DaoSocial {

    private SessionFactory sessionFactory;

    public DaoSocial(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void insert(Housing house) {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();

            session.persist(house);

            tx.commit();

            System.out.println("新增成功：" + house.getHousingName());

        } catch (Exception e) {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            System.out.println("新增失敗：" + house.getHousingName());
            e.printStackTrace();
        }
    }
}