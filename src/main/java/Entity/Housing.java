package Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "social_housing")
public class Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "housing_name")
    private String housingName;

    @Column(name = "household_count")//UNIT
    private Integer householdCount;

    @Column(name = "district")
    private String district;

    @Column(name = "area_square_meter")//AREA
    private Double areaSquareMeter;

    @Column(name = "organizer")
    private String organizer;

    public Housing() {
    }

    public Integer getId() {
        return id;
    }



    public String getHousingName() {
        return housingName;
    }

    public void setHousingName(String housingName) {
        this.housingName = housingName;
    }

    public Integer getHouseholdCount() {
        return householdCount;
    }

    public void setHouseholdCount(Integer householdCount) {
        this.householdCount = householdCount;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getAreaSquareMeter() {
        return areaSquareMeter;
    }

  
    public void setId(Integer id) {
		this.id = id;
	}

	public void setAreaSquareMeter(Double areaSquareMeter) {
		this.areaSquareMeter = areaSquareMeter;
	}

	public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

	public static List<Housing> searchByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setArea(double d) {
		// TODO Auto-generated method stub
		
	}



}