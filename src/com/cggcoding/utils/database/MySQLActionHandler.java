package com.cggcoding.utils.database;

import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.dbutils.DbUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;

/**
 * Created by cgrid_000 on 8/26/2015.
 */
public class MySQLActionHandler {
	private String message;
	DataSource datasource;
    /*TODO - removed old database connection code if I stick with new pool method
     * Connection cn;
    private String baseDbURL = "jdbc:mysql://localhost/";
    private String catalog = "cggcodin_doitright";
    private String userID = "admin";
    private String password = "admin";
    private String fullConnectionURL;
    */
    //private HttpServletRequest request;
    // private WebMessageHandler messageHandler = new WebMessageHandler();
    

    public MySQLActionHandler(DataSource datasource){
    	this.datasource = datasource;
    	this.message = "";
        //this.cn = null;
        //fullConnectionURL = baseDbURL + catalog + "?user=" + userID + "&password=" + password;
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

    /*
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
    */
    
    public Connection getConnection(){
    	Connection conn = null;
    	try {
    		conn = datasource.getConnection();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	return conn;
    }
    

    /*
    public void closeConnection(){
        try {
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

    /**************************************************
     *************** Login Methods ********************
     **************************************************/
     
    public boolean validateUser(String email, String password){
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet userInfo = null;
        int userExists = 0;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT COUNT(*) FROM user WHERE email=? AND password=?");
            ps.setString(1, email.trim());
            ps.setString(2, password.trim());

            userInfo = ps.executeQuery();


            while (userInfo.next()){
                userExists = userInfo.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
			DbUtils.closeQuietly(userInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
			
		}


        if(userExists == 1){
            return true;
        } else {
            return false;
        }
    }

    public User getUserInfo(String email, String password){
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet userInfo = null;
        User user = null;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT user.user_id, user.email, user.active_treatment_plan_id, user_role.role FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user.email)=?) AND ((user.password)=?))");
            ps.setString(1, email.trim());
            ps.setString(2, password.trim());

            userInfo = ps.executeQuery();
            
            //TODO - replace the use of downcasting? - e.g. UserClient.setActiveTreatmentPlanID
            // see http://programmers.stackexchange.com/questions/258655/ood-java-inheritance-and-access-to-child-methods-via-casting 
            while (userInfo.next()){
            	switch (userInfo.getString("role")){
            		case "admin":
            			user = new UserAdmin(userInfo.getInt("user_id"), userInfo.getString("email"));
            			user.addRole("admin");
            			break;
            		case "therapist":
            			user = new UserTherapist(userInfo.getInt("user_id"), userInfo.getString("email"));
            			user.addRole("therapist");
            			break;
            		case "client":
            			user = new UserClient(userInfo.getInt("user_id"), userInfo.getString("email"));
            			user.addRole("client");
            			((UserClient)user).setActiveTreatmentPlanId(userInfo.getInt("active_treatment_plan_id"));
            			break;
            	}
            }
            

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
        	DbUtils.closeQuietly(userInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return user;
    }
    
    
    /**************************************************
     ******** Create Treatment Plan Methods ***********
     **************************************************/
    
    public ArrayList<TreatmentIssue> getTreatmentIssuesList(int userID){
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<TreatmentIssue> issues = new ArrayList<>();
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT treatment_issue.treatment_issue_id, treatment_issue.issue, user.user_id "
            		+ "FROM (user) INNER JOIN treatment_issue ON user.user_id = treatment_issue.treatment_issue_user_id_fk "
            		+ "WHERE (((user.user_id)=?))");
            ps.setInt(1, userID);

            rs = ps.executeQuery();
   
            while (rs.next()){
            	TreatmentIssue issue = new TreatmentIssue(rs.getInt("treatment_issue_id"), rs.getString("issue"));
            	issues.add(issue);
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return issues;
    }

	public ArrayList<TreatmentIssue> getDefaultTreatmentIssues() {
		//TODO - probably shouldn't have a hardcoded value here for the admin user id and should instead lookup all the users with admin role and get their ids and run for each
		ArrayList<TreatmentIssue> issues = getTreatmentIssuesList(1);
		
		return issues;
	}
	
	public TreatmentPlan createTreatmentPlanBasic(TreatmentPlan treatmentPlan){		
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	String sql = "INSERT INTO `cggcodin_doitright`.`treatment_plan` (`treatment_plan_user_id_fk`, `treatment_plan_treatment_issue_id_fk`, `title`, `description`, `current_stage_index`, `active_view_stage_index`, `in_progress`) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, treatmentPlan.getUserID());
            ps.setInt(2, treatmentPlan.getTreatmentIssueID());
            ps.setString(3, treatmentPlan.getName().trim());
            ps.setString(4, treatmentPlan.getDescription().trim());
            ps.setInt(5, treatmentPlan.getCurrentStageIndex());
            ps.setInt(6, treatmentPlan.getActiveViewStageIndex());
            ps.setBoolean(7, treatmentPlan.isInProgress());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	treatmentPlan.setTreatmentPlanID(generatedKeys.getInt(1));;
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return treatmentPlan;
	}
	
	
	public TreatmentIssue createTreatmentIssue(TreatmentIssue treatmentIssue) throws ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	//first check to see if the userID and issue name combo already exists
        	boolean comboExists = validateNewIssueName(treatmentIssue.getTreatmentIssueName(), treatmentIssue.getUserID());
        	
        	if(!comboExists){
        		String sql = "INSERT INTO `cggcodin_doitright`.`treatment_issue` (`issue`, `treatment_issue_user_id_fk`) "
                		+ "VALUES (?, ?)";
            	
                ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                
                ps.setString(1, treatmentIssue.getTreatmentIssueName().trim());
                ps.setInt(2, treatmentIssue.getUserID());

                int success = ps.executeUpdate();
                
                generatedKeys = ps.getGeneratedKeys();
       
                while (generatedKeys.next()){
                	treatmentIssue.setTreatmentIssueID(generatedKeys.getInt(1));
                }
        	}
        	

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return treatmentIssue;
	}
	
	public boolean validateNewIssueName(String issueName, int userID) throws ValidationException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_issue WHERE (((treatment_issue.issue)=?) AND ((treatment_issue.treatment_issue_user_id_fk)=?))");
            ps.setString(1, issueName.trim());
            ps.setInt(2, userID);

            issueCount = ps.executeQuery();


            while (issueCount.next()){
                comboExists = issueCount.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
			DbUtils.closeQuietly(issueCount);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
			
		}


        if(comboExists == 1){
        	throw new ValidationException("The new custom treatment issue already exists in your profile.");
            //return true;
        } else {
            return false;
        }
    }

	public boolean validateNewTreatmentPlanName(int userID, String planName) throws ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_plan WHERE (((treatment_plan.title)=?) AND ((treatment_plan.treatment_plan_user_id_fk)=?))");
            ps.setString(1, planName.trim());
            ps.setInt(2, userID);

            issueCount = ps.executeQuery();


            while (issueCount.next()){
                comboExists = issueCount.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            //messageHandler.setErrorMessage(request, "There seems to be a problem accessing your information from the database.  Please try again later.");
            e.printStackTrace();
        } finally {
			DbUtils.closeQuietly(issueCount);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
			
		}

        if(comboExists != 0){
        	throw new ValidationException("That treatment plan name already exists in your profile.  Please use a different name.");
            //return true;
        } else {
            return false;
        }
		
	}

}
