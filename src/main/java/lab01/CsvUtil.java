package lab01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import Entity.Housing;




public class CsvUtil {

	public CsvUtil() {
	
	}
		public static List<Housing> getHousing(String path) {
			ArrayList<Housing> housingList = new ArrayList<>();
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) 
			{
				String line;
				//讀掉第一列
				bufferedReader.readLine();
			while((line=bufferedReader.readLine())!=null) {
				System.out.println(line);
				
				String[] value=line.split(",");
				Housing house = new Housing();				
				
        
				house.setHousingName(value[1].trim());
				house.setHouseholdCount(parseInteger(value[2]));
				house.setDistrict(value[3].trim());
				house.setAreaSquareMeter(Double.parseDouble(value[4].trim()));
				house.setOrganizer(value[5].trim());
	             
	              if(value[2].isBlank()) {
	            	  
	            	  house.setHouseholdCount((Integer) null);
	            	  
	              } else {
	            	  
	            	  house.setHouseholdCount(
	            			  Integer.parseInt(value[2]));
	              }
	              
	             
			
				housingList.add(house);
			}} catch (IOException e) {
				e.printStackTrace();
			}


			return housingList;
		
		
		
		

	}
		private static Integer parseInteger(String string) {
			// TODO Auto-generated method stub
			return null;
		}

}
