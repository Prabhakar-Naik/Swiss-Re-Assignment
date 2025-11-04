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

This small project and its tests were designed to demonstrate the following evaluation criteria requested in the coding challenge:

1. **Approach to writing testable code**
   - Core logic is encapsulated in `OrgService` (parsing + analysis) and tested with unit tests in `src/test/java`.
   - Tests are small and deterministic (no file-system / network side effects beyond reading a controlled CSV).
   - Methods are focused and return data structures (lists of issue strings) that are easy to assert in tests.

2. **Ability to write simple code**
   - Implementation favors clarity and single responsibility: `Employee` is the data model; `OrgService` handles CSV-loading and analysis.
   - The CSV parser is intentionally minimal and readable (supports quoted fields) — no over-engineering or unnecessary dependencies.

3. **Ability to understand the requirements**
   - Implemented requirements:
     - Detect managers who earn **less than** 20% above the average salary of their direct reports (reported as **UNDERPAID**).
     - Detect managers who earn **more than** 50% above the average salary of their direct reports (reported as **OVERPAID**).
     - Detect employees that have **too long reporting lines** (more than 4 managers between them and the CEO).
   - Important assumptions (documented here and in README):
     - CSV header: `Id,firstName,lastName,salary,managerId`.
     - Empty `managerId` means the employee is the CEO.
     - Salary is parsed as a decimal (double).
     - "Managers between them and the CEO" excludes the CEO. A direct report to the CEO has `0` managers between them and the CEO.

4. **Ability to abstract the requirements into a solution**
   - `OrgService` exposes focused methods:
     - `loadFromCsv(path)` — builds the employee graph.
     - `analyzeManagerSalaries(employees)` — returns salary-related issues.
     - `analyzeLongReportingLines(employees, allowedManagersBetween)` — returns long-reporting-line issues.
   - This separation means the analysis functions can be reused independently of input source (swap CSV for DB/JSON later).

5. **Ability to write code that is extensible / reusable**
   - Threshold values (20% / 50% and reporting-line maximum) are centralized and can be made configurable.
   - CSV parsing is isolated and can be replaced by a full CSV library with minimal changes.
   - `OrgService` is designed so it can be integrated in a larger application or exposed behind an API.


### How to build & run (local)

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
Run tests:
```
./mvnw test 
```

