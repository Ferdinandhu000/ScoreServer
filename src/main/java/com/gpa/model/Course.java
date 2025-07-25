package com.gpa.model;

public class Course {
    private String name;
    private double credit;
    private double score;
    private boolean selected;
    private String semester; // 学期
    private CourseType courseType; // 课程类型
    private boolean isMajorCourse; // 是否是专业课
    private int originalIndex; // 添加原始索引字段

    public enum CourseType {
        MAJOR("专业课程"),
        POLITICAL("思政课程"),
        QUALITY("素质课程"),
        GENERAL("通识课程");

        private final String displayName;

        CourseType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Course(String name, double credit, double score, boolean selected, String semester, CourseType courseType) {
        this.name = name;
        this.credit = credit;
        this.score = score;
        this.selected = selected;
        this.semester = semester;
        this.courseType = courseType;
        this.originalIndex = -1; // 初始化为-1
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }

    public boolean isMajorCourse() { return isMajorCourse; }
    public void setMajorCourse(boolean majorCourse) { isMajorCourse = majorCourse; }

    public int getOriginalIndex() { return originalIndex; }
    public void setOriginalIndex(int originalIndex) { this.originalIndex = originalIndex; }

    // 添加toTableRow方法
    public Object[] toTableRow(int displayIndex) {
        return new Object[]{
            displayIndex + 1,
            name,
            credit,
            score,
            courseType.getDisplayName(),
            semester,
            selected ? "是" : "否"
        };
    }
} 