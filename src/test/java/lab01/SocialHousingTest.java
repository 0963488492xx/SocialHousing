package lab01;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Entity.Housing;

class SocialHousingTest {

	@Test
	void housingEntityShouldPersistStandardFieldValues() {
		Housing housing = new Housing();
		housing.setHousingName("中壢社宅");
		housing.setHouseholdCount(120);
		housing.setDistrict("中壢區");
		housing.setAreaSquareMeter(86.5);
		housing.setOrganizer("政府");

		assertEquals("中壢社宅", housing.getHousingName());
		assertEquals(120, housing.getHouseholdCount());
		assertEquals("中壢區", housing.getDistrict());
		assertEquals(86.5, housing.getAreaSquareMeter());
		assertEquals("政府", housing.getOrganizer());
	}

}
