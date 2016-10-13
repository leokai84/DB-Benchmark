/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.db.benchmarks;

/**
 *
 * @author Leonardo Venezia
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {

    private String jdbcDriver;
    private String url;
    private String dbName;
    private String username;
    private String password;
    private String error;
    private Connection db;
    private boolean connected;

    public QueryManager(String driver, String url, String dbName) {
        this(driver, url, dbName, "", "");
    }

    public QueryManager(String driver, String url, String dbName, String username, String password) {
        this.jdbcDriver = driver;
        this.url = url;
        this.dbName = dbName;
        this.username = username;
        this.password = password;
        connected = false;
        error = "";
    }

    public boolean connect() {
        connected = false;
        try {

            Class.forName(jdbcDriver);

            if (!dbName.equals("")) {

                if (username.equals("")) {

                    db = DriverManager.getConnection(url + dbName);
                } else
                 if (password.equals("")) {

                        db = DriverManager.getConnection(url + dbName + "?user=" + username);
                    } else {

                        db = DriverManager.getConnection(url + dbName + "?user=" + username + "&password=" + password);
                    }

                connected = true;
            } else {
                System.out.println("Missing database!!");
                System.exit(0);
            }
        } catch (Exception e) {
            error = e.getMessage();
            e.printStackTrace();
        }
        return connected;
    }

    public List performQuery(String query) {
        List list = null;
        List<String> record;
        int columns = 0;
        try {
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            list = new ArrayList();
            ResultSetMetaData rsmd = rs.getMetaData();
            columns = rsmd.getColumnCount();

            while (rs.next()) {
                record = new ArrayList(columns);
                for (int i = 0; i < columns; i++) {
                    record.add(rs.getString(i + 1));
                }
                list.add(record);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        return list;
    }

    public boolean performUpdate(String query) {
        boolean result = false;
        PreparedStatement stmt;
        try {
            stmt = db.prepareStatement(query);
            stmt.executeUpdate();
            result = true;
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            error = e.getMessage();
            result = false;
        }
        return result;
    }

    public void disconnect() {
        try {
            db.close();
            connected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getErrore() {
        return error;
    }

    public Connection getConnection() {
        return db;
    }
}
