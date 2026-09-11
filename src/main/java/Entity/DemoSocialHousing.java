package Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SocialHousing")
public class DemoSocialHousing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "csvSeq")
	private Integer csvSeq;
	@Column(name = "housingName")
	private String housingName;
	@Column(name = "householdCount")
	private Integer householdCount;
	@Column(name = "district")
	private String district;
	@Column(name = "areaSize")
	private Double areaSize;
	@Column(name = "organizer")
	private String organizer;
	@Column(name = "importTime")
	private LocalDateTime importTime;

	public DemoSocialHousing() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCsvSeq() {
		return csvSeq;
	}

	public void setCsvSeq(Integer csvSeq) {
		this.csvSeq = csvSeq;
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

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public LocalDateTime getImportTime() {
		return importTime;
	}

	public void setImportTime(LocalDateTime importTime) {
		this.importTime = importTime;
	}

	@Override
	public String toString() {
		return "SocialUsers [id=" + id + ", csvSeq=" + csvSeq + ", housingName=" + housingName + ", householdCount="
				+ householdCount + ", district=" + district + ", areaSize=" + areaSize + ", organizer=" + organizer
				+ ", importTime=" + importTime + "]";
	}

}
