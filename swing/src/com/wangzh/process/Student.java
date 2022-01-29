package com.wangzh.process;

public class Student {
    String name;
    String id;
    boolean isAccess;
    boolean isCorrect;

    public Student(String name, String id, String isAccess, String isCorrect) {
        this.name = name;
        this.id = id;
        this.isAccess = !isAccess.equals("0");
        this.isCorrect = !isCorrect.equals("0");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAccess() {
        return isAccess;
    }

    public void setAccess(boolean access) {
        isAccess = access;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isAccess=" + isAccess +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
