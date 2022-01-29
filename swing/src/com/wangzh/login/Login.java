package com.wangzh.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangzh.Main;
import com.wangzh.data.Token;
import com.wangzh.unit.Constant;
import com.wangzh.unit.HttpClient;
import com.wangzh.unit.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Login implements ActionListener {

    private final JTextField userNameField;
    private final JTextField passwordField;
    private final JLabel hintLabel;

    public Login(JTextField userNameField, JTextField passwordField, JLabel hintLabel) {
        this.userNameField = userNameField;
        this.passwordField = passwordField;
        this.hintLabel = hintLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());

        String userName = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        String data = "userName=" + userName + "&userPass=" + password + "&platform=pc&deviceNo=Pc_Hello";

        String response = HttpClient.doPost(Constant.LOGIN_URL, data);

        System.out.println(response);

        JSONObject res = (JSONObject) JSON.parse(response);
        JSONObject data1 = res.getJSONObject("data");
        if (data1 == null) {
            hintLabel.setText(response);
            return;
        }
        String token = data1.getString("token");
        hintLabel.setText("token: " + token);
        Token.getInstance().setToken(token);
        System.out.println(token);

        recordUser(userName, password);
        Main.getInstance().createAndShowProcessView();
    }

    private void recordUser(String name, String pass) {
        File userFile = new File(Constant.USER_FILE);
        try {
            userFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(userFile)) {
            fileOutputStream.write(name.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write(pass.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pair<String, String> getUser() {
        File userFile = new File(Constant.USER_FILE);
        if (!userFile.isFile()) {
            return new Pair<>("", "");
        }
        try (FileReader fr = new FileReader(userFile);
             BufferedReader br = new BufferedReader(fr)) {
            String name = br.readLine();
            String pass = br.readLine();
            return new Pair<>(name, pass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>("", "");
    }
}
