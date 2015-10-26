package com.cggcoding.utils.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.TaskTwoTextBoxes;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.SqlBuilders;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Created by cgrid_000 on 8/26/2015.
 */
/**
 * @author cgrid_000
 *
 */
public class MySQLActionHandler implements DatabaseActionHandler{
	DatabaseConnection mysqlConn;
	private static final int ADMIN_ROLE_ID = 1;

    public MySQLActionHandler(){
    	this.mysqlConn = new MySQLConnection();
    }

    /* (non-Javadoc)
	 * @see com.cggcoding.utils.database.DatabaseActionHandler#getConnection()
	 */
	@Override
    public Connection getConnection(){
		return mysqlConn.getConnection();

    }
	
	private boolean throwValidationExceptionIfTemplateHolderID(int templateHolderObjectID) throws ValidationException{
		if(templateHolderObjectID == Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){
			throw new ValidationException(ErrorMessages.DEFAULTS_HOLDER_ID_SELECTED);
		}
		
		return true;
	}
	
	private boolean throwValidationExceptionIfNull(Object o) throws ValidationException{
		if(o == null){
			throw new ValidationException(ErrorMessages.OBJECT_IS_NULL);
		}
		
		return true;
	}
    

    /* (non-Javadoc)
	 * @see com.cggcoding.utils.database.DatabaseActionHandler#validateUser(java.lang.String, java.lang.String)
	 */
     
