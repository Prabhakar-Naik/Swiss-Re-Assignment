package com.swiss.re.swissreassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class SwissReAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwissReAssignmentApplication.class, args);
        System.out.println("SwissReAssignmentApplication started...@");

        if (args.length == 0) {
            System.out.println("Usage: java -jar org-analyzer.jar path/to/employees.csv");
            System.out.println("Using sample.csv included in project for demo. Pass a path to use a custom file.");
            args = new String[] {"sample.csv"};
        }
        String path = args[0];
        OrgService svc = new OrgService();
        try {
            Map<String, Employee> map = svc.loadFromCsv(path);
            System.out.println("Loaded " + map.size() + " employees.");
            System.out.println("\n=== Salary checks (managers outside 20%-50% window) ===");
            List<String> salReport = svc.analyzeManagerSalaries(map);
            if (salReport.isEmpty()) {
                System.out.println("All managers are within the acceptable range.");
            } else {
                salReport.forEach(System.out::println);
            }

            System.out.println("\n=== Reporting-line length checks (more than 4 managers between employee and CEO) ===");
            List<String> longReport = svc.analyzeLongReportingLines(map, 4);
            if (longReport.isEmpty()) {
                System.out.println("No reporting lines exceed the allowed length.");
            } else {
                longReport.forEach(System.out::println);
            }

        } catch (IOException ex) {
            System.err.println("Failed to read file: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @GetMapping("/myData/{input}")
    public Map<?, ?> getData(@PathVariable Object input){
        return Map.of("API Response: ", "Success", "Your Input", input);
    }

}
