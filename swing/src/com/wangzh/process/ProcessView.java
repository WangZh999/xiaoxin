package com.wangzh.process;

import com.wangzh.unit.Pair;

import javax.swing.*;
import java.util.List;

public class ProcessView {
    JComboBox<Pair<String, String>> classListCmb;
    JComboBox<Pair<String, String>> taskListCmb;
    JRadioButton allButton;
    JRadioButton onlyButton;
    JButton scoresButton;
    JButton correctButton;
    JButton assessButton;
    JTextArea outputTextField;

    JComboBox<Integer> jmComboBox;
    JComboBox<Integer> dtComboBox;
    JComboBox<Integer> sxComboBox;
    JComboBox<Integer> ztComboBox;
    JTextField pjTextField;

    public JPanel createView() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        classListCmb = new JComboBox<>();
        classListCmb.setBounds(15, 20, 200, 25);
        panel.add(classListCmb);

        taskListCmb = new JComboBox<>();
        taskListCmb.setBounds(245, 20, 200, 25);
        panel.add(taskListCmb);

        addPJComponent(panel);

        ButtonGroup allOrOnly = new ButtonGroup();
        allButton = new JRadioButton("全部");
        allButton.setBounds(130, 190, 60, 25);
        onlyButton = new JRadioButton("仅未批改");
        onlyButton.setBounds(210, 190, 100, 25);
        onlyButton.setSelected(true);
        allOrOnly.add(allButton);
        allOrOnly.add(onlyButton);
        panel.add(allButton);
        panel.add(onlyButton);


        assessButton = new JButton("评价");
        assessButton.setBounds(40, 220, 100, 25);
        panel.add(assessButton);

        correctButton = new JButton("批改成绩");
        correctButton.setBounds(180, 220, 100, 25);
        panel.add(correctButton);

        scoresButton = new JButton("成绩单");
        scoresButton.setBounds(320, 220, 100, 25);
        panel.add(scoresButton);

        outputTextField = new JTextArea();
//        outputTextField.setBounds(20, 260, 405, 490);
        outputTextField.setBounds(0, 0, 400, 460);
        JScrollPane scrollPane = new JScrollPane(outputTextField);
        scrollPane.setBounds(20, 260, 410, 490);
        panel.add(scrollPane);

        addDataAndListener();

