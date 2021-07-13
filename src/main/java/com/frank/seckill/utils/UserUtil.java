package com.frank.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frank.seckill.pojo.User;
import com.frank.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Cabin
 * @date: 2021/7/11
 */
public class UserUtil {
    public static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13500000000L + i);// 模拟11位的手机号码
            user.setNickname("user" + i);
            user.setSalt("1a2b3c");
            user.setLoginCount(1);
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            user.setRegisterDate(new Date());
            users.add(user);
        }
        System.out.println("Create user");
        //插入数据库
        Connection conn = getConn();
        String sql = "INSERT INTO t_user(login_count,nickname,register_date,salt,password,id)VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            preparedStatement.setInt(1,user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        conn.close();
        System.out.println("insert to db done!");
        //
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("E:\\Java\\seckill\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        file.createNewFile();
        accessFile.seek(0);
        for (int i = 0; i < users.size(); i++) {
            // 模拟用户登录
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream out = httpURLConnection.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            // 生成token
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObject());
            System.out.println("create token : " + user.getId());
            // 将token写入文件中，用于压测时模拟客户端登录时发送的token
            String row = user.getId() + "," + userTicket;
            accessFile.seek(accessFile.length());
            accessFile.write(row.getBytes());
            accessFile.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        accessFile.close();
        System.out.println("write token to file done!");
    }

    public static Connection getConn() throws Exception {
        String url = "jdbc:mysql://rm-bp181438h1s942d21oo.mysql.rds.aliyuncs.com:3306/seckill?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "zhaobin";
        String password = "15294834575zhaO";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