    @Override
	public boolean userValidate(String email, String password) throws DatabaseException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet userInfo = null;
        int userExists = 0;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT COUNT(*) FROM user WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);

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

    /* (non-Javadoc)
	 * @see com.cggcoding.utils.database.DatabaseActionHandler#getUserInfo(java.lang.String, java.lang.String)
	 */
    @Override
	public User userLoadInfo(String email, String password) throws DatabaseException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet userInfo = null;
        User user = null;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT user.user_id, user.email, user.active_treatment_plan_id, user_role.role FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user.email)=?) AND ((user.password)=?))");
            ps.setString(1, email);
            ps.setString(2, password);

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
    
    
    private List<Integer> userGetAdminIDs(Connection cn) throws DatabaseException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> adminIDList = new ArrayList<>();
        
        try {
    		String sql = "SELECT user.user_id FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user_role.user_role_id)=" + ADMIN_ROLE_ID + "))";    	

    		ps = cn.prepareStatement(sql);
    		
            rs = ps.executeQuery();
   
            while (rs.next()){
            	adminIDList.add(rs.getInt("user_id"));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }
        
        return adminIDList;
    }
    

	@Override
	public List<TreatmentPlan> treatmentPlanGetDefaults() throws DatabaseException, ValidationException {
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<TreatmentPlan> defaultPlanList = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	List<Integer> adminIDList = userGetAdminIDs(cn);
        	
        	String baseStatement = "SELECT * FROM treatment_plan WHERE treatment_plan_is_template=? AND treatment_plan_user_id_fk in (";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, null);
        	
    		ps = cn.prepareStatement(sql);
    		
    		ps.setBoolean(1, true);
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+2, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	if(rs.getInt("treatment_plan_id") != Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){ //TreatmentPlan with id=1 is the Plan that holds all Stage Defaults and so should not be included in the results of this query.
            		defaultPlanList.add(treatmentPlanLoadWithEmpyLists(rs.getInt("treatment_plan_id")));
            	}
            	
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        throwValidationExceptionIfNull(defaultPlanList);
        
        return defaultPlanList;
	}
	
    @Override
    public TreatmentPlan treatmentPlanLoadWithEmpyLists(int treatmentPlanID) throws DatabaseException, ValidationException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet planInfo = null;
        TreatmentPlan plan = null;
        
        throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT * from treatment_plan WHERE treatment_plan_id=?");
            ps.setInt(1, treatmentPlanID);


            planInfo = ps.executeQuery();

            while (planInfo.next()){
            	plan = TreatmentPlan.getInstanceBasic(planInfo.getInt("treatment_plan_id"), planInfo.getInt("treatment_plan_user_id_fk"), 
            			planInfo.getString("treatment_plan_title"), planInfo.getString("treatment_plan_description"), planInfo.getInt("treatment_plan_treatment_issue_id_fk"),
            			planInfo.getBoolean("in_progress"), planInfo.getBoolean("treatment_plan_is_template"), planInfo.getBoolean("treatment_plan_completed")
            			);
            	
            }
            

        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(planInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        throwValidationExceptionIfNull(plan);
        
        return plan;
    }
    
    @Override
    public List<Integer> treatmentPlanGetStageIDs(int treatmentPlanID) throws DatabaseException, ValidationException {
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> stageIDs = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	cn = getConnection();
        	ps = cn.prepareStatement("SELECT stage_id FROM stage WHERE stage_treatment_plan_id_fk=?");
            ps.setInt(1, treatmentPlanID);


            rs = ps.executeQuery();

            while (rs.next()){
            	stageIDs.add(rs.getInt("stage_id"));
            }
            /*The code below was for when this method returned a list of Stages instead of a list of stageIDs
             * ps = cn.prepareStatement("SELECT stage.*, stage_goal.* FROM stage INNER JOIN stage_goal ON stage.stage_id = stage_goal.stage_goal_stage_id_fk WHERE stage_id=?");
            ps.setInt(1, treatmentPlanID);


            rs = ps.executeQuery();

            while (rs.next()){
            	stages.add(Stage.getInstance(rs.getInt("stage_id"), rs.getInt("stage_treatment_plan_id_fk"), rs.getInt("stage_user_id_fk"), rs.getString("stage_title"), 
            			rs.getString("stage_description"), rs.getInt("stage_order"), new ArrayList<Task>(), new ArrayList<Task>(), rs.getBoolean("stage_completed"), rs.getInt("percent_complete"),
            			new ArrayList<StageGoal>(), rs.getBoolean("stage_is_template"))	);
            }*/
            

        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return stageIDs;
    }
    
    //TODO delete method?
    public TreatmentPlan treatmentPlanLoadWithStageAndTaskCoreData(int treatmentPlanID) throws DatabaseException, ValidationException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet planInfo = null;
        TreatmentPlan plan = null;
        int stageID = 0;
        int taskID = 0;
        
        throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT treatment_plan.treatment_plan_id, treatment_plan.*, treatment_issue.issue, stage.*, task_generic.*, task_type.task_type "
            + "FROM (treatment_issue INNER JOIN treatment_plan ON treatment_issue.treatment_issue_id = treatment_plan.treatment_plan_treatment_issue_id_fk) "
            + "LEFT JOIN (task_type RIGHT JOIN (stage LEFT JOIN task_generic ON stage.stage_id = task_generic.task_generic_stage_id_fk) "
            + "ON task_type.task_type_id = task_generic.task_generic_task_type_id_fk) ON treatment_plan.treatment_plan_id = stage.stage_treatment_plan_id_fk "
            + "WHERE (((treatment_plan.treatment_plan_id)=?))");
            
            ps.setInt(1, treatmentPlanID);


            planInfo = ps.executeQuery();

            while (planInfo.next()){
            	if(planInfo.isFirst()){
            		plan = TreatmentPlan.getInstanceBasic(planInfo.getInt("treatment_plan_id"), planInfo.getInt("treatment_plan_user_id_fk"), 
                			planInfo.getString("treatment_plan_title"), planInfo.getString("treatment_plan_description"), planInfo.getInt("treatment_plan_treatment_issue_id_fk"),
                			planInfo.getBoolean("in_progress"), planInfo.getBoolean("treatment_plan_is_template"), planInfo.getBoolean("treatment_plan_completed")
                			);
            		
            		//need to initialize these variables
            		stageID = planInfo.getInt("stage_id");
                	taskID = planInfo.getInt("task_generic_id");
            	}
            	
            	stageID = planInfo.getInt("stage_id");
            	taskID = planInfo.getInt("task_generic_id");
            	
            	
            	
            }
            

        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(planInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        throwValidationExceptionIfNull(plan);
        
        return plan;
    }
    
    @Override
    public TreatmentPlan treatmentPlanValidateAndCreateBasic(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException{
    	Connection cn = null;
        
        try {
        	cn = getConnection();
        	if(treatmentPlanValidateNewTitle(cn, treatmentPlan.getUserID(), treatmentPlan.getTitle())){
        		treatmentPlan = treatmentPlanCreateBasic(cn, treatmentPlan);
        	}
        } finally {
			DbUtils.closeQuietly(cn);
        }
        
        throwValidationExceptionIfNull(treatmentPlan);
        
        return treatmentPlan;
    }
    
    @Override
	public void treatmentPlanValidateAndUpdate(TreatmentPlan treatmentPlan) throws DatabaseException, ValidationException {
		
		Connection cn = null;
        
        try {
        	cn = getConnection();
        	if(treatmentPlanValidateUpdatedTitle(cn, treatmentPlan)){
        		treatmentPlanUpdate(cn, treatmentPlan);
        	}
        } finally {
			DbUtils.closeQuietly(cn);
        }
		
	}

	private boolean treatmentPlanValidateNewTitle(Connection cn, int userID, String planTitle) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {

            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_plan WHERE treatment_plan.treatment_plan_title=? AND treatment_plan.treatment_plan_user_id_fk=?");
            ps.setString(1, planTitle.trim());
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
        	throw new ValidationException(ErrorMessages.PLAN_TITLE_EXISTS);
        } else {
        	return true;
        }
		
	}
	
	private boolean treatmentPlanValidateUpdatedTitle(Connection cn, TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {
            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_plan WHERE treatment_plan.treatment_plan_title=? AND treatment_plan_id!=? AND treatment_plan_is_template=? AND treatment_plan.treatment_plan_user_id_fk=?");
            ps.setString(1, treatmentPlan.getTitle().trim());
            ps.setInt(2, treatmentPlan.getTreatmentPlanID());
            ps.setBoolean(3, treatmentPlan.isTemplate());
            ps.setInt(4, treatmentPlan.getUserID());

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
        	throw new ValidationException(ErrorMessages.PLAN_TITLE_EXISTS);
        } else {
        	return true;
        }
		
	}

	private TreatmentPlan treatmentPlanCreateBasic(Connection cn, TreatmentPlan treatmentPlan) throws DatabaseException, ValidationException{		
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	String sql = "INSERT INTO treatment_plan (treatment_plan_user_id_fk, treatment_plan_treatment_issue_id_fk, treatment_plan_title, treatment_plan_description, current_stage_index, active_view_stage_index, in_progress, treatment_plan_is_template) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, treatmentPlan.getUserID());
            ps.setInt(2, treatmentPlan.getTreatmentIssueID());
            ps.setString(3, treatmentPlan.getTitle().trim());
            ps.setString(4, treatmentPlan.getDescription());
            ps.setInt(5, treatmentPlan.getCurrentStageIndex());
            ps.setInt(6, treatmentPlan.getActiveViewStageIndex());
            ps.setBoolean(7, treatmentPlan.isInProgress());
            ps.setBoolean(8, treatmentPlan.isTemplate());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	treatmentPlan.setTreatmentPlanID(generatedKeys.getInt(1));;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        throwValidationExceptionIfNull(treatmentPlan);
        
        return treatmentPlan;
	}
	
	
	private void treatmentPlanUpdate(Connection cn, TreatmentPlan treatmentPlan) throws DatabaseException, ValidationException {
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        throwValidationExceptionIfNull(treatmentPlan);
        
        try {
        	String sql = "UPDATE treatment_plan SET treatment_plan_user_id_fk=?, treatment_plan_treatment_issue_id_fk=?, treatment_plan_title=?, treatment_plan_description=?, current_stage_index=?, "
        			+ "active_view_stage_index=?, in_progress=?, treatment_plan_is_template=?, treatment_plan_completed=? WHERE treatment_plan_id=?";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, treatmentPlan.getUserID());
            ps.setInt(2, treatmentPlan.getTreatmentIssueID());
            ps.setString(3, treatmentPlan.getTitle().trim());
            ps.setString(4, treatmentPlan.getDescription());
            ps.setInt(5, treatmentPlan.getCurrentStageIndex());
            ps.setInt(6, treatmentPlan.getActiveViewStageIndex());
            ps.setBoolean(7, treatmentPlan.isInProgress());
            ps.setBoolean(8, treatmentPlan.isTemplate());
            ps.setBoolean(9, treatmentPlan.isCompleted());
            ps.setInt(10, treatmentPlan.getTreatmentPlanID());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	treatmentPlan.setTreatmentPlanID(generatedKeys.getInt(1));;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
	}
	
	public Stage stageValidateAndCreate(Stage newStage) throws ValidationException, DatabaseException{
		Connection cn = null;
		Stage stage = null;
		
		throwValidationExceptionIfTemplateHolderID(newStage.getStageID());
		
        try {
        	cn= getConnection();
			if(stageValidateNewTitle(cn, newStage)){
				stage = stageCreateBasic(cn, newStage);
			}
			
        } finally {
			DbUtils.closeQuietly(cn);
		}
        
        throwValidationExceptionIfNull(stage);
        
        return stage;
	}
	
	/** Validating a new Stage title involves checking is there is already a match for the combination of the new title and the userID.
	 * If there is a match then the new title is invalid
	 * @param cn
	 * @param newStage - A Stage object containing at least a title and userID
	 * @return true if valid combination, false throws ValidationException
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	private boolean stageValidateNewTitle(Connection cn, Stage newStage) throws ValidationException, DatabaseException{
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        if(newStage.getTitle().isEmpty()){
        		throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
        	}
        
        	try {

	        	cn= getConnection();
				ps = cn.prepareStatement("SELECT COUNT(*) FROM stage WHERE stage_title=? AND stage_treatment_plan_id_fk=? AND stage_user_id_fk=? AND stage_is_template=?");
				ps.setString(1, newStage.getTitle().trim());
				ps.setInt(2, newStage.getTreatmentPlanID());
				ps.setInt(3, newStage.getUserID());
				ps.setBoolean(4, newStage.isTemplate());
	
				stageCount = ps.executeQuery();
	
				while (stageCount.next()){
				    comboExists = stageCount.getInt("COUNT(*)");
				}
				
				if(comboExists > 0){
					throw new ValidationException(ErrorMessages.STAGE_TITLE_EXISTS);
				}

	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
	        } finally {
				DbUtils.closeQuietly(stageCount);
				DbUtils.closeQuietly(ps);

			}

        
		return true;
	}
	
	/**Validating a new Stage title involves checking is there is already a match for the combination of the new title and the userID. However, since
	 * in case the title wasn't actually changed, need to also exclude any results that have a stageID equal to the stageID of the Stage parameter
	 * @param cn
	 * @param newStage
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	private boolean stageValidateUpdatedTitle(Connection cn, Stage newStage) throws ValidationException, DatabaseException{
		//Connection cn = null;
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
        	cn= getConnection();
			ps = cn.prepareStatement("SELECT COUNT(*) FROM stage WHERE stage.stage_title = ? AND stage.stage_treatment_plan_id_fk= ?  AND stage.stage_id != ? AND stage.stage_user_id_fk = ? AND stage_is_template=?");
			ps.setString(1, newStage.getTitle().trim());
			ps.setInt(2, newStage.getTreatmentPlanID());
			ps.setInt(3, newStage.getStageID());
			ps.setInt(4, newStage.getUserID());
			ps.setBoolean(5, newStage.isTemplate());

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
			//DbUtils.closeQuietly(cn);
		}
        
		if(comboExists > 0){
			throw new ValidationException(ErrorMessages.STAGE_TITLE_EXISTS);
		} else {
			return true;
		}
	}
	
	private Stage stageCreateBasic(Connection cn, Stage newStage) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
    		String sql = "INSERT INTO stage (stage_user_id_fk, stage_treatment_plan_id_fk, stage_title, stage_description, stage_completed, stage_order, percent_complete, stage_is_template) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newStage.getUserID());
            ps.setInt(2, newStage.getTreatmentPlanID());
            ps.setString(3, newStage.getTitle().trim());
            ps.setString(4, newStage.getDescription());
            ps.setBoolean(5,  newStage.isCompleted());
            ps.setInt(6, newStage.getStageOrder());
            ps.setInt(7, newStage.getPercentComplete());
            ps.setBoolean(8, newStage.isTemplate());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	newStage.setStageID(generatedKeys.getInt(1));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        
        return newStage;
	}

	
	@Override
	public boolean stageUpdate(Stage newStage) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        int success = 0;
        
        throwValidationExceptionIfTemplateHolderID(newStage.getStageID());
        
        try {
        	cn = getConnection();
        	
        	if(stageValidateUpdatedTitle(cn, newStage)){
	        	//TODO include updating of all the list properties of Stage.java?
        		
        		
	    		String sql = "UPDATE stage SET stage_treatment_plan_id_fk=?, stage_user_id_fk=?, stage_title=?, stage_description=?, stage_completed=?, `stage_order`=?, percent_complete=?, stage_is_template=? WHERE stage_id=?";
	        	
	            ps = cn.prepareStatement(sql);

	            ps.setInt(1, newStage.getTreatmentPlanID());
	            ps.setInt(2, newStage.getUserID());
	            ps.setString(3, newStage.getTitle().trim());
	            ps.setString(4, newStage.getDescription());
	            ps.setBoolean(5, newStage.isCompleted());
	            ps.setInt(6, newStage.getStageOrder());
	            ps.setInt(7, newStage.getPercentComplete());
	            ps.setBoolean(8, newStage.isTemplate());
	            ps.setInt(9, newStage.getStageID());
	
	            success = ps.executeUpdate();
        	}
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return success == 1;
	}
	
	public List<Stage> stagesGetDefaults() throws DatabaseException, ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Stage> defaultStageList = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	List<Integer> adminIDList = userGetAdminIDs(cn);
        	
        	String baseStatement = "SELECT * FROM stage WHERE stage_is_template=1 AND stage_user_id_fk in (";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, null);

    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	if(rs.getInt("stage_id") != Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){// The Stage with id=1 is the Stage that holds all of the Task templates, so should not be returned in this query
            		defaultStageList.add(stageLoadWithEmplyLists(rs.getInt("stage_id")));
            	}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return defaultStageList;
	}

	@Override
	public Stage stageLoadWithEmplyLists(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Stage stage = null;
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
        	cn = getConnection();

    		String sql = "SELECT * FROM stage WHERE stage.stage_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, stageID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	//TODO load tasks
            	List<Task> tasks = new ArrayList<>();
            	//TODO load extra tasks
            	List<Task> extraTasks = new ArrayList<>();
            	//TODO load goals
            	List<StageGoal> goals = new ArrayList<>();
            	
            	//boolean completed = rs.getInt("stage_completed") == 1;
            	//boolean inProgress = rs.getInt("") == 1;
            	//boolean isTemplate = rs.getInt("stage_is_template") == 1;
            	
            	stage = Stage.getInstance(stageID, rs.getInt("stage_treatment_plan_id_fk"), rs.getInt("stage.stage_user_id_fk"), rs.getString("stage.stage_title"), rs.getString("stage.stage_description"), rs.getInt("stage.stage_order"), tasks, extraTasks, rs.getBoolean("stage_completed"), rs.getInt("percent_complete"), goals, rs.getBoolean("stage_is_template"));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        throwValidationExceptionIfNull(stage);
        
        return stage;
	}
	
	public List<Integer> stageGetTaskIDs(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> taskIDs = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT task_generic_id, task_generic_task_type_id_fk, task_order FROM task_generic WHERE task_generic_stage_id_fk=? ORDER BY task_order");
            ps.setInt(1, stageID);
            

            rs = ps.executeQuery();

            //TODO set date completed properly
            while (rs.next()){
            	taskIDs.add(rs.getInt("task_generic_id"));
            	/*tasks.add(GenericTask.getInstanceFull(rs.getInt("task_generic_id"), rs.getInt("task_generic_stage_id_fk"), rs.getInt("task_generic_user_id_fk"), rs.getInt("task_generic_task_type_id_fk"),
            			rs.getInt("parent_task_id"), rs.getString("task_title"), rs.getString("instructions"), rs.getString("resource_link"), rs.getBoolean("task_completed"), null, rs.getInt("task_order"), 
            			rs.getBoolean("is_extra_task"), rs.getBoolean("task_is_tempalte")));*/
            }
            

        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return taskIDs;
	}
	
	
	@Override
	public void stageDelete(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        int result  = 0;
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("DELETE FROM stage WHERE stage_id=?");
            ps.setInt(1, stageID);

            result = ps.executeUpdate();
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
	
	}
	
	public List<StageGoal> stageLoadGoals(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<StageGoal> goals = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT * FROM stage_goal WHERE stage_goal_stage_id_fk=?");
            ps.setInt(1, stageID);

            rs = ps.executeQuery();

            while (rs.next()){
            	goals.add(StageGoal.getInstance(rs.getInt("stage_goal_id"), rs.getInt("stage_goal_stage_id_fk"), rs.getString("stage_goal_description")));
            }
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return goals;
	}
	
	public StageGoal stageGoalValidateAndCreate(StageGoal stageGoal) throws DatabaseException, ValidationException{
		Connection cn = null;
        
		throwValidationExceptionIfTemplateHolderID(stageGoal.getStageID());
		
        try {
	        if(stageGoalValidate(stageGoal)){
	        	cn = getConnection();
	        	stageGoalCreate(cn, stageGoal);
        	}
        }finally {
			DbUtils.closeQuietly(cn);
        }
        
        return stageGoal;
	}
	
	private boolean stageGoalValidate(StageGoal stageGoal) throws ValidationException{

        if(stageGoal.getStageID() != 0 && !stageGoal.getDescription().isEmpty()){
        	return true;
    	} else {
    		throw new ValidationException(ErrorMessages.STAGE_GOAL_VALIDATION_ERROR);
    	}

	}
	
	public List<StageGoal> copyGoalsIntoNewStage(List<StageGoal> stageGoals, int stageID) throws DatabaseException, ValidationException {
		Connection cn = null;
        List<StageGoal> copiedStageGoals = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
	        cn = getConnection();
	        for(StageGoal stageGoal : stageGoals){
	        	stageGoal.setStageID(stageID);
	        	copiedStageGoals.add(stageGoalCreate(cn, stageGoal));
	        	
        	}
        }finally {
			DbUtils.closeQuietly(cn);
        }
        
        return copiedStageGoals;
	}
	
	private StageGoal stageGoalCreate(Connection cn, StageGoal stageGoal) throws DatabaseException, ValidationException {
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        throwValidationExceptionIfTemplateHolderID(stageGoal.getStageID());
        
        try {
	        	String sql = "INSERT INTO stage_goal (stage_goal_stage_id_fk, stage_goal_description) VALUES (?, ?)";
	        	
	            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            
	            ps.setInt(1, stageGoal.getStageID());
	            ps.setString(2, stageGoal.getDescription());
	
	            int success = ps.executeUpdate();
	            
	            generatedKeys = ps.getGeneratedKeys();
	   
	            int goalID = 0;
	            while (generatedKeys.next()){
	            	goalID = generatedKeys.getInt(1);;
	            }
	            
	            stageGoal.setStageGoalID(goalID);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        
        return stageGoal;
	}
	
	@Override
	public List<Task> taskGetDefaults() throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Task> defaultTaskList = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	List<Integer> adminIDList = userGetAdminIDs(cn);
        	
        	String baseStatement = "SELECT * FROM task_generic WHERE task_is_template=1 AND task_generic_user_id_fk in (";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, null);
        	
    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	defaultTaskList.add(taskGenericLoad(rs.getInt("task_generic_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return defaultTaskList;
	}
	
	/*
	public Task taskLoad(int taskID) throws DatabaseException {
		Connection cn = null;
        Task task = null;

        try {
        	cn = getConnection();
        	
        	GenericTask genericTask = (GenericTask)taskGenericLoad(cn, taskID);
        	
    		
    		switch(genericTask.getTaskTypeID()){
    			case 1:
    				task = genericTask;
    				break;
    			case 2:
    				task = taskTwoTextBoxesLoad(cn, taskID);
    		}
        	
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(cn);
        }
        
        return task;
	}*/
	
	@Override
	public Task taskGenericLoad(int taskID) throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Task task = null;
        
        try {
        	cn = getConnection();

    		String sql = "SELECT * FROM task_generic WHERE task_generic_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, taskID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	//TODO ADD GETTING DATE COMPLETED - just setting and returning it to null now
            	task = TaskGeneric.getInstanceFull(rs.getInt("task_generic_id"), rs.getInt("task_generic_stage_id_fk"), rs.getInt("task_generic_user_id_fk"), rs.getInt("task_generic_task_type_id_fk"), rs.getInt("parent_task_id"), rs.getString("task_title"), rs.getString("instructions"), rs.getString("resource_link"), rs.getBoolean("task_completed"), null/*rs.getDate("task_date_completed")*/, rs.getInt("task_order"), rs.getBoolean("is_extra_task"), rs.getBoolean("task_is_template"));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return task;
	}
	
	public void taskTwoTextBoxesSaveNewAdditionalData(TaskTwoTextBoxes twoTextBoxesTask) throws DatabaseException, ValidationException{
		Connection cn = null;
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();

        	String sql = "INSERT INTO task_two_textboxes (task_generic_id, extra_text_label_1, extra_text_value_1, extra_text_label_2, extra_text_value_2) "
        			+ "VALUES (?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, twoTextBoxesTask.getTaskID());
            ps.setString(2, twoTextBoxesTask.getExtraTextLabel1());
            ps.setString(3, twoTextBoxesTask.getExtraTextValue1());
            ps.setString(4, twoTextBoxesTask.getExtraTextLabel2());
            ps.setString(5, twoTextBoxesTask.getExtraTextValue2());


            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	twoTextBoxesTask.setTaskID(generatedKeys.getInt(1));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
		

	}

	@Override
	public boolean taskTwoTextBoxesUpdateAdditionalData(TaskTwoTextBoxes twoTextBoxesTask) throws DatabaseException, ValidationException {
		Connection cn = null;
    	PreparedStatement ps = null;
        int success = 0;
        
        try {
        	cn = getConnection();
        	
    		String sql = "UPDATE task_two_textboxes SET extra_text_label_1=?, extra_text_value_1=?, extra_text_label_2=?, extra_text_value_2=? WHERE task_generic_id=?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, twoTextBoxesTask.getExtraTextLabel1());
            ps.setString(2, twoTextBoxesTask.getExtraTextValue1());
            ps.setString(3, twoTextBoxesTask.getExtraTextLabel2());
            ps.setString(4, twoTextBoxesTask.getExtraTextValue2());
            ps.setInt(5, twoTextBoxesTask.getTaskID()); 

            success = ps.executeUpdate();   	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return success == 1;
	}
	
	@Override
	public Task taskTwoTextBoxesLoad(int taskID) throws DatabaseException {
		Connection cn = null;
		PreparedStatement ps = null;
        ResultSet rs = null;
        Task task = null;
        
        try {
        	cn = getConnection();

    		String sql = "SELECT task_generic.*, task_two_textboxes.extra_text_label_1, task_two_textboxes.extra_text_value_1, task_two_textboxes.extra_text_label_2, task_two_textboxes.extra_text_value_2 "
    				+ "FROM task_generic INNER JOIN task_two_textboxes ON task_generic.task_generic_id = task_two_textboxes.task_generic_id WHERE task_two_textboxes.task_generic_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, taskID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	task = TaskTwoTextBoxes.getInstanceFull(rs.getInt("task_generic_id"), rs.getInt("task_generic_stage_id_fk"), rs.getInt("task_generic_user_id_fk"), rs.getInt("task_generic_task_type_id_fk"), 
            			rs.getInt("parent_task_id"), rs.getString("task_title"), rs.getString("instructions"), rs.getString("resource_link"), rs.getBoolean("task_completed"), 
            			null/*rs.getDate("task_date_completed")*/, rs.getInt("task_order"), rs.getBoolean("is_extra_task"), rs.getBoolean("task_is_template"),
            			rs.getString("extra_text_label_1"), rs.getString("extra_text_value_1"), rs.getString("extra_text_label_2"), rs.getString("extra_text_value_2"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return task;
	}

	
	@Override
	public boolean taskGenericUpdate(Task taskToUpdate) throws DatabaseException, ValidationException {
		Connection cn = null;
    	PreparedStatement ps = null;
        int success = 0;
        
        try {
        	cn = getConnection();
        	
        	if(taskValidate(cn, taskToUpdate)){

	    		String sql = "UPDATE task_generic SET task_generic_task_type_id_fk=?, task_generic_stage_id_fk=?, task_generic_user_id_fk=?, parent_task_id=?, task_title=?, instructions=?, resource_link=?, "
	    				+ "task_completed=?, task_date_completed=?, task_order=?, is_extra_task=?, task_is_template=? WHERE task_generic_id=?";
	        	
	            ps = cn.prepareStatement(sql);
	            
	            ps.setInt(1, taskToUpdate.getTaskTypeID());
	            ps.setInt(2, taskToUpdate.getStageID());
	            ps.setInt(3, taskToUpdate.getUserID());
	            ps.setInt(4, taskToUpdate.getParentTaskID());
	            ps.setString(5, taskToUpdate.getTitle().trim());
	            ps.setString(6,  taskToUpdate.getInstructions());
	            ps.setString(7, taskToUpdate.getResourceLink());
	            ps.setBoolean(8, taskToUpdate.isCompleted());
	            ps.setDate(9, null);//taskToUpdate.getDateCompleted() == null ? null : Date.valueOf(taskToUpdate.getDateCompleted()));//TODO ACTUALLY UPDATE THE DATE COMPLETED! Just setting to null now.
	            ps.setInt(10, taskToUpdate.getTaskOrder());
	            ps.setBoolean(11, taskToUpdate.isExtraTask());
	            ps.setBoolean(12, taskToUpdate.isTemplate());
	            ps.setInt(13, taskToUpdate.getTaskID());
	
	            success = ps.executeUpdate();
        	}
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return success == 1;
	}
	
	@Override
	public Task taskValidateAndCreate(Task newTask) throws DatabaseException, ValidationException{
		Connection cn = null;

        try {
        	cn= getConnection();
			if(taskValidate(cn, newTask)){
				return taskGenericCreate(cn, newTask);
			}
			
        } finally {
			DbUtils.closeQuietly(cn);
		}
        
        return null;
		
	}
	
	//TODO - bug fix - either create different validate method for updates so doesn't throw TaskTitleExists exception when updating fields of a task without changing the title or add logic in method below to do this
	private boolean taskValidate(Connection cn, Task newTask) throws ValidationException, DatabaseException{

		if(newTask.getTitle() == null || newTask.getTitle().isEmpty() || 
				newTask.getInstructions() == null || newTask.getInstructions().isEmpty() ||
				newTask.getTaskTypeID() == 0){
			throw new ValidationException(ErrorMessages.TASK_MISSING_INFO);
		}
		
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
        	cn= getConnection();

			ps = cn.prepareStatement("SELECT COUNT(*) FROM task_generic WHERE (task_generic.task_title=? AND task_generic_stage_id_fk=? AND task_generic_id!=?)");
			ps.setString(1, newTask.getTitle().trim());
			ps.setInt(2, newTask.getStageID());
			ps.setInt(3, newTask.getTaskID());

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
			throw new ValidationException(ErrorMessages.TASK_TITLE_EXISTS_FOR_STAGE);
		} else {
			return true;
		}
		
	}
	
	/**Inserts a Generic Task template into the database.  Since it is a template, it does not insert for the 
	 * fields: task_id, task_stage_id_fk, task_date_completed, or parent_task_id, and it sets is_extra_task = 0(false) and task_is_template = 1(true)
	 * @param cn
	 * @param newTask - Task object to be inserted.
	 * @return
	 * @throws DatabaseException
	 */
	private Task taskGenericCreate(Connection cn, Task newTask) throws DatabaseException{
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	String sql = "INSERT INTO task_generic (task_generic_task_type_id_fk, task_generic_stage_id_fk, task_generic_user_id_fk, "
        			+ "parent_task_id, task_title, instructions, resource_link, task_completed, task_date_completed, task_order, is_extra_task, task_is_template) "
    				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newTask.getTaskTypeID());

            ps.setInt(2, newTask.getStageID());
            ps.setInt(3, newTask.getUserID());
            ps.setInt(4, newTask.getParentTaskID());
            ps.setString(5, newTask.getTitle().trim());
            ps.setString(6, newTask.getInstructions());
            ps.setString(7, newTask.getResourceLink());
            ps.setBoolean(8, newTask.isCompleted());
            Date dateCompleted = null;//newTask.getDateCompleted()==null ? null : Date.valueOf(newTask.getDateCompleted()); //TODO ACTUALLY SET THE DATE COMPLETED> just setting to null now.
            ps.setDate(9, dateCompleted);
            ps.setInt(10, newTask.getTaskOrder());
            ps.setBoolean(11, newTask.isExtraTask());
            ps.setBoolean(12, newTask.isTemplate());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	newTask.setTaskID(generatedKeys.getInt(1));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
		
		return newTask;
	}
	
	public Map<Integer, String> taskTypesLoad() throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, String> taskTypeMap = new HashMap<>();
        
        try {
        	cn = getConnection();
        	String sql = "SELECT * FROM task_type;";
    		ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
   
            while (rs.next()){
            	taskTypeMap.put(rs.getInt("task_type_id"), rs.getString("task_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
		
		return taskTypeMap;
	}

	@Override
	public TreatmentIssue treatmentIssueValidateAndCreate(TreatmentIssue treatmentIssue, int userID) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	
        	if(treatmentIssueValidateNewName(cn, treatmentIssue.getTreatmentIssueName(), userID)){
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
	 * @param issueName Treatment Issue title of new issue that user wants to create
	 * @param userID id of the user
	 * @return true if the the combination is valid and does not exist in the database. false if the combination exists and is therefore invalid.
	 * @throws SQLException
	 * @throws ValidationException 
	 * @throws DatabaseException
	 */
	private boolean treatmentIssueValidateNewName(Connection cn, String issueName, int userID) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {
        	
        	if(issueName.isEmpty() || issueName ==""){
        		throw new ValidationException(ErrorMessages.ISSUE_NAME_MISSING);
        	}
        	
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

	//TODO - change to make sure this works with a List of adminIDs
	@Override
	public ArrayList<TreatmentIssue> treatmentIssueGetDefaults() throws DatabaseException{
		Connection cn = null;
		ArrayList<TreatmentIssue> issues = new ArrayList<>();
		
		try{
			cn = getConnection();
			List<Integer> userIDs = userGetAdminIDs(cn);
			for(int adminUserID : userIDs){
				issues.addAll(treatmentIssueGetListByUserID(adminUserID));
			}
			
		} finally {
			DbUtils.closeQuietly(cn);
		}

		return issues;
	}


    @Override
	public ArrayList<TreatmentIssue> treatmentIssueGetListByUserID(int userID) throws DatabaseException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList<TreatmentIssue> issues = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	String sql = "SELECT treatment_issue.treatment_issue_id, treatment_issue.issue, user.user_id "
            		+ "FROM user INNER JOIN treatment_issue ON user.user_id = treatment_issue.treatment_issue_user_id_fk "
            		+ "WHERE user.user_id=?";
        	
            ps = cn.prepareStatement(sql);
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
