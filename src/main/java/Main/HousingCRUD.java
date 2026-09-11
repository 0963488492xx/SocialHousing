package Main;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import Entity.Housing;

public class HousingCRUD {

    // ===== 共用 Scanner =====
    static Scanner sc = new Scanner(System.in);

    // ===== Hibernate Factory =====
    static SessionFactory factory = new Configuration()
            .configure("SocialHousing.cfg.xml")
            .buildSessionFactory();

    // =======================
    // MAIN
    // =======================
    public static void main(String[] args) {

        while (true) {
            int choice = menu();

            if (choice == 0) {
                System.out.println("系統結束，謝謝使用！");
                break;
            }

            execute(choice);
            System.out.println();
        }

        factory.close();
        sc.close();
    }

    // =======================
    // 主選單
    // =======================
    public static int menu() {

        System.out.println("====== 社會住宅管理系統 ======");
        System.out.println("1. 查詢全部");
        System.out.println("2. 依行政區查詢");
        System.out.println("3. 新增社宅");
        System.out.println("4. 修改戶數");
        System.out.println("5. 刪除社宅");
        System.out.println("6. 統計各區戶數");
        System.out.println("0. 結束");
        System.out.print("請輸入：");

        int choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }

    // =======================
    // 功能分派
    // =======================
    public static void execute(int choice) {

        switch (choice) {
        	case 1 -> findAll();
            case 2 -> findByDistrict();
            case 3 -> create();
            case 4 -> updateHouseholdCount();
            case 5 -> delete();
            case 6 -> countHouseholdByDistrict();
            
            default -> System.out.println("輸入錯誤，請重新輸入");
        }
    }


    // =======================
    // 1. 查詢全部
    // =======================
    public static void findAll() {

        try (Session session = factory.openSession()) {

            List<Housing> list = session
                    .createQuery("from Housing", Housing.class)
                    .list();

            if (list.isEmpty()) {
                System.out.println("目前沒有資料");
                return;
            }

            for (Housing h : list) {
                printHousing(h);
            }
        }
    }

    // =======================
    // 2. 依行政區查詢
    // =======================
    public static void findByDistrict() {

        System.out.print("請輸入行政區，例如 桃園區、中壢區、八德區：");
        String district = sc.nextLine();

        try (Session session = factory.openSession()) {

            List<Housing> list = session
                    .createQuery(
                            "from Housing where district = :district",
                            Housing.class
                    )
                    .setParameter("district", district)
                    .list();

            if (list.isEmpty()) {
                System.out.println("查無此行政區資料");
                return;
            }

            for (Housing h : list) {
                printHousing(h);
            }
        }
    }

    // =======================
    // 3. 新增社宅
    // =======================
    public static void create() {

        Transaction tx = null;

        try (Session session = factory.openSession()) {

            tx = session.beginTransaction();

            Housing h = new Housing();

            System.out.print("名稱：");
            h.setHousingName(sc.nextLine());

            System.out.print("戶數：");
            h.setHouseholdCount(sc.nextInt());
            sc.nextLine();

            System.out.print("行政區：");
            h.setDistrict(sc.nextLine());

            System.out.print("面積：");
            h.setAreaSquareMeter(sc.nextDouble());
            sc.nextLine();

            System.out.print("主辦單位：");
            h.setOrganizer(sc.nextLine());

            session.persist(h);

            tx.commit();

            System.out.println("新增成功");

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("新增失敗：" + e.getMessage());
        }
    }

    // =======================
    // 4. 修改戶數
    // =======================
    public static void updateHouseholdCount() {

        Transaction tx = null;

        try (Session session = factory.openSession()) {

            tx = session.beginTransaction();

            System.out.print("請輸入要修改的 ID：");
            int id = sc.nextInt();

            Housing h = session.find(Housing.class, id);

            if (h != null) {
                System.out.println("目前資料：");
                printHousing(h);

                System.out.print("請輸入新的戶數：");
                int newCount = sc.nextInt();
                sc.nextLine();

                h.setHouseholdCount(newCount);

                session.merge(h);

                tx.commit();

                System.out.println("戶數修改成功");

            } else {
                tx.rollback();
                System.out.println("查無資料");
            }

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("修改失敗：" + e.getMessage());
        }
    }

    // =======================
    // 5. 刪除社宅
    // =======================
    public static void delete() {

        Transaction tx = null;

        try (Session session = factory.openSession()) {

            tx = session.beginTransaction();

            System.out.print("請輸入要刪除的 ID：");
            int id = sc.nextInt();
            sc.nextLine();

            Housing h = session.find(Housing.class, id);

            if (h != null) {
                session.remove(h);
                tx.commit();
                System.out.println("刪除成功");
            } else {
                tx.rollback();
                System.out.println("查無資料");
            }

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("刪除失敗：" + e.getMessage());
        }
    }

    // =======================
    // 6. 統計各區戶數
    // =======================
    public static void countHouseholdByDistrict() {

        try (Session session = factory.openSession()) {

            List<Object[]> result = session
                    .createQuery(
                            "select district, sum(householdCount) " +
                                    "from Housing " +
                                    "group by district",
                            Object[].class
                    )
                    .list();

            if (result.isEmpty()) {
                System.out.println("目前沒有資料可以統計");
                return;
            }

            System.out.println("====== 各行政區戶數統計 ======");

            for (Object[] row : result) {
                String district = (String) row[0];
                Number total = (Number) row[1];

                System.out.println(district + "：" + total.intValue() + " 戶");
            }
        }
    }


    // =======================
    // 共用列印方法
    // =======================
    public static void printHousing(Housing h) {

        System.out.println("ID：" + h.getId());
        System.out.println("名稱：" + h.getHousingName());
        System.out.println("戶數：" + h.getHouseholdCount());
        System.out.println("行政區：" + h.getDistrict());
        System.out.println("面積：" + h.getAreaSquareMeter());
        System.out.println("主辦：" + h.getOrganizer());
        System.out.println("------------------");
    }
}