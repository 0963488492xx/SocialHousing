package Main;

import java.nio.file.Path;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import Dao.DaoSocial;
import Entity.Housing;
import lab01.CsvUtil;

public class Main {

    public static void main(String[] args) {

        System.out.println("程式開始");

        SessionFactory sessionFactory = new Configuration()
                .configure("SocialHousing.cfg.xml")
                .buildSessionFactory();

        System.out.println("Hibernate 連線建立完成");

        Path csvPath = Path.of("Social Housing.csv");
        System.out.println("準備讀取 CSV：" + csvPath.toAbsolutePath());

        List<Housing> housingList = CsvUtil.getHousing(csvPath.toString());

        if (housingList == null) {
            System.out.println("housingList 是 null，CSV 讀取失敗");
            sessionFactory.close();
            return;
        }

        if (housingList.isEmpty()) {
            System.out.println("housingList 是空的，沒有資料可以匯入");
            sessionFactory.close();
            return;
        }

        System.out.println("CSV 讀取成功，共 " + housingList.size() + " 筆");

        DaoSocial dao = new DaoSocial(sessionFactory);

        for (Housing house : housingList) {
            System.out.println("準備匯入：" + house.getHousingName());
            dao.insert(house);
            System.out.println(house.getHousingName() + " 匯入成功");
        }

        sessionFactory.close();

        System.out.println("全部資料匯入完成");
    }
}