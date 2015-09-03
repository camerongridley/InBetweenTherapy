package com.cggcoding.utils.database;

import java.sql.*;
import java.util.ArrayList;

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

    public boolean validateUser(String email, String password){
        ResultSet userInfo = null;
        int userExists = 0;
        
        try {
            PreparedStatement ps = cn.prepareStatement("SELECT COUNT(*) FROM user WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);

            userInfo = ps.executeQuery();


            while (userInfo.next()){
                userExists = userInfo.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
			
		}


        if(userExists == 1){
            return true;
        } else {
            return false;
        }
    }

    public ResultSet getUserInfo(String email, String password){
        ResultSet userInfo = null;
        try {
            PreparedStatement ps = cn.prepareStatement("SELECT user.user_id, user.email, user.active_treatment_plan_id, user_role.role FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user.email)=?) AND ((user.password)=?))");
            ps.setString(1, email);
            ps.setString(2, password);

            userInfo = ps.executeQuery();

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        }

        return userInfo;
    }

	public ArrayList<String> getDefaultTreatmentIssues() {
		ResultSet treatmentIssues = null;
		
		
		return null;
	}

}
