package com.cggcoding.utils.database;

import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.dbutils.DbUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Created by cgrid_000 on 8/26/2015.
 */
/**
 * @author cgrid_000
 *
 */
public class MySQLActionHandler {
	DataSource datasource;

    public MySQLActionHandler(DataSource datasource){
    	this.datasource = datasource;
    }

    
    public Connection getConnection(){
    	Connection conn = null;
    	try {
    		conn = datasource.getConnection();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	return conn;
    }
    

    /**************************************************
     *************** Login Methods ********************
     **************************************************/
     
    public boolean validateUser(String email, String password) throws DatabaseException{
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
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
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

    public User getUserInfo(String email, String password) throws DatabaseException{
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
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(userInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return user;
    }
    
    
    /**************************************************************************************************
     ****************************** Treatment Plan Methods *************************************
     **************************************************************************************************/
	
	public TreatmentPlan createTreatmentPlanBasic(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException{		
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	
        	//determine if the combo if userID and plan name exists, if not, then proceed to inserting new plan name
        	boolean comboValid = validateNewTreatmentPlanName(cn, treatmentPlan.getUserID(), treatmentPlan.getName());
        	
        	if(comboValid){
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
        	}
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return treatmentPlan;
	}
	
	
	
	
	private boolean validateNewTreatmentPlanName(Connection cn, int userID, String planName) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {

            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_plan WHERE (((treatment_plan.title)=?) AND ((treatment_plan.treatment_plan_user_id_fk)=?))");
            ps.setString(1, planName.trim());
            ps.setInt(2, userID);

            issueCount = ps.executeQuery();


            while (issueCount.next()){
                comboExists = issueCount.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(issueCount);
			DbUtils.closeQuietly(ps);
		}
		
        if(comboExists > 0){
        	throw new ValidationException(ErrorMessages.PLAN_NAME_EXISTS);
        } else {
        	return true;
        }
		
	}
	
    /**************************************************************************************************
     ****************************************** Stage Methods *****************************************
     **************************************************************************************************/	
	
	public Stage createStageTemplate(Stage newStageTemplate) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	//first check to see if the userID and issue name combo already exists
        	boolean comboValid = validateNewStageName(cn, newStageTemplate.getName(), newStageTemplate.getUserID());
        	
        	if(comboValid){
        		String sql = "INSERT INTO `cggcodin_doitright`.`stage` (`stage_user_id_fk`, `title`, `description`, `order`, `is_template`) "
                		+ "VALUES (?, ?, ?, ?, ?)";
            	
                ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                
                ps.setInt(1, newStageTemplate.getUserID());
                ps.setString(2, newStageTemplate.getName().trim());
                ps.setString(3, newStageTemplate.getDescription().trim());
                ps.setInt(4, newStageTemplate.getIndex());
                ps.setInt(5, 1);

                int success = ps.executeUpdate();
                
                generatedKeys = ps.getGeneratedKeys();
       
                while (generatedKeys.next()){
                	newStageTemplate.setStageID(generatedKeys.getInt(1));
                }
        	}
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return newStageTemplate;
	}


	private boolean validateNewStageName(Connection cn, String stageName, int userID) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
			ps = cn.prepareStatement("SELECT COUNT(*)  FROM stage WHERE (((stage.title)=?) AND ((stage.stage_user_id_fk)=?))");
			ps.setString(1, stageName.trim());
			ps.setInt(2, userID);

			stageCount = ps.executeQuery();

			while (stageCount.next()){
			    comboExists = stageCount.getInt("COUNT(*)");
			}

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(stageCount);
			DbUtils.closeQuietly(ps);
		}
        
		if(comboExists > 0){
			throw new ValidationException(ErrorMessages.STAGE_NAME_EXISTS);
		} else {
			return true;
		}
	}
	
    /**************************************************************************************************
     *************************************** Treatment Issue Methods **********************************
     **************************************************************************************************/

	public TreatmentIssue createTreatmentIssue(TreatmentIssue treatmentIssue) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	//first check to see if the userID and issue name combo already exists
        	boolean comboValid = validateNewIssueName(cn, treatmentIssue.getTreatmentIssueName(), treatmentIssue.getUserID());
        	
        	if(comboValid){
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
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return treatmentIssue;
	}
	

	/**
	 * Checks if there is an existing combination of treatment issue name and userID in the database.
	 * @param cn Database connection
	 * @param issueName Treatment Issue name of new issue that user wants to create
	 * @param userID id of the user
	 * @return true if the the combination is valid and does not exist in the database. false if the combination exists and is therefore invalid.
	 * @throws SQLException
	 * @throws ValidationException 
	 * @throws DatabaseException
	 */
	private boolean validateNewIssueName(Connection cn, String issueName, int userID) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {
            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_issue WHERE (((treatment_issue.issue)=?) AND ((treatment_issue.treatment_issue_user_id_fk)=?))");
            ps.setString(1, issueName.trim());
            ps.setInt(2, userID);

            issueCount = ps.executeQuery();


            while (issueCount.next()){
                comboExists = issueCount.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(issueCount);
			DbUtils.closeQuietly(ps);
		}

        if(comboExists > 0){
        	throw new ValidationException(ErrorMessages.ISSUE_NAME_EXISTS);
        } else {
            return true;
        }
    }

	public ArrayList<TreatmentIssue> getDefaultTreatmentIssues() throws DatabaseException{
		//TODO - probably shouldn't have a hardcoded value here for the admin user id and should instead lookup all the users with admin role and get their ids and run for each
		ArrayList<TreatmentIssue> issues = getTreatmentIssuesList(1);
		
		return issues;
	}

    public ArrayList<TreatmentIssue> getTreatmentIssuesList(int userID) throws DatabaseException{
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
        	e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return issues;
    }

}
