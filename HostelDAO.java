import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class HostelDAO {

    /**
     * Operation 1: Insert a single student record with duplicate ID handling.
     */
    public boolean insertStudentRecord(int id, String name, String rollNo, String hostelName, String roomNo, double fee) {
        String sql = "INSERT INTO HostelStudent (student_id, student_name, roll_number, hostel_name, room_number, fee) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, rollNo);
            ps.setString(4, hostelName);
            ps.setString(5, roomNo);
            ps.setDouble(6, fee);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[Error] Student ID " + id + " already exists in the database. Duplicate IDs are not allowed.");
            return false;
        } catch (SQLException e) {
            System.out.println("[Database Error] Insertion failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Operation 2: Display students for a specific hostel with fee > 50,000, sorted descending.
     */
    public void displayHighFeeStudentsByHostel(String hostelName) {
        String query = "SELECT student_id, student_name, roll_number, hostel_name, room_number, fee "
                     + "FROM HostelStudent "
                     + "WHERE hostel_name = ? AND fee > 50000 "
                     + "ORDER BY fee DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, hostelName);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                System.out.println("\n-------------------------------------------------------------------------------------------------");
                System.out.printf("%-12s %-22s %-15s %-18s %-12s %-12s%n", 
                                  "Student ID", "Name", "Roll No", "Hostel", "Room No", "Fee (INR)");
                System.out.println("-------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    found = true;
                    System.out.printf("%-12d %-22s %-15s %-18s %-12s ₹%-11.2f%n",
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("roll_number"),
                            rs.getString("hostel_name"),
                            rs.getString("room_number"),
                            rs.getDouble("fee"));
                }

                if (!found) {
                    System.out.println("No students found in '" + hostelName + "' with fee strictly greater than ₹50,000.");
                }
                System.out.println("-------------------------------------------------------------------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("[Database Error] Could not retrieve student list: " + e.getMessage());
        }
    }

    /**
     * Operation 3: Update hostel fee using student ID.
     */
    public void updateStudentFee(int studentId, double newFee) {
        String sql = "UPDATE HostelStudent SET fee = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newFee);
            ps.setInt(2, studentId);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                System.out.println("[Success] Fee updated to ₹" + newFee + " for Student ID: " + studentId);
            } else {
                System.out.println("[Notice] No record found matching Student ID: " + studentId);
            }

        } catch (SQLException e) {
            System.out.println("[Database Error] Failed to update fee: " + e.getMessage());
        }
    }

    /**
     * Operation 4: Search student by ID and update room number.
     */
    public void searchAndUpdateRoom(int studentId, String newRoomNumber) {
        String searchQuery = "SELECT * FROM HostelStudent WHERE student_id = ?";
        String updateQuery = "UPDATE HostelStudent SET room_number = ? WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement searchPs = conn.prepareStatement(searchQuery)) {

            searchPs.setInt(1, studentId);

            try (ResultSet rs = searchPs.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n[Student Found]");
                    System.out.println(" Name        : " + rs.getString("student_name"));
                    System.out.println(" Roll No     : " + rs.getString("roll_number"));
                    System.out.println(" Hostel      : " + rs.getString("hostel_name"));
                    System.out.println(" Current Room: " + rs.getString("room_number"));
                    System.out.println(" Current Fee : ₹" + rs.getDouble("fee"));

                    // Proceed with room update
                    try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                        updatePs.setString(1, newRoomNumber);
                        updatePs.setInt(2, studentId);

                        int updated = updatePs.executeUpdate();
                        if (updated > 0) {
                            System.out.println("[Success] Room number updated to '" + newRoomNumber + "' for Student ID: " + studentId);
                        }
                    }
                } else {
                    System.out.println("[Notice] No student found with ID: " + studentId);
                }
            }

        } catch (SQLException e) {
            System.out.println("[Database Error] Search/Update room operation failed: " + e.getMessage());
        }
    }

    /**
     * Operation 5: Display total hostel fees using SUM() and conditionally delete student if fee == 0.
     */
    public void displayTotalFeesAndDeleteZeroFeeStudent(int studentIdToDelete) {
        String sumSql = "SELECT SUM(fee) AS total_fee_sum FROM HostelStudent";
        String findSql = "SELECT student_id, student_name, fee FROM HostelStudent WHERE student_id = ?";
        String deleteSql = "DELETE FROM HostelStudent WHERE student_id = ? AND fee = 0";

        try (Connection conn = DBConnection.getConnection()) {

            // Step A: Calculate and display aggregate fee using SUM()
            try (PreparedStatement sumPs = conn.prepareStatement(sumSql);
                 ResultSet rsSum = sumPs.executeQuery()) {
                if (rsSum.next()) {
                    double totalFee = rsSum.getDouble("total_fee_sum");
                    System.out.println("\n========================================================");
                    System.out.printf("Total Hostel Fee Collected (All Students): ₹%,.2f%n", totalFee);
                    System.out.println("========================================================");
                }
            }

            // Step B: Check student fee status before attempting deletion
            try (PreparedStatement findPs = conn.prepareStatement(findSql)) {
                findPs.setInt(1, studentIdToDelete);

                try (ResultSet rsStudent = findPs.executeQuery()) {
                    if (!rsStudent.next()) {
                        System.out.println("[Notice] Student with ID " + studentIdToDelete + " does not exist.");
                        return;
                    }

                    String name = rsStudent.getString("student_name");
                    double currentFee = rsStudent.getDouble("fee");

                    if (currentFee > 0.0) {
                        System.out.println("[Action Denied] Cannot delete student '" + name 
                                + "' (ID: " + studentIdToDelete + "). Pending fee balance is ₹" + currentFee + ".");
                        System.out.println("Rule: Only students with a fee balance of ₹0 can be deleted.");
                    } else {
                        // Fee is 0 -> execute deletion
                        try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                            deletePs.setInt(1, studentIdToDelete);
                            int deleted = deletePs.executeUpdate();
                            if (deleted > 0) {
                                System.out.println("[Success] Student '" + name + "' (ID: " + studentIdToDelete 
                                        + ") with ₹0 fee balance was successfully deleted.");
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("[Database Error] Operation failed: " + e.getMessage());
        }
    }
}
