package lab01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import Main.ExportData;

class ExportDataCsvTest {

    @Test
    void escapeCsvField_shouldQuoteValuesContainingCommaOrQuote() {
        String value = "A, \"B\"";

        assertEquals("\"A, \"\"B\"\"\"", ExportData.escapeCsvField(value));
    }
}
