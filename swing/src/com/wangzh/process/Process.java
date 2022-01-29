package com.wangzh.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangzh.data.Token;
import com.wangzh.unit.Constant;
import com.wangzh.unit.HttpClient;
import com.wangzh.unit.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Process {
    public List<Pair<String, String>> getTeacherClass() {
        List<Pair<String, String>> pairs = new ArrayList<>();

        String payload = "token=" + Token.getInstance().getToken();
        String responseStr = HttpClient.doPost(Constant.GET_TEACHER_CLASS_URL, payload);
        JSONArray allClass = JSON.parseObject(responseStr).getJSONArray("data");
        for (int i = 0; i < allClass.size(); i++) {
            JSONObject jsonObject = allClass.getJSONObject(i);
            String className = jsonObject.getString("className");
            String classId = jsonObject.getString("classId");
            pairs.add(new Pair<>(className, classId));
        }

        return pairs;
    }


    public List<Pair<String, String>> getStudentTasks(String classId) {
        List<Pair<String, String>> pairs = new ArrayList<>();

        String payload = "classId=" + classId + "&sid=3&token=" + Token.getInstance().getToken();
        String responseStr = HttpClient.doPost(Constant.GET_STUDENT_TASKS_URL, payload);
        JSONArray allClass = JSON.parseObject(responseStr).getJSONArray("data");
        for (int i = 0; i < allClass.size(); i++) {
            JSONObject jsonObject = allClass.getJSONObject(i);
            String taskName = jsonObject.getString("taskName");
            String taskId = jsonObject.getString("taskId");
            pairs.add(new Pair<>(taskName, taskId));
        }

        return pairs;
    }


    public List<Student> getStudentCorrectState(String cId, String tId) {
        List<Student> studentList = new ArrayList<>();
        String payload = "page=1&limit=99999999&classId=" + cId + "&taskId=" + tId + "&token=" + Token.getInstance().getToken();
        String responseStr = HttpClient.doPost(Constant.GET_STUDENT_CORRECT_STATE_URL, payload);

        JSONArray allStudents = JSON.parseObject(responseStr).getJSONArray("data");

        for (int i = 0; i < allStudents.size(); i++) {
            JSONObject jsonObject = allStudents.getJSONObject(i);
            String realName = jsonObject.getString("realName");
            String userId = jsonObject.getString("userId");
            String isAssess = jsonObject.getString("isAssess");
            String isCorrect = jsonObject.getString("isCorrect");
            studentList.add(new Student(realName, userId, isAssess, isCorrect));
        }

        return studentList;
    }

    public void assessStudentTask(String tId, String uId, int i1, int i2, int i3, int i4, String assessStr) {
        String encodeAssess = "";
        try {
            encodeAssess = URLEncoder.encode(assessStr, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String payload = "taskId=" + tId +
                "&userId=" + uId +
                "&taskAssess=" + i1 +
                "%2C" + i2 +
                "%2C" + i3 +
                "%2C" + i4 +
                "&assessContent=" + encodeAssess +
                "&token=" + Token.getInstance().getToken();

        HttpClient.doPost(Constant.ASSESS_STUDENT_TASK_URL, payload);
    }

    public void correctStudentTask(String tId, String uId, String teaId, int teaScore) {
        int score = teaScore - new Random().nextInt((int) (teaScore * 0.3));
        String payload = "taskId=" + tId + "&userId=" + uId + "&token=" + Token.getInstance().getToken() +
                "&taskScore=" + teaId + "-" + score + "-" + teaScore;
        HttpClient.doPost(Constant.CORRECT_STUDENT_TASK_URL, payload);
    }

    public Pair<String, Integer> getStudentSubjectiveAnswer(String tId, String uId) {
        String payload = "taskId=" + tId + "&userId=" + uId + "&token=" + Token.getInstance().getToken();
        String responseStr = HttpClient.doPost(Constant.GET_STUDENT_SUBJECTIVE_ANSWER_URL, payload);
        JSONObject jsonObject = JSON.parseArray(responseStr).getJSONObject(0);
        String teaId = jsonObject.getString("teaId");
        Integer teaScore = jsonObject.getInteger("teaScore");
        return new Pair<>(teaId, teaScore);
    }

    public List<String> getStudentScore(String cId, String tId) {
        String payload = "page=1&limit=99999999&classId=" + cId + "&taskId=" + tId +
                "&token=" + Token.getInstance().getToken();
        String responseStr = HttpClient.doPost(Constant.GET_STUDENT_SCORE_URL, payload);
        JSONArray allStudents = JSON.parseObject(responseStr).getJSONArray("data");
        List<String> students = new ArrayList<>();
        for (int i = 0; i < allStudents.size(); i++) {
            JSONObject jsonObject = allStudents.getJSONObject(i);
            students.add(jsonObject.getString("realName"));
        }
        return students;
    }


    public void correctTask(String cId, String tId, boolean isAll) {
        List<Student> studentList = getStudentCorrectState(cId, tId);
        if (!isAll) {
            studentList = studentList.stream().filter(o -> !o.isCorrect).collect(Collectors.toList());
        }

        Pair<String, Integer> studentSubjectiveAnswer = null;
        if (studentList.isEmpty()) {
            return;
        }

        studentSubjectiveAnswer = getStudentSubjectiveAnswer(tId, studentList.get(0).getId());
        for (Student student : studentList) {
            correctStudentTask(tId, student.getId(), studentSubjectiveAnswer.getKey(), studentSubjectiveAnswer.getValue());
        }
    }

    public void assessTask(String cId, String tId, boolean isAll, int i1, int i2, int i3, int i4, String assessStr) {
        List<Student> studentList = getStudentCorrectState(cId, tId);
        if (!isAll) {
            studentList = studentList.stream().filter(o -> !o.isCorrect).collect(Collectors.toList());
        }
        if (studentList.isEmpty()) {
            return;
        }

        for (Student student : studentList) {
            assessStudentTask(tId, student.getId(), i1, i2, i3, i4, assessStr);
        }
    }

}
