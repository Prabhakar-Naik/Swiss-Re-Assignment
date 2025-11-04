package com.swiss.re.swissreassignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author prabhakar, @Date 04-11-2025
 */

public class OrgService {

    public Map<String, Employee> loadFromCsv(String path) throws IOException {
        Map<String, Employee> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // header
            if (line == null) return map;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = splitCsvLine(line);
                if (parts.length < 5) {
                    // pad missing fields
                    parts = Arrays.copyOf(parts, 5);
                }
                String id = parts[0].trim();
                String first = parts[1] == null ? "" : parts[1].trim();
                String last = parts[2] == null ? "" : parts[2].trim();
                double salary = 0;
                try {
                    salary = Double.parseDouble(parts[3].trim());
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid salary on line: " + line);
                }
                String managerId = parts[4] == null ? null : parts[4].trim();
                Employee e = new Employee(id, first, last, salary, managerId);
                map.put(id, e);
            }
        }
        // link managers
        for (Employee e : map.values()) {
            String mgid = e.getManagerId();
            if (mgid != null && !mgid.isEmpty()) {
                Employee m = map.get(mgid);
                if (m == null) {
                    throw new IllegalStateException("Manager with id " + mgid + " not found for employee " + e.getId());
                }
                e.setManager(m);
                m.addDirectReport(e);
            }
        }
        return map;
    }

    /**
     * Minimal CSV splitter that supports simple commas and fields optionally quoted with double-quotes.
     * It does not implement full RFC4180 but is sufficient for the coding challenge inputs.
     */
    private String[] splitCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }

    public List<String> analyzeManagerSalaries(Map<String, Employee> employees) {
        List<String> report = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (e.getDirectReports().isEmpty()) continue;
            double avg = e.getDirectReports().stream().mapToDouble(Employee::getSalary).average().orElse(0);
            double minAllowed = avg * 1.20;
            double maxAllowed = avg * 1.50;
            double sal = e.getSalary();
            if (sal + 1e-9 < minAllowed) {
                double shortfall = minAllowed - sal;
                report.add(String.format("UNDERPAID: %s - salary=%.2f, expected>=%.2f, shortfall=%.2f", e, sal, minAllowed, shortfall));
            } else if (sal - 1e-9 > maxAllowed) {
                double excess = sal - maxAllowed;
                report.add(String.format("OVERPAID: %s - salary=%.2f, expected<=%.2f, excess=%.2f", e, sal, maxAllowed, excess));
            } else {
                // within limits - no message
            }
        }
        return report;
    }

    public List<String> analyzeLongReportingLines(Map<String, Employee> employees, int allowedManagersBetween) {
        List<String> report = new ArrayList<>();
        for (Employee e : employees.values()) {
            int between = countManagersBetweenAndCEO(e);
            if (between > allowedManagersBetween) {
                report.add(String.format("TOO_LONG: %s - managers_between_CEO=%d, exceeds_by=%d", e, between, between - allowedManagersBetween));
            }
        }
        return report;
    }

    // counts the number of managers between employee and CEO (excluding CEO)
    // per README assumption: direct report to CEO -> 0
    public int countManagersBetweenAndCEO(Employee e) {
        int count = 0;
        Employee cur = e.getManager();
        while (cur != null && cur.getManager() != null) {
            count++;
            cur = cur.getManager();
        }
        return count;
    }

}
