package com.cggcoding.utils.database;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.*;
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
public class MySQLActionHandler implements Serializable, DatabaseActionHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DatabaseConnection mysqlConn;

    public MySQLActionHandler(){
    	this.mysqlConn = new MySQLConnection();
    }

    /* (non-Javadoc)
	 * @see com.cggcoding.utils.database.DatabaseActionHandler#getConnection()
	 */
	@Override
    public Connection getConnection() throws DatabaseException{
		Connection cn = null;
		
		cn = mysqlConn.getConnection();
		if(cn == null){
			throw new DatabaseException(ErrorMessages.CONNECTION_IS_NULL);
		}
		
		return cn;
    }
	
	@Override
	public boolean throwValidationExceptionIfTemplateHolderID(int templateHolderObjectID) throws ValidationException{
		if(templateHolderObjectID == Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){
			throw new ValidationException(ErrorMessages.DEFAULTS_HOLDER_ID_SELECTED);
		}
		
		return true;
	}
	
	@Override
	public boolean throwValidationExceptionIfNull(Object o) throws ValidationException{
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

    //XXX can I move the switch statement into the User model?
    @Override
	public User userLoadInfo(String email, String password) throws DatabaseException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rsUserInfo = null;
        User user = null;
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT user.user_id, user.email, user.active_treatment_plan_id, user_role.role FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user.email)=?) AND ((user.password)=?))");
            ps.setString(1, email);
            ps.setString(2, password);

            rsUserInfo = ps.executeQuery();
            
            //XXX - replace the use of downcasting? - e.g. UserClient.setActiveTreatmentPlanID
            // see http://programmers.stackexchange.com/questions/258655/ood-java-inheritance-and-access-to-child-methods-via-casting 
            while (rsUserInfo.next()){
            	switch (rsUserInfo.getString("role")){
            		case "admin":
            			user = new UserAdmin(rsUserInfo.getInt("user_id"), rsUserInfo.getString("email"));
            			user.addRole("admin");
            			user.setRole("admin");
            			break;
            		case "therapist":
            			user = new UserTherapist(rsUserInfo.getInt("user_id"), rsUserInfo.getString("email"));
            			user.addRole("therapist");
            			user.setRole("therapist");
            			break;
            		case "client":
            			user = new UserClient(rsUserInfo.getInt("user_id"), rsUserInfo.getString("email"));
            			user.addRole("client");
            			user.setRole("client");
            			((UserClient)user).setActiveTreatmentPlanId(rsUserInfo.getInt("active_treatment_plan_id"));
            			break;
            	}
            }
            

        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rsUserInfo);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }

        return user;
    }
    
    //XXX Make this public and called from User class?
    private List<Integer> userGetAdminIDs(Connection cn) throws DatabaseException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> adminIDList = new ArrayList<>();
        
        try {
    		String sql = "SELECT user.user_id FROM user_role INNER JOIN (user) ON user_role.user_role_id = user.user_user_role_id_fk WHERE (((user_role.user_role_id)=" + Constants.ADMIN_ROLE_ID + "))";    	

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
    public Map<Integer, UserClient> userGetClientsByTherapistID(int therapistID) throws DatabaseException{
    	Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, UserClient> clients = new HashMap<>();
        
        try {
        	cn = getConnection();
            ps = cn.prepareStatement("SELECT therapist_user_id_client_user_id_maps.therapist_user_id, "
            		+ "therapist_user_id_client_user_id_maps.client_user_id, user.email, user.password, "
            		+ "user.user_user_role_id_fk, user.active_treatment_plan_id, user_role.role "
            		+ "FROM user_role INNER JOIN ((user) INNER JOIN therapist_user_id_client_user_id_maps "
            		+ "ON user.user_id = therapist_user_id_client_user_id_maps.client_user_id) "
            		+ "ON user_role.user_role_id = user.user_user_role_id_fk "
            		+ "WHERE (((therapist_user_id_client_user_id_maps.therapist_user_id)=?))");

            ps.setInt(1, therapistID);


            rs = ps.executeQuery();


            while (rs.next()){
            	UserClient client = new UserClient(rs.getInt("client_user_id"), rs.getString("email"));
            	client.setRoleID(rs.getInt("user_user_role_id_fk"));
            	client.addRole(rs.getString("role"));
                clients.put(client.getUserID(), client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
		}
        return clients;
    }
    
    @Override
	public List<TreatmentPlan> userGetClientTreatmentPlans(int clientUserID, boolean inProgress, boolean isCompleted) throws DatabaseException, ValidationException {
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<TreatmentPlan> assignedTreatmentPlans = new ArrayList<>();
        
        try {
        	cn = getConnection();

    		ps = cn.prepareStatement("SELECT * FROM treatment_plan WHERE treatment_plan_user_id_fk = ? AND in_progress=? AND treatment_plan_completed=?");
    		ps.setInt(1, clientUserID);
    		ps.setBoolean(2, inProgress);
    		ps.setBoolean(3, isCompleted);
            
    		rs = ps.executeQuery();
   
            while (rs.next()){
            	assignedTreatmentPlans.add(treatmentPlanLoadBasic(cn, rs.getInt("treatment_plan_id")));
            	
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        
        return assignedTreatmentPlans;
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
        	
        	String orderByClause = "ORDER BY treatment_plan_title";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, orderByClause);
        	
    		ps = cn.prepareStatement(sql);
    		
    		ps.setBoolean(1, true);
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+2, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	if(rs.getInt("treatment_plan_id") != Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){ //TreatmentPlan with id=1 is the Plan that holds all Stage Defaults and so should not be included in the results of this query.
            		defaultPlanList.add(treatmentPlanLoadBasic(cn, rs.getInt("treatment_plan_id")));
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
    public TreatmentPlan treatmentPlanLoadBasic(Connection cn, int treatmentPlanID) throws SQLException, ValidationException{
    	PreparedStatement ps = null;
        ResultSet rsPlanInfo = null;
        TreatmentPlan plan = null;
        
        throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
            ps = cn.prepareStatement("SELECT * from treatment_plan WHERE treatment_plan_id=?");
            ps.setInt(1, treatmentPlanID);


            rsPlanInfo = ps.executeQuery();

            while (rsPlanInfo.next()){
            	plan = TreatmentPlan.getInstanceBasic(rsPlanInfo.getInt("treatment_plan_id"), rsPlanInfo.getInt("treatment_plan_user_id_fk"), 
            			rsPlanInfo.getString("treatment_plan_title"), rsPlanInfo.getString("treatment_plan_description"), rsPlanInfo.getInt("treatment_plan_treatment_issue_id_fk"),
            			rsPlanInfo.getBoolean("in_progress"), rsPlanInfo.getBoolean("treatment_plan_is_template"), rsPlanInfo.getBoolean("treatment_plan_completed"),
            			rsPlanInfo.getInt("current_stage_index"), rsPlanInfo.getInt("active_view_stage_index"));
            	
            }
            
        } finally {
        	DbUtils.closeQuietly(rsPlanInfo);
			DbUtils.closeQuietly(ps);
        }

        throwValidationExceptionIfNull(plan);
        
        return plan;
    }
    
	@Override
    public List<Integer> treatmentPlanGetStageIDs(Connection cn, int treatmentPlanID) throws SQLException, ValidationException {
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> stageIDs = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	ps = cn.prepareStatement("SELECT stage_id FROM stage WHERE stage_treatment_plan_id_fk=? ORDER BY stage_order");
            ps.setInt(1, treatmentPlanID);


            rs = ps.executeQuery();

            while (rs.next()){
            	stageIDs.add(rs.getInt("stage_id"));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }

        return stageIDs;
    }
    
    @Override
	public TreatmentPlan treatmentPlanCreateBasic(Connection cn, TreatmentPlan treatmentPlan) throws SQLException, ValidationException{		
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	String sql = "INSERT INTO treatment_plan (treatment_plan_user_id_fk, treatment_plan_treatment_issue_id_fk, treatment_plan_title, treatment_plan_description, "
        			+ "current_stage_index, active_view_stage_index, in_progress, treatment_plan_is_template) "
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

        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        throwValidationExceptionIfNull(treatmentPlan);
        
        return treatmentPlan;
	}
    
    @Override
	public boolean treatmentPlanValidateUpdatedTitle(Connection cn, TreatmentPlan treatmentPlan) throws ValidationException, SQLException{
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

    @Override
	public void treatmentPlanUpdateBasic(Connection cn, TreatmentPlan treatmentPlan) throws SQLException, ValidationException {
		PreparedStatement ps = null;
        
        throwValidationExceptionIfNull(treatmentPlan);
        
        try {
        	String sql = "UPDATE treatment_plan SET treatment_plan_user_id_fk=?, treatment_plan_treatment_issue_id_fk=?, treatment_plan_title=?, treatment_plan_description=?, current_stage_index=?, "
        			+ "active_view_stage_index=?, in_progress=?, treatment_plan_is_template=?, treatment_plan_completed=? WHERE treatment_plan_id=?";
        	
            ps = cn.prepareStatement(sql);
            
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

        } finally {
			DbUtils.closeQuietly(ps);
        }
	}
    
    @Override
	public boolean treatmentPlanValidateNewTitle(Connection cn, int userID, String planTitle) throws ValidationException, SQLException{
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
	
	@Override
	public void treatmentPlanDelete(Connection cn, int treatmentPlanID) throws SQLException, ValidationException {
		PreparedStatement ps = null;

		try {
            ps = cn.prepareStatement("DELETE FROM treatment_plan WHERE treatment_plan_id=?");
            ps.setInt(1, treatmentPlanID);

            ps.executeUpdate();
		
		} finally {
			DbUtils.closeQuietly(ps);
	    }

	}
	
	/** Validating a new Stage title involves checking is there is already a match for the combination of the new title and the userID.
	 * If there is a match then the new title is invalid
	 * @param cn
	 * @param newStage - A Stage object containing at least a title and userID
	 * @return true if valid combination, false throws ValidationException
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	@Override
	public boolean stageValidateNewTitle(Connection cn, Stage newStage) throws ValidationException, SQLException{
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;

    	try {

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
	@Override
	public boolean stageValidateUpdatedTitle(Connection cn, Stage newStage) throws ValidationException, SQLException{
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
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

        } finally {
			DbUtils.closeQuietly(stageCount);
			DbUtils.closeQuietly(ps);
		}
        
		if(comboExists > 0){
			throw new ValidationException(ErrorMessages.STAGE_TITLE_EXISTS);
		} else {
			return true;
		}
	}
	
	
	@Override
	public Stage stageCreateBasic(Connection cn, Stage newStage) throws ValidationException, SQLException{
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
    		String sql = "INSERT INTO stage (stage_user_id_fk, stage_treatment_plan_id_fk, stage_title, stage_description, stage_completed, stage_order, percent_complete, stage_in_progress, stage_is_template) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newStage.getUserID());
            ps.setInt(2, newStage.getTreatmentPlanID());
            ps.setString(3, newStage.getTitle().trim());
            ps.setString(4, newStage.getDescription());
            ps.setBoolean(5,  newStage.isCompleted());
            ps.setInt(6, newStage.getStageOrder());
            ps.setDouble(7, newStage.getPercentComplete());
            ps.setBoolean(8, newStage.isInProgress());
            ps.setBoolean(9, newStage.isTemplate());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	newStage.setStageID(generatedKeys.getInt(1));
            }
        	
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        
        return newStage;
	}

	
	@Override
	public boolean stageUpdateBasic(Connection cn, Stage stage) throws ValidationException, SQLException{
    	PreparedStatement ps = null;
        int success = 0;
        
        throwValidationExceptionIfTemplateHolderID(stage.getStageID());
        
        try {
        		
    		String sql = "UPDATE stage SET stage_treatment_plan_id_fk=?, stage_user_id_fk=?, stage_title=?, stage_description=?, stage_completed=?, `stage_order`=?, percent_complete=?, stage_in_progress=?, stage_is_template=? WHERE stage_id=?";
        	
            ps = cn.prepareStatement(sql);

            ps.setInt(1, stage.getTreatmentPlanID());
            ps.setInt(2, stage.getUserID());
            ps.setString(3, stage.getTitle().trim());
            ps.setString(4, stage.getDescription());
            ps.setBoolean(5, stage.isCompleted());
            ps.setInt(6, stage.getStageOrder());
            ps.setDouble(7, stage.getPercentComplete());
            ps.setBoolean(8, stage.isInProgress());
            ps.setBoolean(9, stage.isTemplate());
            ps.setInt(10, stage.getStageID());

            success = ps.executeUpdate();
        	
        } finally {
			DbUtils.closeQuietly(ps);
        }
        
        return success == 1;
	}
	
	//TODO implement method
	public Stage stageCopy(int stageIDBeingCopied, int userID, int treatmentPlanID, int stageOrder, boolean isTemplate ){
		return null;
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
        	
        	String orderByClause = "ORDER BY stage_title";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, orderByClause);

    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	if(rs.getInt("stage_id") != Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID){// The Stage with id=1 is the Stage that holds all of the Task templates, so should not be returned in this query
            		defaultStageList.add(Stage.loadBasic(cn, rs.getInt("stage_id")));
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
	public Stage stageLoadBasic(Connection cn, int stageID) throws SQLException, ValidationException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Stage stage = null;
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
    		String sql = "SELECT * FROM stage WHERE stage.stage_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, stageID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	List<Task> tasks = new ArrayList<>();
            	List<Task> extraTasks = new ArrayList<>();
            	List<StageGoal> goals = new ArrayList<>();
            	
            	//boolean completed = rs.getInt("stage_completed") == 1;
            	//boolean inProgress = rs.getInt("") == 1;
            	//boolean isTemplate = rs.getInt("stage_is_template") == 1;
            	
            	stage = Stage.getInstance(stageID, rs.getInt("stage_treatment_plan_id_fk"), rs.getInt("stage.stage_user_id_fk"), rs.getString("stage.stage_title"), rs.getString("stage.stage_description"), rs.getInt("stage.stage_order"), tasks, extraTasks, rs.getBoolean("stage_completed"), rs.getDouble("percent_complete"), goals, rs.getBoolean("stage_in_progress"), rs.getBoolean("stage_is_template"));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }
        
        throwValidationExceptionIfNull(stage);
        
        return stage;
	}
	
	@Override
	public List<Task> stageLoadTasks(Connection cn, int stageID) throws SQLException {
		PreparedStatement ps = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();
        
        //throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
            ps = cn.prepareStatement("SELECT task_generic_id, task_generic_task_type_id_fk, task_order FROM task_generic WHERE task_generic_stage_id_fk=? ORDER BY task_order");
            ps.setInt(1, stageID);
            

            rs = ps.executeQuery();

            while (rs.next()){
            	tasks.add(Task.load(cn, rs.getInt("task_generic_id")));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);

        }

        return tasks;
	}

	@Override
	public List<StageGoal> stageLoadGoals(Connection cn, int stageID) throws SQLException, ValidationException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<StageGoal> goals = new ArrayList<>();
        
        throwValidationExceptionIfTemplateHolderID(stageID);
        
        try {
            ps = cn.prepareStatement("SELECT * FROM stage_goal WHERE stage_goal_stage_id_fk=?");
            ps.setInt(1, stageID);

            rs = ps.executeQuery();

            while (rs.next()){
            	goals.add(StageGoal.getInstance(rs.getInt("stage_goal_id"), rs.getInt("stage_goal_stage_id_fk"), rs.getString("stage_goal_description")));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }

        return goals;
	}
	
	@Override
	public StageGoal stageGoalCreate(Connection cn, StageGoal stageGoal) throws SQLException, ValidationException {
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

        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        
        return stageGoal;
	}
	
	@Override
	public void stageDelete(Connection cn, int stageID) throws SQLException, ValidationException{
    	PreparedStatement ps = null;
        
    	try{
	        ps = cn.prepareStatement("DELETE FROM stage WHERE stage_id=?");
	        ps.setInt(1, stageID);
	
	        ps.executeUpdate();
    	}finally{
    		DbUtils.closeQuietly(ps);
    	}
	
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
        	
        	String orderByClause = "ORDER BY task_title";
        	
        	String sql = SqlBuilders.includeMultipleIntParams(baseStatement, adminIDList, orderByClause);
        	
    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	defaultTaskList.add(taskGenericLoad(cn, rs.getInt("task_generic_id")));
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
	
	@Override
	public Task taskGenericLoad(Connection cn, int taskID) throws SQLException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Task task = null;
        
        try {
    		String sql = "SELECT * FROM task_generic WHERE task_generic_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, taskID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	Timestamp timestamp = rs.getTimestamp("task_date_completed");
            	LocalDateTime dateCompleted = convertTimestampToLocalDateTime(timestamp);
            	
            	task = TaskGeneric.getInstanceFull(rs.getInt("task_generic_id"), rs.getInt("task_generic_stage_id_fk"), rs.getInt("task_generic_user_id_fk"), rs.getInt("task_generic_task_type_id_fk"), 
            			rs.getInt("parent_task_id"), rs.getString("task_title"), rs.getString("instructions"), rs.getString("resource_link"), rs.getBoolean("task_completed"), 
            			dateCompleted, rs.getInt("task_order"), rs.getBoolean("is_extra_task"), 
            			rs.getBoolean("task_is_template"), rs.getInt("template_id"), rs.getInt("repetitions"));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }
        
        return task;
	}
	@Override
	public void taskTwoTextBoxesCreateAdditionalData(Connection cn, TaskTwoTextBoxes twoTextBoxesTask) throws SQLException{
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
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

        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
		

	}
	
	@Override
	public boolean taskTwoTextBoxesUpdateAdditionalData(Connection cn, TaskTwoTextBoxes twoTextBoxesTask) throws SQLException, ValidationException {
    	PreparedStatement ps = null;
        int success = 0;
        
        try {
        	
    		String sql = "UPDATE task_two_textboxes SET extra_text_label_1=?, extra_text_value_1=?, extra_text_label_2=?, extra_text_value_2=? WHERE task_generic_id=?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, twoTextBoxesTask.getExtraTextLabel1());
            ps.setString(2, twoTextBoxesTask.getExtraTextValue1());
            ps.setString(3, twoTextBoxesTask.getExtraTextLabel2());
            ps.setString(4, twoTextBoxesTask.getExtraTextValue2());
            ps.setInt(5, twoTextBoxesTask.getTaskID()); 

            success = ps.executeUpdate();   	

        } finally {
			DbUtils.closeQuietly(ps);
        }
        
        return success == 1;
	}
	
	@Override
	public Task taskTwoTextBoxesLoadAdditionalData(Connection cn, TaskGeneric genericTask) throws SQLException {
		PreparedStatement ps = null;
        ResultSet rs = null;
        TaskTwoTextBoxes task = null;
        
        try {
    		String sql = "SELECT * FROM cggcodin_doitright.task_two_textboxes WHERE task_two_textboxes.task_generic_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, genericTask.getTaskID());
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	task = TaskTwoTextBoxes.addDataToGenericTask(genericTask, rs.getString("extra_text_label_1"), rs.getString("extra_text_value_1"), rs.getString("extra_text_label_2"), rs.getString("extra_text_value_2"));
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }
        
        return task;
	}
	
	@Override
	public boolean taskGenericUpdate(Connection cn, Task taskToUpdate) throws SQLException {

    	PreparedStatement ps = null;
        int success = 0;
        
        try {

	    		String sql = "UPDATE task_generic SET task_generic_task_type_id_fk=?, task_generic_stage_id_fk=?, task_generic_user_id_fk=?, parent_task_id=?, task_title=?, instructions=?, resource_link=?, "
	    				+ "task_completed=?, task_date_completed=?, task_order=?, is_extra_task=?, task_is_template=?, template_id=?, repetitions=? WHERE task_generic_id=?";
	        	
	            ps = cn.prepareStatement(sql);
	            
	            ps.setInt(1, taskToUpdate.getTaskTypeID());
	            ps.setInt(2, taskToUpdate.getStageID());
	            ps.setInt(3, taskToUpdate.getUserID());
	            ps.setInt(4, taskToUpdate.getParentTaskID());
	            ps.setString(5, taskToUpdate.getTitle().trim());
	            ps.setString(6,  taskToUpdate.getInstructions());
	            ps.setString(7, taskToUpdate.getResourceLink());
	            ps.setBoolean(8, taskToUpdate.isCompleted());
	            ps.setTimestamp(9, convertLocalTimeDateToTimstamp(taskToUpdate.getDateCompleted()));
	            ps.setInt(10, taskToUpdate.getTaskOrder());
	            ps.setBoolean(11, taskToUpdate.isExtraTask());
	            ps.setBoolean(12, taskToUpdate.isTemplate());
	            ps.setInt(13, taskToUpdate.getTemplateID());
	            ps.setInt(14, taskToUpdate.getRepetitions());
	            ps.setInt(15, taskToUpdate.getTaskID());
	
	            success = ps.executeUpdate();

        } finally {
			DbUtils.closeQuietly(ps);
        }
        
        return success == 1;
	}
	
	//TODO - bug fix - either create different validate method for updates so doesn't throw TaskTitleExists exception when updating fields of a task without changing the title or add logic in method below to do this
	@Override
	public boolean taskValidate(Connection cn, Task newTask) throws ValidationException, SQLException{
		PreparedStatement ps = null;
        ResultSet rsStageCount = null;
        int comboExists = 0;
	        
        try {
			ps = cn.prepareStatement("SELECT COUNT(*) FROM task_generic WHERE (task_generic.task_title=? AND task_generic_stage_id_fk=? AND task_generic_id!=?)");
			ps.setString(1, newTask.getTitle().trim());
			ps.setInt(2, newTask.getStageID());
			ps.setInt(3, newTask.getTaskID());

			rsStageCount = ps.executeQuery();

			while (rsStageCount.next()){
			    comboExists = rsStageCount.getInt("COUNT(*)");
			}

        } finally {
			DbUtils.closeQuietly(rsStageCount);
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
	@Override
	public Task taskGenericCreate(Connection cn, Task newTask) throws SQLException{
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	String sql = "INSERT INTO task_generic (task_generic_task_type_id_fk, task_generic_stage_id_fk, task_generic_user_id_fk, parent_task_id, task_title, "
        			+ "instructions, resource_link, task_completed, task_date_completed, task_order, is_extra_task, task_is_template, template_id, repetitions) "
    				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newTask.getTaskTypeID());

            ps.setInt(2, newTask.getStageID());
            ps.setInt(3, newTask.getUserID());
            ps.setInt(4, newTask.getParentTaskID());
            ps.setString(5, newTask.getTitle().trim());
            ps.setString(6, newTask.getInstructions());
            ps.setString(7, newTask.getResourceLink());
            ps.setBoolean(8, newTask.isCompleted());
            ps.setTimestamp(9, convertLocalTimeDateToTimstamp(newTask.getDateCompleted()));
            ps.setInt(10, newTask.getTaskOrder());
            ps.setBoolean(11, newTask.isExtraTask());
            ps.setBoolean(12, newTask.isTemplate());
            ps.setInt(13, newTask.getTemplateID());
            ps.setInt(14, newTask.getRepetitions());

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	newTask.setTaskID(generatedKeys.getInt(1));
            }
        	
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
		
		return newTask;
	}
	
	
	@Override
	public void taskDelete(Connection cn, int taskID) throws SQLException {
		PreparedStatement ps = null;

		try {
            ps = cn.prepareStatement("DELETE FROM task_generic WHERE task_generic_id=?");
            ps.setInt(1, taskID);

            ps.executeUpdate();

		} finally {
			DbUtils.closeQuietly(ps);
	    }

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
	public TreatmentIssue treatmentIssueCreate(Connection cn, TreatmentIssue treatmentIssue, int userID) throws ValidationException, SQLException{
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	
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

        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
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
	@Override
	public boolean treatmentIssueValidateNewName(Connection cn, String issueName, int userID) throws ValidationException, SQLException{
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

	//XXX I need to move this logic into a model and from there call treatmentIssueGetListByUserID()
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
            		+ "WHERE user.user_id=? ORDER BY issue";
        	
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
    
    private Timestamp convertLocalTimeDateToTimstamp(LocalDateTime ldt){
    	Timestamp timestamp = null;
    	
        if(ldt != null){
        	timestamp = Timestamp.valueOf(ldt);
        }
        
        return timestamp; 
    }

    private LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp){
    	LocalDateTime ldt = null;
    	if(timestamp != null){
    		ldt = timestamp.toLocalDateTime();
    	}
    	return ldt;
    }



}
