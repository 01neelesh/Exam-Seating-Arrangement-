package org.seating;

import java.io.*;
import java.util.*;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ExamSeatingArrangement {
    public static void main(String[] args) {
        String studentExcel = "students.xlsx";
        String roomExcel = "rooms.xlsx";
        List<Student> students = readStudentsFromExcel(studentExcel);
        List<Room> rooms = readRoomsFromExcel(roomExcel);

        Map<Room, List<Student>> roomAllocation = allocateRooms(students, rooms);

        try {
            generatePDF(roomAllocation, "SeatingArrangement.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read students from Excel
    public static List<Student> readStudentsFromExcel(String fileName) {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            rows.next(); // Skip header row

            while (rows.hasNext()) {
                Row row = rows.next();
                String rollNumber = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String studentClass = getCellValueAsString(row.getCell(2));

                students.add(new Student(rollNumber, name, studentClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Method to read rooms from Excel
    public static List<Room> readRoomsFromExcel(String fileName) {
        List<Room> rooms = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            rows.next(); // Skip header row

            while (rows.hasNext()) {
                Row row = rows.next();
                String roomID = getCellValueAsString(row.getCell(0));
                int capacity = Integer.parseInt(getCellValueAsString(row.getCell(1)));

                rooms.add(new Room(roomID, capacity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // Helper method to get cell value as String
    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Handle date values if needed
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // The allocateRooms and generatePDF methods remain the same as before
    // ...

    // Method to allocate students to rooms
    public static Map<Room, List<Student>> allocateRooms(List<Student> students, List<Room> rooms) {
        Map<Room, List<Student>> allocation = new LinkedHashMap<>();
        int studentIndex = 0;

        for (Room room : rooms) {
            List<Student> assignedStudents = new ArrayList<>();
            for (int i = 0; i < room.getCapacity() && studentIndex < students.size(); i++) {
                assignedStudents.add(students.get(studentIndex++));
            }
            allocation.put(room, assignedStudents);
        }

        if (studentIndex < students.size()) {
            System.out.println("Warning: Not enough room capacity for all students.");
        }

        return allocation;
    }

    // Method to generate seating arrangement PDF using iText
    public static void generatePDF(Map<Room, List<Student>> allocation, String fileName) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        for (Map.Entry<Room, List<Student>> entry : allocation.entrySet()) {
            Room room = entry.getKey();
            List<Student> students = entry.getValue();

            document.add(new Paragraph("Room: " + room.getRoomID()));
            PdfPTable table = new PdfPTable(3); // Roll Number, Name, Class

            table.addCell("Roll Number");
            table.addCell("Name");
            table.addCell("Class");

            for (Student student : students) {
                table.addCell(student.getRollNumber());
                table.addCell(student.getName());
                table.addCell(student.getStudentClass());
            }

            document.add(table);
            document.add(Chunk.NEWLINE);
        }

        document.close();
        System.out.println("PDF generated: " + fileName);
    }


}
