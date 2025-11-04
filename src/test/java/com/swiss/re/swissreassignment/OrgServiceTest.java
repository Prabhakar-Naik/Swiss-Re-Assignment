package com.swiss.re.swissreassignment;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author prabhakar, @Date 04-11-2025
 */

public class OrgServiceTest {

    @Test
    public void testSampleCsvAnalysis() throws Exception {
        Path tmp = Files.createTempFile("employees", ".csv");
        try (FileWriter fw = new FileWriter(tmp.toFile())) {
            fw.write("Id,firstName,lastName,salary,managerId\n");
            fw.write("1,CEO,One,200000,\n");
            fw.write("2,ManagerA,A,90000,1\n");
            fw.write("3,ManagerB,B,150000,1\n");
            fw.write("4,DevX,X,40000,2\n");
            fw.write("5,DevY,Y,40000,2\n");
            fw.write("6,DevZ,Z,40000,3\n");
        }
        OrgService svc = new OrgService();
        Map<String, Employee> map = svc.loadFromCsv(tmp.toString());
        assertEquals(6, map.size());

        List<String> salReport = svc.analyzeManagerSalaries(map);
        // ManagerA has subordinates with avg 40000 => minAllowed=48000 maxAllowed=60000 -> ManagerA salary 90000 -> OVERPAID
        boolean foundOverpaid = salReport.stream().anyMatch(s -> s.contains("ManagerA") && s.contains("OVERPAID"));
                assertTrue(foundOverpaid, "Expected ManagerA to be reported as overpaid");

                        // Reporting line lengths: DevX -> ManagerA -> CEO => managers between = 1 (ManagerA) => allowed
                        List < String > longReport = svc.analyzeLongReportingLines(map, 0); // set allowed to 0 to filter
        assertFalse(longReport.isEmpty(), "At least one employee should exceed allowed manager count (we set allowed=0)");
    }

    @Test
    public void testCountManagersBetween() throws Exception {
        // build small hierarchy manually
        Employee ceo = new Employee("10","C","E",300000, null);
                Employee m1 = new Employee("11","M1","",120000, "10");
                Employee m2 = new Employee("12","M2","",110000, "11");
                Employee e = new Employee("13","E","",90000, "12");
                // link
                m1.setManager(ceo); ceo.addDirectReport(m1);
        m2.setManager(m1);
        m1.addDirectReport(m2);
        e.setManager(m2);
        m2.addDirectReport(e);
        OrgService svc = new OrgService();
        assertEquals(2, svc.countManagersBetweenAndCEO(e)); // m2 and m1 are between e and CEO
    }

}
