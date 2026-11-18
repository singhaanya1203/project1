-- ========================================================
-- KIET Hostel Management System - Database Schema
-- Database: hostel_db
-- Table: HostelStudent
-- ========================================================

CREATE DATABASE IF NOT EXISTS hostel_db;
USE hostel_db;

CREATE TABLE IF NOT EXISTS HostelStudent (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(20) NOT NULL,
    hostel_name VARCHAR(50) NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    fee DOUBLE NOT NULL
);

-- Optional: Verify table creation
DESCRIBE HostelStudent;
