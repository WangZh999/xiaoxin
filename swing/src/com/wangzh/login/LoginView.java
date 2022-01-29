package com.wangzh.login;

import com.wangzh.unit.Pair;

import javax.swing.*;

public class LoginView {

    Login login;

    public JPanel createView() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);


        JTextField passwordText = new JTextField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        // 创建登录按钮
        JButton loginButton = new JButton("login");
        loginButton.setBounds(120, 80, 80, 25);
        panel.add(loginButton);

        // 输入密码的文本域
        JLabel hintLabel = new JLabel("");
        hintLabel.setBounds(10, 120, 300, 25);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(hintLabel);

        login = new Login(userText, passwordText, hintLabel);
        loginButton.addActionListener(login);

        Pair<String, String> user = login.getUser();
        userText.setText(user.getKey());
        passwordText.setText(user.getValue());
        return panel;
    }
}