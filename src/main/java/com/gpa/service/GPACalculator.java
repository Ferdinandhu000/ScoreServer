package com.gpa.service;

import com.gpa.model.Course;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class GPACalculator {
    private final List<Course> courses;
    private static final String EXCEL_FILE = "score.xlsx";
    private static final String[] HEADERS = {"课程名称", "学分", "成绩", "是否计入GPA", "学期", "课程类型"};

    public GPACalculator() {
        courses = new ArrayList<>();
        loadFromExcel();
    }

    public void addCourse(Course course) {
        courses.add(course);
        saveToExcel();
    }

    public void removeCourse(int index) {
        if (index >= 0 && index < courses.size()) {
            courses.remove(index);
            saveToExcel();
        }
    }

    public void updateCourse(int index, Course course) {
        if (index >= 0 && index < courses.size()) {
            courses.set(index, course);
            saveToExcel();
        }
    }

    public void toggleCourseSelection(int index) {
        if (index >= 0 && index < courses.size()) {
            Course course = courses.get(index);
            course.setSelected(!course.isSelected());
            saveToExcel();
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    // 按学期分组获取课程
    public Map<String, List<Course>> getCoursesBySemester() {
        return courses.stream()
            .collect(Collectors.groupingBy(Course::getSemester));
    }

    // 按课程类型分组获取课程
    public Map<Course.CourseType, List<Course>> getCoursesByType() {
        return courses.stream()
            .collect(Collectors.groupingBy(Course::getCourseType));
    }

    // 计算加权平均分
    public double calculateAverageScore() {
        double totalWeightedScore = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected()) {
                totalWeightedScore += course.getScore() * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedScore / totalCredits;
    }

    // 计算标准五分制GPA
    public double calculateStandardFiveGPA() {
        double totalWeightedGPA = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected()) {
                double score = course.getScore();
                double gpa;
                
                if (score >= 90) gpa = 5.0;
                else if (score >= 80) gpa = 4.0;
                else if (score >= 70) gpa = 3.0;
                else if (score >= 60) gpa = 2.0;
                else gpa = 1.0;

                totalWeightedGPA += gpa * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedGPA / totalCredits;
    }

    // 计算标准四分制GPA
    public double calculateStandardFourGPA() {
        double totalWeightedGPA = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected()) {
                double score = course.getScore();
                double gpa;
                
                if (score >= 90) gpa = 4.0;
                else if (score >= 80) gpa = 3.0;
                else if (score >= 70) gpa = 2.0;
                else if (score >= 60) gpa = 1.0;
                else gpa = 0.0;

                totalWeightedGPA += gpa * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedGPA / totalCredits;
    }

    // 计算北大四分制GPA
    public double calculatePKUGPA() {
        double totalWeightedGPA = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected()) {
                double score = course.getScore();
                double gpa;
                
                if (score >= 90) gpa = 4.0;
                else if (score >= 85) gpa = 3.7;
                else if (score >= 82) gpa = 3.3;
                else if (score >= 78) gpa = 3.0;
                else if (score >= 75) gpa = 2.7;
                else if (score >= 72) gpa = 2.3;
                else if (score >= 68) gpa = 2.0;
                else if (score >= 64) gpa = 1.5;
                else if (score >= 60) gpa = 1.0;
                else gpa = 0.0;

                totalWeightedGPA += gpa * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedGPA / totalCredits;
    }

    // 计算大工算法GPA
    public double calculateDGUTGPA() {
        double totalWeightedGPA = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected()) {
                double score = course.getScore();
                double gpa = (score - 50) / 10;
                if (gpa < 0) gpa = 0;

                totalWeightedGPA += gpa * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedGPA / totalCredits;
    }

    // 计算专业课程均分
    public double calculateMajorAverageScore() {
        double totalWeightedScore = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected() && course.getCourseType() == Course.CourseType.MAJOR) {
                totalWeightedScore += course.getScore() * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedScore / totalCredits;
    }

    // 计算思政课程均分
    public double calculatePoliticalAverageScore() {
        double totalWeightedScore = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected() && course.getCourseType() == Course.CourseType.POLITICAL) {
                totalWeightedScore += course.getScore() * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedScore / totalCredits;
    }

    // 计算素质课程均分
    public double calculateQualityAverageScore() {
        double totalWeightedScore = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected() && course.getCourseType() == Course.CourseType.QUALITY) {
                totalWeightedScore += course.getScore() * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedScore / totalCredits;
    }

    // 计算通识课程均分
    public double calculateGeneralAverageScore() {
        double totalWeightedScore = 0;
        double totalCredits = 0;

        for (Course course : courses) {
            if (course.isSelected() && course.getCourseType() == Course.CourseType.GENERAL) {
                totalWeightedScore += course.getScore() * course.getCredit();
                totalCredits += course.getCredit();
            }
        }

        return totalCredits == 0 ? 0 : totalWeightedScore / totalCredits;
    }

    private void loadFromExcel() {
        File file = new File(EXCEL_FILE);
        if (!file.exists()) {
            createNewExcelFile();
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    try {
                        String name = getStringCellValue(row.getCell(0));
                        double credit = getNumericCellValue(row.getCell(1));
                        double score = getNumericCellValue(row.getCell(2));
                        boolean selected = getBooleanCellValue(row.getCell(3));
                        String semester = getStringCellValue(row.getCell(4));
                        Course.CourseType type = Course.CourseType.valueOf(getStringCellValue(row.getCell(5)));
                        
                        courses.add(new Course(name, credit, score, selected, semester, type));
                    } catch (Exception e) {
                        System.err.println("Error reading row " + i + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "无法读取课程数据文件：" + e.getMessage(), 
                "文件读取错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0.0;
        cell.setCellType(CellType.NUMERIC);
        return cell.getNumericCellValue();
    }

    private boolean getBooleanCellValue(Cell cell) {
        if (cell == null) return false;
        cell.setCellType(CellType.BOOLEAN);
        return cell.getBooleanCellValue();
    }

    private void saveToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("课程信息");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 创建数据行
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            
            for (int i = 0; i < courses.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Course course = courses.get(i);
                
                Cell nameCell = row.createCell(0);
                nameCell.setCellValue(course.getName());
                nameCell.setCellStyle(dataStyle);
                
                Cell creditCell = row.createCell(1);
                creditCell.setCellValue(course.getCredit());
                creditCell.setCellStyle(dataStyle);
                
                Cell scoreCell = row.createCell(2);
                scoreCell.setCellValue(course.getScore());
                scoreCell.setCellStyle(dataStyle);
                
                Cell selectedCell = row.createCell(3);
                selectedCell.setCellValue(course.isSelected());
                selectedCell.setCellStyle(dataStyle);
                
                Cell semesterCell = row.createCell(4);
                semesterCell.setCellValue(course.getSemester());
                semesterCell.setCellStyle(dataStyle);
                
                Cell typeCell = row.createCell(5);
                typeCell.setCellValue(course.getCourseType().name());
                typeCell.setCellStyle(dataStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(EXCEL_FILE)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "无法保存课程数据：" + e.getMessage(), 
                "保存错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewExcelFile() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("课程信息");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(EXCEL_FILE)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "无法创建课程数据文件：" + e.getMessage(), 
                "文件创建错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 