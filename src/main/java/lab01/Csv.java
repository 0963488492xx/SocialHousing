package lab01;

import java.io.BufferedReader;
import java.io.FileReader;

public class Csv {
	public static void main(String[] args ) {
		
	try {
		
//家裡電腦放的檔案位置	
//		BufferedReader br =	new BufferedReader(	new FileReader("C:\\Java\\SocialHousing.csv"));
//學校電腦放的檔案位置	
		BufferedReader br =	new BufferedReader(	new FileReader("D:\\HibernateWorkspace\\SocialHousing\\Social Housing.csv"));

			String line;

			br.readLine(); //略過標題

			while((line=br.readLine())!=null){

			    String[] data = line.split(",");
			    System.out.print(data[0]);
			    System.out.print(data[1]);
			    System.out.print(data[2]);
			    System.out.print(data[3]);
			    System.out.println(data[4]);

			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}