package Main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class CleanSocialHousingColumns {

    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration()
                .configure("SocialHousing.cfg.xml")
                .buildSessionFactory();

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();

            System.out.println("開始清理 social_housing 欄位...");

            dropColumnIfExists(session, "householdCount");
            dropColumnIfExists(session, "housingName");
            dropColumnIfExists(session, "SeqNo");
            dropColumnIfExists(session, "seq_no");

            tx.commit();

            System.out.println("欄位清理完成");

        } catch (Exception e) {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            System.out.println("刪除欄位失敗");
            e.printStackTrace();

        } finally {
            sessionFactory.close();
        }
    }

    private static void dropColumnIfExists(Session session, String columnName) {

        String sql = """
                IF COL_LENGTH('dbo.social_housing', '%s') IS NOT NULL
                BEGIN
                    ALTER TABLE dbo.social_housing DROP COLUMN %s
                END
                """.formatted(columnName, columnName);

        session.createNativeMutationQuery(sql).executeUpdate();

        System.out.println("已檢查並刪除欄位：" + columnName);
    }
}