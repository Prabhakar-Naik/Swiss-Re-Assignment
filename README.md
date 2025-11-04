# Swiss Re Org Analyzer - Coding Challenge

This Java (Maven) project solves the coding task supplied in the PDF: it reads a CSV with employee records and reports:

* Managers that earn **less than** required (below 20% above average direct-subordinates salary)
* Managers that earn **more than** allowed (above 50% above average direct-subordinates salary)
* Employees that have **too long reporting lines** (more than 4 managers between them and the CEO)

## Assumptions (documented)
* CSV header is: `Id,firstName,lastName,salary,managerId`.
* Empty `managerId` (or missing) means the employee is the CEO.
* Salaries are parsed as decimals (double) and comparisons done in double precision.
* Definition of *"managers between them and the CEO"*:
    - We **count managers excluding the CEO**.
    - Example: If an employee reports directly to the CEO, they have 0 managers between them and the CEO.
    - If an employee's chain is: Employee -> M1 -> M2 -> CEO, then number of managers between employee and CEO = 2 (M1 and M2).
* We report amounts (shortfall or excess) in the same currency units as the input salaries (no rounding beyond two decimals in output).

## How to run
Build with Maven: 
if you are using windows :
```
mvn clean install
```

you can find the .jar file like
SwissReAssignmentApplication.jar

run the application:
```
./mvnw spring-boot:run
```


