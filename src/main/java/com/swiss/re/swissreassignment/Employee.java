package com.swiss.re.swissreassignment;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author prabhakar, @Date 04-11-2025
 */

public class Employee {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final String managerId;

    // runtime links
//    @Setter // we can use lombok
    private Employee manager;
    private final List<Employee> directReports = new ArrayList<>();

    public Employee(String id, String firstName, String lastName, double salary, String managerId) {
        this.id = id;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
        this.salary = salary;
        this.managerId = (managerId == null || managerId.trim().isEmpty()) ? null : managerId.trim();
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getSalary() { return salary; }
    public String getManagerId() { return managerId; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }
    public List<Employee> getDirectReports() { return directReports; }
    public void addDirectReport(Employee e) { directReports.add(e); }

    @Override
    public String toString() {
        return id + " (" + firstName + " " + lastName + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee emp = (Employee) o;
        return Objects.equals(id, emp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
