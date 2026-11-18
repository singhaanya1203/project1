import java.util.Scanner;

public class HostelManagementSystem {

    public static void main(String[] args) {
        HostelDAO dao = new HostelDAO();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=======================================================");
        System.out.println("   WELCOME TO KIET HOSTEL MANAGEMENT SYSTEM (JDBC)    ");
        System.out.println("=======================================================");

        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. Insert Student Records (at least 3 records)");
            System.out.println("2. View Students by Hostel with Fee > ₹50,000 (Sorted Descending)");
            System.out.println("3. Update Student Fee by ID");
            System.out.println("4. Search Student by ID & Update Room Number");
            System.out.println("5. Display Total Fees [SUM()] & Delete Student (if fee is 0)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            String choiceInput = scanner.nextLine().trim();

            switch (choiceInput) {
                case "1":
                    handleInsertRecords(dao, scanner);
                    break;

                case "2":
                    System.out.print("\nEnter Hostel Name (e.g., Tagore, Aryabhata, Gargi): ");
                    String hostelName = scanner.nextLine().trim();
                    if (!hostelName.isEmpty()) {
                        dao.displayHighFeeStudentsByHostel(hostelName);
                    } else {
                        System.out.println("Hostel name cannot be empty.");
                    }
                    break;

                case "3":
                    handleUpdateFee(dao, scanner);
                    break;

                case "4":
                    handleSearchAndUpdateRoom(dao, scanner);
                    break;

                case "5":
                    handleTotalFeeAndDelete(dao, scanner);
                    break;

                case "6":
                    System.out.println("\nExiting KIET Hostel Management System. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid selection! Please enter a number between 1 and 6.");
            }
        }

        scanner.close();
    }

    private static void handleInsertRecords(HostelDAO dao, Scanner scanner) {
        System.out.println("\n--- Operation 1: Insert Student Records ---");
        System.out.println("How many records would you like to insert? (Enter at least 3 for initial setup): ");
        int count = 0;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }

        if (count < 1) {
            System.out.println("Count must be at least 1.");
            return;
        }

        int successCount = 0;
        for (int i = 1; i <= count; i++) {
            System.out.println("\nEntering details for Student #" + i + " of " + count + ":");
            try {
                System.out.print("Enter Student ID (Integer): ");
                int id = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter Student Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Enter Roll Number: ");
                String rollNo = scanner.nextLine().trim();

                System.out.print("Enter Hostel Name: ");
                String hostel = scanner.nextLine().trim();

                System.out.print("Enter Room Number: ");
                String room = scanner.nextLine().trim();

                System.out.print("Enter Fee (e.g., 65000 or 0): ");
                double fee = Double.parseDouble(scanner.nextLine().trim());

                boolean inserted = dao.insertStudentRecord(id, name, rollNo, hostel, room, fee);
                if (inserted) {
                    System.out.println("[Success] Record for '" + name + "' inserted successfully.");
                    successCount++;
                }

            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Numerical value expected. Skipping this student record.");
            }
        }

        System.out.println("\nCompleted: Successfully inserted " + successCount + " record(s).");
    }

    private static void handleUpdateFee(HostelDAO dao, Scanner scanner) {
        System.out.println("\n--- Operation 3: Update Hostel Fee by Student ID ---");
        try {
            System.out.print("Enter Student ID to update fee: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter New Fee Amount: ");
            double newFee = Double.parseDouble(scanner.nextLine().trim());

            dao.updateStudentFee(id, newFee);

        } catch (NumberFormatException e) {
            System.out.println("[Input Error] Please enter valid numeric values for ID and Fee.");
        }
    }

    private static void handleSearchAndUpdateRoom(HostelDAO dao, Scanner scanner) {
        System.out.println("\n--- Operation 4: Search Student & Update Room Number ---");
        try {
            System.out.print("Enter Student ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter New Room Number to assign: ");
            String newRoom = scanner.nextLine().trim();

            if (newRoom.isEmpty()) {
                System.out.println("Room number cannot be empty.");
                return;
            }

            dao.searchAndUpdateRoom(id, newRoom);

        } catch (NumberFormatException e) {
            System.out.println("[Input Error] Please enter a valid integer Student ID.");
        }
    }

    private static void handleTotalFeeAndDelete(HostelDAO dao, Scanner scanner) {
        System.out.println("\n--- Operation 5: Total Fee [SUM()] & Conditional Delete ---");
        try {
            System.out.print("Enter Student ID to attempt deletion: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            dao.displayTotalFeesAndDeleteZeroFeeStudent(id);

        } catch (NumberFormatException e) {
            System.out.println("[Input Error] Please enter a valid integer Student ID.");
        }
    }
}
