package com.cggcoding.utils.database;

import java.sql.*;

/**
 * Created by cgrid_000 on 8/26/2015.
 */
public class MySQLActionHandler {
    Connection cn;
    private String baseDbURL = "jdbc:mysql://localhost/";
    private String catalog = "cggcodin_doitright";
    private String userID = "admin";
    private String password = "admin";
    private String fullConnectionURL;
    //private HttpServletRequest request;
    // private WebMessageHandler messageHandler = new WebMessageHandler();
    private String message;

    public MySQLActionHandler(){
        this.cn = null;
        //this.request = request;
        this.message = "";
        fullConnectionURL = baseDbURL + catalog + "?user=" + userID + "&password=" + password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void clearMessage(){
        this.message = "";
    }

    public Connection openConnection(){

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem connecting to the database.  Please try again later.");
            ex.printStackTrace();
        } catch (IllegalAccessException ex){
            //messageHandler.setErrorMessage(request, "There seems to be a problem connecting to the database.  Please try again later.");
            System.out.println();
            ex.printStackTrace();
        } catch (InstantiationException ex){
            //messageHandler.setErrorMessage(request, "There seems to be a problem connecting to the database.  Please try again later.");
            ex.printStackTrace();
        }


        try {
            cn = DriverManager.getConnection(fullConnectionURL);
        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem connecting to the database.  Please try again later.");
            e.printStackTrace();
        }
        return cn;

    }

    public void closeConnection(){
        try {
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet validateUser(String email, String password){
        openConnection();
        ResultSet userInfo = null;
        try {
            //PreparedStatement ps = cn.prepareStatement("SELECT COUNT(*) FROM users WHERE email=? AND password=?");
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM user WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);

            userInfo = ps.executeQuery();

            //TODO - delete the line below
            while (userInfo.next()){
                System.out.println("rs has something");
                System.out.println("user id: " + userInfo.getInt("user_id") + " - user email: " + userInfo.getString("email"));
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        }

        closeConnection();

        return userInfo;
    }

    public ResultSet getUserInfo(String username, String password){

        ResultSet userInfo = null;
        try {
            PreparedStatement ps = cn.prepareStatement("SELECT userID, firstName, userName, lastName, email FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);

            userInfo = ps.executeQuery();

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        }

        return userInfo;
    }

}
