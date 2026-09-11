package Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import Entity.Housing;
import Util.HibernateUtil;

public class ExportData {

    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Housing> houseList = session
                    .createQuery("from Housing", Housing.class)
                    .list();

            System.out.println("查詢資料筆數：" + houseList.size());

            export(houseList);

        } finally {
            HibernateUtil.shutdown();
        }
    }

    public static void export(List<Housing> houseList) {

        if (houseList == null || houseList.isEmpty()) {
            System.out.println("沒有資料可以匯出");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SocialHousing_New.csv"))) {

            writer.write("ID,社宅名稱,戶數,行政區,面積(平方公尺),主辦單位");
            writer.newLine();

            for (Housing house : houseList) {
                writer.write(String.join(",",
                        String.valueOf(house.getId()),
                        escapeCsvField(house.getHousingName()),
                        String.valueOf(house.getHouseholdCount()),
                        escapeCsvField(house.getDistrict()),
                        String.valueOf(house.getAreaSquareMeter()),
                        escapeCsvField(house.getOrganizer())
                ));
                writer.newLine();
            }

            System.out.println("CSV 匯出成功：SocialHousing_New.csv");

        } catch (IOException e) {
            System.out.println("CSV 匯出失敗");
            e.printStackTrace();
        }
    }

    public static String escapeCsvField(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}