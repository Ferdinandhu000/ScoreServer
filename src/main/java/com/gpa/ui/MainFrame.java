package com.gpa.ui;

import com.gpa.model.Course;
import com.gpa.service.GPACalculator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Map;

public class MainFrame extends JFrame {
    private final GPACalculator calculator;
    private final JTable courseTable;
    private final DefaultTableModel tableModel;
    private final JPanel chartPanel;
    private final JLabel averageScoreLabel;
    private final JLabel standardFiveGPALabel;
    private final JLabel standardFourGPALabel;
    private final JLabel pkuGPALabel;
    private final JLabel dgutGPALabel;
    private final JLabel majorAverageLabel;
    private final JLabel politicalAverageLabel;
    private final JLabel qualityAverageLabel;
    private final JLabel generalAverageLabel;
    private JTextField searchField;
    private JComboBox<String> semesterFilter;
    private JComboBox<Course.CourseType> typeFilter;
    private final java.util.List<String> semesters = new ArrayList<>();

    public MainFrame() {
        calculator = new GPACalculator();

        // 设置窗口
        setTitle("大学成绩管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建左侧面板（课程表格和按钮）
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setPreferredSize(new Dimension(600, getHeight()));

        // 添加搜索和筛选面板
        JPanel searchPanel = createSearchPanel();
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // 创建表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "课程列表", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Microsoft YaHei", Font.BOLD, 14)
        ));

