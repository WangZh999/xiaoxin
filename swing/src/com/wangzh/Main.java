package com.wangzh;

import com.wangzh.login.LoginView;
import com.wangzh.process.ProcessView;

import javax.swing.*;

public class Main {
    private JFrame loginFrame = new JFrame();
    private JFrame processFrame = new JFrame();


    private static class HOLDER {
        private final static Main INSTANCE = new Main();
    }

    public static Main getInstance() {
        return HOLDER.INSTANCE;
    }

    private Main() {

    }

    /**
     * {
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    public void createAndShowLoginView() {
        processFrame.setVisible(false);
        loginFrame.setTitle("Login");
        loginFrame.setSize(350, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel loginView = new LoginView().createView();
        loginFrame.add(loginView);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    public void createAndShowProcessView() {
        loginFrame.setVisible(false);
        processFrame.setTitle("批改");
        processFrame.setSize(460, 800);
        processFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new ProcessView().createView();
        processFrame.add(panel);

        processFrame.setLocationRelativeTo(null);
        processFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Main main = Main.getInstance();
        // 显示应用 GUI
        main.createAndShowLoginView();
//        StringBuilder sb = new StringBuilder("成绩单：");
//        sb.append("\r\n").append("fe");
//        System.out.println(sb.toString());
    }
}
