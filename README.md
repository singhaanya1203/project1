# KIET Hostel Management System (Java JDBC & MySQL)

A menu-driven Core Java JDBC application for managing student hostel records in MySQL.

---

## 1. Database Specifications
- **Database:** `hostel_db`
- **Table:** `HostelStudent`
- **Columns:**
  - `student_id INT PRIMARY KEY`
  - `student_name VARCHAR(100)`
  - `roll_number VARCHAR(20)`
  - `hostel_name VARCHAR(50)`
  - `room_number VARCHAR(20)`
  - `fee DOUBLE`

---

## 2. Project Architecture
The project is organized into modular classes:
- **`schema.sql`**: SQL statements to create the database and table.
- **`DBConnection.java`**: Manages the MySQL JDBC connection with credentials and driver registration.
- **`HostelDAO.java`**: Implements all 5 database operations using `PreparedStatement` and SQL aggregate queries.
- **`HostelManagementSystem.java`**: The interactive menu-driven console interface.

---

## 3. Implemented Operations
1. **Insert Student Records**: Uses `PreparedStatement` to insert student records (prompts for at least 3 records) and handles duplicate IDs via `SQLIntegrityConstraintViolationException`.
2. **Filter by Hostel & Fee**: Accepts a hostel name and displays students with fees exceeding ₹50,000, ordered by fee descending (`ORDER BY fee DESC`).
3. **Update Fee**: Updates a student's hostel fee given their `student_id`.
4. **Search & Update Room**: Searches for a student by `student_id`, displays their existing record, and updates their `room_number`.
5. **Fee Aggregate & Conditional Deletion**:
   - Calculates and displays total fees collected across all students using SQL `SUM(fee)`.
   - Checks the target student's fee: if the fee is `0`, deletes the record; if fee is greater than `0`, prevents deletion.

---

## 4. Setup and Execution Guide

### Step 1: Set up MySQL Database
Open your MySQL terminal or MySQL Workbench and run:
```sql
source schema.sql;
```
Or execute the queries inside [schema.sql](file:///c:/Users/hp/OneDrive/Desktop/project1/schema.sql).

### Step 2: Configure Credentials
Open [DBConnection.java](file:///c:/Users/hp/OneDrive/Desktop/project1/DBConnection.java) and set your MySQL username and password:
```java
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### Step 3: Compile and Run
Ensure you have the MySQL Connector/J driver `.jar` file (e.g., `mysql-connector-j-8.x.x.jar`) placed in the folder or referenced in your classpath.

**Compile:**
```bash
javac -cp ".;mysql-connector-j.jar" *.java
```

**Run:**
```bash
java -cp ".;mysql-connector-j.jar" HostelManagementSystem
```
*(On Linux/macOS, replace `;` with `:` in the classpath flag)*
