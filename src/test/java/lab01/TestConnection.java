package lab01;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {

    public static void main(String[] args) {

        String url =
            "jdbc:sqlserver://localhost:1433;databaseName=SocialHousingDB;encrypt=false";

        String user = "sa";

        String password = "你的密碼";

        try {

//            Connection conn =
//                DriverManager.getConnection(url, user, password);
//
//            System.out.println("連線成功");
//
//            conn.close();
            Connection conn =
            		DriverManager.getConnection(
            		"jdbc:sqlserver://localhost:1433;databaseName=SocialHousingDB;encrypt=false",
            		"xiao",
            		"9088");

            		System.out.println("連線成功");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}