        // 创建表格
        String[] columnNames = {"序号", "课程名称", "学分", "成绩", "课程类型", "学期", "是否计入GPA"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3) {
                    return Double.class;
                }
                return Object.class;
            }
        };
        
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        courseTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
        courseTable.setRowHeight(25);
        
        // 设置表格列宽
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // 序号
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(200); // 课程名称
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 学分
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // 成绩
        courseTable.getColumnModel().getColumn(4).setPreferredWidth(100); // 课程类型
        courseTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // 学期
        courseTable.getColumnModel().getColumn(6).setPreferredWidth(100); // 是否计入GPA
        
        // 设置表格渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < courseTable.getColumnCount(); i++) {
            courseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // 设置表格选择颜色
        courseTable.setSelectionBackground(new Color(135, 206, 250));
        courseTable.setSelectionForeground(Color.BLACK);
        
        // 修改右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("修改课程信息");
        JMenuItem toggleItem = new JMenuItem("切换选择状态");
        JMenuItem deleteItem = new JMenuItem("删除课程");
        
        editItem.addActionListener(e -> editSelectedCourse());
        toggleItem.addActionListener(e -> toggleSelectedCourse());
        deleteItem.addActionListener(e -> {
            if (showDeleteConfirmDialog()) {
                deleteSelectedCourse();
            }
        });
        
        popupMenu.add(editItem);
        popupMenu.add(toggleItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteItem);
        
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = courseTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        courseTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        leftPanel.add(tablePanel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JButton addButton = new JButton("添加课程");
        addButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        addButton.setPreferredSize(new Dimension(100, 30));
        addButton.addActionListener(this::showAddCourseDialog);
        buttonPanel.add(addButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建右侧面板（GPA信息和均分信息）
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // 创建GPA信息面板
        JPanel gpaInfoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        gpaInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "GPA信息",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Microsoft YaHei", Font.BOLD, 14)
        ));

        averageScoreLabel = new JLabel("总平均分：0.00");
        standardFiveGPALabel = new JLabel("标准五分制GPA：0.00");
        standardFourGPALabel = new JLabel("标准四分制GPA：0.00");
        pkuGPALabel = new JLabel("北大算法GPA：0.00");
        dgutGPALabel = new JLabel("大工算法GPA：0.00");

        gpaInfoPanel.add(averageScoreLabel);
        gpaInfoPanel.add(standardFiveGPALabel);
        gpaInfoPanel.add(standardFourGPALabel);
        gpaInfoPanel.add(pkuGPALabel);
        gpaInfoPanel.add(dgutGPALabel);

        // 创建均分统计面板
        JPanel averagePanel = new JPanel(new GridLayout(4, 1, 5, 5));
        averagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "课程分类统计",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Microsoft YaHei", Font.BOLD, 14)
        ));

        majorAverageLabel = new JLabel("专业课程均分：0.00");
        politicalAverageLabel = new JLabel("思政课程均分：0.00");
        qualityAverageLabel = new JLabel("素质课程均分：0.00");
        generalAverageLabel = new JLabel("通识课程均分：0.00");

        averagePanel.add(majorAverageLabel);
        averagePanel.add(politicalAverageLabel);
        averagePanel.add(qualityAverageLabel);
        averagePanel.add(generalAverageLabel);

        // 创建右侧信息面板容器
        JPanel rightInfoPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        rightInfoPanel.add(gpaInfoPanel);
        rightInfoPanel.add(averagePanel);
        
        rightPanel.add(rightInfoPanel, BorderLayout.NORTH);

        // 图表面板
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "成绩分布",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Microsoft YaHei", Font.BOLD, 14)
        ));
        rightPanel.add(chartPanel, BorderLayout.CENTER);

        // 添加左右面板到主面板
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // 添加主面板到窗口
        add(mainPanel);

        // 初始化数据
        refreshData();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "搜索和筛选",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Microsoft YaHei", Font.BOLD, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 搜索框
        searchField = new JTextField(15);
        searchField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterCourses(); }
            public void removeUpdate(DocumentEvent e) { filterCourses(); }
            public void changedUpdate(DocumentEvent e) { filterCourses(); }
        });

        // 学期筛选
        semesterFilter = new JComboBox<>();
        semesterFilter.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        semesterFilter.addItem("全部学期");
        semesterFilter.addActionListener(e -> filterCourses());

        // 课程类型筛选
        typeFilter = new JComboBox<>(Course.CourseType.values());
        typeFilter.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        typeFilter.insertItemAt(null, 0);
        typeFilter.setSelectedIndex(0);
        typeFilter.addActionListener(e -> filterCourses());

        // 添加组件
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        searchPanel.add(new JLabel("搜索课程："), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        searchPanel.add(searchField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        searchPanel.add(new JLabel("选择学期："), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        searchPanel.add(semesterFilter, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        searchPanel.add(new JLabel("课程类型："), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        searchPanel.add(typeFilter, gbc);

        return searchPanel;
    }

    private void filterCourses() {
        String searchText = searchField.getText().toLowerCase();
        String selectedSemester = (String) semesterFilter.getSelectedItem();
        Course.CourseType selectedType = (Course.CourseType) typeFilter.getSelectedItem();

        tableModel.setRowCount(0);
        int index = 0;
        for (Course course : calculator.getCourses()) {
            boolean matchesSearch = course.getName().toLowerCase().contains(searchText);
            boolean matchesSemester = "全部学期".equals(selectedSemester) || course.getSemester().equals(selectedSemester);
            boolean matchesType = selectedType == null || course.getCourseType() == selectedType;

            if (matchesSearch && matchesSemester && matchesType) {
                tableModel.addRow(course.toTableRow(index++));
            }
        }
    }

    private void updateSemesterList() {
        String currentSelection = (String) semesterFilter.getSelectedItem();
        semesterFilter.removeAllItems();
        semesterFilter.addItem("全部学期");
        
        // 获取所有不重复的学期
        Set<String> uniqueSemesters = calculator.getCourses().stream()
            .map(Course::getSemester)
            .collect(Collectors.toSet());
        
        // 按学期排序（假设格式为"yyyy-学期x"）
        List<String> sortedSemesters = new ArrayList<>(uniqueSemesters);
        Collections.sort(sortedSemesters);
        
        for (String semester : sortedSemesters) {
            semesterFilter.addItem(semester);
        }
        
        // 恢复之前的选择
        if (currentSelection != null) {
            semesterFilter.setSelectedItem(currentSelection);
        }
    }

    // 修改添加课程对话框
    private void showAddCourseDialog(ActionEvent e) {
        JDialog dialog = new JDialog(this, "添加课程", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField creditField = new JTextField(20);
        JTextField scoreField = new JTextField(20);
        JCheckBox selectedBox = new JCheckBox("", true);
        JTextField semesterField = new JTextField(20);
        JComboBox<Course.CourseType> typeComboBox = new JComboBox<>(Course.CourseType.values());

        // 添加组件
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("课程名称："), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("学分："), gbc);
        gbc.gridx = 1;
        dialog.add(creditField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("成绩："), gbc);
        gbc.gridx = 1;
        dialog.add(scoreField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("计入GPA："), gbc);
        gbc.gridx = 1;
        dialog.add(selectedBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("学期："), gbc);
        gbc.gridx = 1;
        dialog.add(semesterField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("课程类型："), gbc);
        gbc.gridx = 1;
        dialog.add(typeComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(ae -> {
            try {
                String name = nameField.getText().trim();
                double credit = Double.parseDouble(creditField.getText().trim());
                double score = Double.parseDouble(scoreField.getText().trim());
                String semester = semesterField.getText().trim();
                Course.CourseType type = (Course.CourseType) typeComboBox.getSelectedItem();

                if (name.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "课程名称和学期不能为空！");
                    return;
                }
                if (credit <= 0) {
                    JOptionPane.showMessageDialog(dialog, "学分必须大于0！");
                    return;
                }
                if (score < 0 || score > 100) {
                    JOptionPane.showMessageDialog(dialog, "成绩必须在0-100之间！");
                    return;
                }

                calculator.addCourse(new Course(name, credit, score, selectedBox.isSelected(), 
                    semester, type));
                updateSemesterList();
                refreshData();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的数字！");
            }
        });

        cancelButton.addActionListener(ae -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void toggleSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            calculator.toggleCourseSelection(selectedRow);
            refreshData();
        }
    }

    private void deleteSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            calculator.removeCourse(selectedRow);
            refreshData();
        }
    }

    // 修改编辑课程对话框
    private void editSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) return;

        Course course = calculator.getCourses().get(selectedRow);
        JDialog dialog = new JDialog(this, "修改课程信息", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(course.getName(), 20);
        JTextField creditField = new JTextField(String.valueOf(course.getCredit()), 20);
        JTextField scoreField = new JTextField(String.valueOf(course.getScore()), 20);
        JCheckBox selectedBox = new JCheckBox("", course.isSelected());
        JTextField semesterField = new JTextField(course.getSemester(), 20);
        JComboBox<Course.CourseType> typeComboBox = new JComboBox<>(Course.CourseType.values());
        typeComboBox.setSelectedItem(course.getCourseType());
        JCheckBox majorCourseBox = new JCheckBox("", course.isMajorCourse());

        // 添加组件
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("课程名称："), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("学分："), gbc);
        gbc.gridx = 1;
        dialog.add(creditField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("成绩："), gbc);
        gbc.gridx = 1;
        dialog.add(scoreField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("计入GPA："), gbc);
        gbc.gridx = 1;
        dialog.add(selectedBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("学期："), gbc);
        gbc.gridx = 1;
        dialog.add(semesterField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("课程类型："), gbc);
        gbc.gridx = 1;
        dialog.add(typeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        dialog.add(new JLabel("是否专业课："), gbc);
        gbc.gridx = 1;
        dialog.add(majorCourseBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(ae -> {
            try {
                String name = nameField.getText().trim();
                double credit = Double.parseDouble(creditField.getText().trim());
                double score = Double.parseDouble(scoreField.getText().trim());
                String semester = semesterField.getText().trim();
                Course.CourseType type = (Course.CourseType) typeComboBox.getSelectedItem();
                boolean isMajorCourse = majorCourseBox.isSelected();

                if (name.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "课程名称和学期不能为空！");
                    return;
                }
                if (credit <= 0) {
                    JOptionPane.showMessageDialog(dialog, "学分必须大于0！");
                    return;
                }
                if (score < 0 || score > 100) {
                    JOptionPane.showMessageDialog(dialog, "成绩必须在0-100之间！");
                    return;
                }

                course.setName(name);
                course.setCredit(credit);
                course.setScore(score);
                course.setSelected(selectedBox.isSelected());
                course.setSemester(semester);
                course.setCourseType(type);
                course.setMajorCourse(isMajorCourse);

                calculator.updateCourse(selectedRow, course);
                updateSemesterList();
                refreshData();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的数字！");
            }
        });

        cancelButton.addActionListener(ae -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 添加删除确认对话框
    private boolean showDeleteConfirmDialog() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) return false;

        Course course = calculator.getCourses().get(selectedRow);
        int result = JOptionPane.showConfirmDialog(
            this,
            String.format("确定要删除课程\"%s\"吗？\n此操作不可恢复！", course.getName()),
            "删除确认",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    private void refreshData() {
        // 更新表格
        tableModel.setRowCount(0);
        List<Course> courses = calculator.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            Object[] rowData = {
                i + 1,
                course.getName(),
                course.getCredit(),
                course.getScore(),
                course.getCourseType().getDisplayName(),
                course.getSemester(),
                course.isSelected() ? "是" : "否"
            };
            tableModel.addRow(rowData);
        }

        // 更新GPA信息
        List<Course> selectedCourses = calculator.getCourses().stream()
                .filter(Course::isSelected)
                .collect(Collectors.toList());

        double averageScore = calculateAverageScore(selectedCourses);
        double standardFiveGPA = calculateStandardFiveGPA(selectedCourses);
        double standardFourGPA = calculateStandardFourGPA(selectedCourses);
        double pkuGPA = calculatePKUGPA(selectedCourses);
        double dgutGPA = calculateDGUTGPA(selectedCourses);

        averageScoreLabel.setText(String.format("总平均分：%.2f", averageScore));
        standardFiveGPALabel.setText(String.format("标准五分制GPA：%.2f", standardFiveGPA));
        standardFourGPALabel.setText(String.format("标准四分制GPA：%.2f", standardFourGPA));
        pkuGPALabel.setText(String.format("北大算法GPA：%.2f", pkuGPA));
        dgutGPALabel.setText(String.format("大工算法GPA：%.2f", dgutGPA));

        // 更新课程分类统计
        double majorAverage = calculator.calculateMajorAverageScore();
        double politicalAverage = calculator.calculatePoliticalAverageScore();
        double qualityAverage = calculator.calculateQualityAverageScore();
        double generalAverage = calculator.calculateGeneralAverageScore();

        majorAverageLabel.setText(String.format("专业课程均分：%.2f", majorAverage));
        politicalAverageLabel.setText(String.format("思政课程均分：%.2f", politicalAverage));
        qualityAverageLabel.setText(String.format("素质课程均分：%.2f", qualityAverage));
        generalAverageLabel.setText(String.format("通识课程均分：%.2f", generalAverage));

        // 更新图表
        updateChart();
        updateSemesterList(); // 更新学期列表
        filterCourses(); // 应用当前的筛选条件
    }

    private void updateChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 添加各个GPA数据
        dataset.addValue(calculator.calculateStandardFiveGPA(), "GPA值", "标准五分制");
        dataset.addValue(calculator.calculateStandardFourGPA(), "GPA值", "标准四分制");
        dataset.addValue(calculator.calculatePKUGPA(), "GPA值", "北大四分制");
        dataset.addValue(calculator.calculateDGUTGPA(), "GPA值", "大工算法");

        // 创建图表并设置中文字体
        Font defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
        Font titleFont = new Font("Microsoft YaHei", Font.BOLD, 16);

        JFreeChart chart = ChartFactory.createBarChart(
                "各算法GPA",
                "算法类型",
                "GPA值",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // 设置图表样式
        chart.setBackgroundPaint(Color.white);
        chart.getTitle().setFont(titleFont);
        
        // 设置图表主题
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 255, 255));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlinePaint(new Color(200, 200, 200));
        
        // 设置条形图样式
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(65, 105, 225));
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setItemMargin(0.1);
        
        // 设置X轴样式
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(defaultFont);
        domainAxis.setLabelFont(defaultFont);
        domainAxis.setCategoryMargin(0.3);
        
        // 设置Y轴样式
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(defaultFont);
        rangeAxis.setLabelFont(defaultFont);
        rangeAxis.setRange(0, 5.0);
        rangeAxis.setTickUnit(new NumberTickUnit(0.5));
        
        // 设置图例样式
        chart.getLegend().setItemFont(defaultFont);
        chart.getLegend().setFrame(BlockBorder.NONE);

        // 更新图表面板
        chartPanel.removeAll();
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 300));
        chartPanel.add(panel);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private double calculateAverageScore(List<Course> courses) {
        if (courses.isEmpty()) return 0.0;
        double totalScore = 0.0;
        double totalCredit = 0.0;
        for (Course course : courses) {
            totalScore += course.getScore() * course.getCredit();
            totalCredit += course.getCredit();
        }
        return totalCredit == 0 ? 0 : totalScore / totalCredit;
    }

    private double calculateStandardFiveGPA(List<Course> courses) {
        if (courses.isEmpty()) return 0.0;
        double totalGPA = 0.0;
        double totalCredit = 0.0;
        for (Course course : courses) {
            if (!course.isSelected()) continue;
            double score = course.getScore();
            double gpa;
            if (score >= 90) gpa = 5.0;
            else if (score >= 80) gpa = 4.0;
            else if (score >= 70) gpa = 3.0;
            else if (score >= 60) gpa = 2.0;
            else gpa = 1.0;
            totalGPA += gpa * course.getCredit();
            totalCredit += course.getCredit();
        }
        return totalCredit == 0 ? 0 : totalGPA / totalCredit;
    }

    private double calculateStandardFourGPA(List<Course> courses) {
        if (courses.isEmpty()) return 0.0;
        double totalGPA = 0.0;
        double totalCredit = 0.0;
        for (Course course : courses) {
            if (!course.isSelected()) continue;
            double score = course.getScore();
            double gpa;
            if (score >= 90) gpa = 4.0;
            else if (score >= 80) gpa = 3.0;
            else if (score >= 70) gpa = 2.0;
            else if (score >= 60) gpa = 1.0;
            else gpa = 0.0;
            totalGPA += gpa * course.getCredit();
            totalCredit += course.getCredit();
        }
        return totalCredit == 0 ? 0 : totalGPA / totalCredit;
    }

    private double calculatePKUGPA(List<Course> courses) {
        if (courses.isEmpty()) return 0.0;
        double totalGPA = 0.0;
        double totalCredit = 0.0;
        for (Course course : courses) {
            if (!course.isSelected()) continue;
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
            totalGPA += gpa * course.getCredit();
            totalCredit += course.getCredit();
        }
        return totalCredit == 0 ? 0 : totalGPA / totalCredit;
    }

    private double calculateDGUTGPA(List<Course> courses) {
        if (courses.isEmpty()) return 0.0;
        double totalGPA = 0.0;
        double totalCredit = 0.0;
        for (Course course : courses) {
            if (!course.isSelected()) continue;
            double score = course.getScore();
            double gpa = (score - 50) / 10;
            if (gpa < 0) gpa = 0;
            totalGPA += gpa * course.getCredit();
            totalCredit += course.getCredit();
        }
        return totalCredit == 0 ? 0 : totalGPA / totalCredit;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
} 