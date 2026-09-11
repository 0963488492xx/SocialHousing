package Main;

import Util.HibernateUtil;
import org.hibernate.Session;

public class TestHibernate {

    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("Hibernate 連線 SQL Server 成功！");
        } catch (Exception e) {
            System.out.println("Hibernate 連線 SQL Server 失敗！");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}