        return panel;
    }

    private void addPJComponent(JPanel panel) {
        JLabel jmLabel = new JLabel("卷面:");
        jmLabel.setBounds(20, 65, 60, 25);
        panel.add(jmLabel);
        jmComboBox = getComboBox(1, 5);
        jmComboBox.setBounds(85, 65, 40, 25);
        panel.add(jmComboBox);

        JLabel dtLabel = new JLabel("答题部数:");
        dtLabel.setBounds(20, 95, 60, 25);
        panel.add(dtLabel);
        dtComboBox = getComboBox(1, 5);
        dtComboBox.setBounds(85, 95, 40, 25);
        panel.add(dtComboBox);

        JLabel sxLabel = new JLabel("书写规范:");
        sxLabel.setBounds(20, 125, 60, 25);
        panel.add(sxLabel);
        sxComboBox = getComboBox(1, 5);
        sxComboBox.setBounds(85, 125, 40, 25);
        panel.add(sxComboBox);

        JLabel ztLabel = new JLabel("整体评价:");
        ztLabel.setBounds(20, 155, 60, 25);
        panel.add(ztLabel);
        ztComboBox = getComboBox(1, 5);
        ztComboBox.setBounds(85, 155, 40, 25);
        panel.add(ztComboBox);

        JLabel pyLabel = new JLabel("教师留言:");
        pyLabel.setBounds(150, 65, 80, 25);
        panel.add(pyLabel);
        pjTextField = new JTextField();
        pjTextField.setBounds(150, 95, 270, 80);
        pjTextField.setText("excellent");
        panel.add(pjTextField);
    }

    private JComboBox<Integer> getComboBox(int start, int end) {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int i = start; i <= end; i++) {
            comboBox.addItem(i);
        }
        comboBox.setSelectedIndex(end - start);
        return comboBox;
    }

    private void addDataAndListener() {
        Process process = new Process();
        List<Pair<String, String>> teacherClass = process.getTeacherClass();
        for (Pair<String, String> aClass : teacherClass) {
            classListCmb.addItem(aClass);
        }

        classListCmb.addActionListener(e -> {
            Pair<String, String> selectedItem = classListCmb.getItemAt(classListCmb.getSelectedIndex());
            System.out.println("click: " + selectedItem);
            List<Pair<String, String>> studentTasks = process.getStudentTasks(selectedItem.getValue());
            for (Pair<String, String> studentTask : studentTasks) {
                taskListCmb.addItem(studentTask);
            }
        });

        assessButton.addActionListener(e -> {
            Pair<String, String> classItem = classListCmb.getItemAt(classListCmb.getSelectedIndex());
            Pair<String, String> taskItem = taskListCmb.getItemAt(taskListCmb.getSelectedIndex());

            boolean isAll = allButton.isSelected();
            Integer i1 = jmComboBox.getItemAt(jmComboBox.getSelectedIndex());
            Integer i2 = dtComboBox.getItemAt(dtComboBox.getSelectedIndex());
            Integer i3 = sxComboBox.getItemAt(sxComboBox.getSelectedIndex());
            Integer i4 = ztComboBox.getItemAt(ztComboBox.getSelectedIndex());
            String assessStr = pjTextField.getText();
            if (classItem == null || taskItem == null || i1 == null || i2 == null || i3 == null || i4 == null
                    || assessStr == null || assessStr.isEmpty()) {
                outputTextField.setText("输入有误，请检查班级、作业、评价分数、留言。");
                return;
            }

            outputTextField.setText("评价：" + classItem.getKey() + " - " + taskItem.getKey());
            process.assessTask(classItem.getValue(), taskItem.getValue(), isAll, i1, i2, i3, i4, assessStr);
        });

        correctButton.addActionListener(e -> {
            Pair<String, String> classItem = classListCmb.getItemAt(classListCmb.getSelectedIndex());
            Pair<String, String> taskItem = taskListCmb.getItemAt(taskListCmb.getSelectedIndex());
            if (classItem == null || taskItem == null) {
                outputTextField.setText("输入有误，请检查班级、作业。");
                return;
            }
            boolean isAll = allButton.isSelected();
            outputTextField.setText("批改成绩：" + classItem.getKey() + " - " + taskItem.getKey());
            process.correctTask(classItem.getValue(), taskItem.getValue(), isAll);
        });

        scoresButton.addActionListener(e -> {
            Pair<String, String> classItem = classListCmb.getItemAt(classListCmb.getSelectedIndex());
            Pair<String, String> taskItem = taskListCmb.getItemAt(taskListCmb.getSelectedIndex());
            if (classItem == null || taskItem == null) {
                outputTextField.setText("输入有误，请检查班级、作业。");
                return;
            }
            outputTextField.setText("成绩单：" + classItem.getKey() + " - " + taskItem.getKey());
            List<String> studentScore = process.getStudentScore(classItem.getValue(), taskItem.getValue());
            int len = studentScore.size();
            StringBuilder sb = new StringBuilder("优秀：");
            for (int i = 0; i < 10 && i < len; i++) {
                sb.append("\r\n").append(studentScore.get(i));
            }

            sb.append("\r\n\r\n进步：");
            for (int i = 24; i < 34 && i < len; i++) {
                sb.append("\r\n").append(studentScore.get(i));
            }

            sb.append("\r\n\r\n带改进：");
            for (int i = 45; i < len; i++) {
                sb.append("\r\n").append(studentScore.get(i));
            }
            String text = sb.toString();
            outputTextField.setText(text);
        });
    }
}
