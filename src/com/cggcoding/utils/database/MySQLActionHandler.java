package com.cggcoding.utils.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.models.tasktypes.GenericTask;
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
    
    
    private List<Integer> userGetAdminIDs(Connection cn) throws DatabaseException{
    	//Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> adminIDList = new ArrayList<>();
        
        try {
        	//cn = getConnection();

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
			//DbUtils.closeQuietly(cn);
        }
        
        return adminIDList;
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
        
        return treatmentPlan;
    }
    
	
	private boolean treatmentPlanValidateNewTitle(Connection cn, int userID, String planTitle) throws ValidationException, DatabaseException{
    	PreparedStatement ps = null;
        ResultSet issueCount = null;
        int comboExists = 0;
        
        try {

            ps = cn.prepareStatement("SELECT COUNT(*)  FROM treatment_plan WHERE (((treatment_plan.title)=?) AND ((treatment_plan.treatment_plan_user_id_fk)=?))");
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
	

	private TreatmentPlan treatmentPlanCreateBasic(Connection cn, TreatmentPlan treatmentPlan) throws DatabaseException{		
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();

        	String sql = "INSERT INTO `cggcodin_doitright`.`treatment_plan` (`treatment_plan_user_id_fk`, `treatment_plan_treatment_issue_id_fk`, `title`, `description`, `current_stage_index`, `active_view_stage_index`, `in_progress`) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, treatmentPlan.getUserID());
            ps.setInt(2, treatmentPlan.getTreatmentIssueID());
            ps.setString(3, treatmentPlan.getTitle().trim());
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
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
        }
        
        return treatmentPlan;
	}
	
	
	public Stage stageTemplateValidateAndCreate(Stage stageTemplate) throws ValidationException, DatabaseException{
		Connection cn = null;

        try {
        	cn= getConnection();
			if(stageTemplateValidateNewTitle(cn, stageTemplate)){
				return stageTemplateCreate(cn, stageTemplate);
			}
			
        } finally {
			DbUtils.closeQuietly(cn);
		}
        
        return null;
	}
	
	/** Validating a new Stage Template title involves checking is there is already a match for the combination of the new title and the userID.
	 * If there is a match then the new title is invalid
	 * @param cn
	 * @param newStage - A Stage object containing at least a title and userID
	 * @return true if valid combination, false throws ValidationException
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	private boolean stageTemplateValidateNewTitle(Connection cn, Stage newStage) throws ValidationException, DatabaseException{
		//Connection cn = null;
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
        	cn= getConnection();
			ps = cn.prepareStatement("SELECT COUNT(*) FROM stage WHERE (((stage.title)=?) AND ((stage.stage_user_id_fk)=?))");
			ps.setString(1, newStage.getTitle().trim());
			ps.setInt(2, newStage.getUserID());

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
	
	/**Validating a new Stage Template title involves checking is there is already a match for the combination of the new title and the userID. However, since
	 * in case the title wasn't actually changed, need to also exclude any results that have a stageID equal to the stageID of the Stage parameter
	 * @param cn
	 * @param newStage
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	private boolean stageTemplateValidateUpdatedTitle(Connection cn, Stage newStage) throws ValidationException, DatabaseException{
		//Connection cn = null;
		PreparedStatement ps = null;
        ResultSet stageCount = null;
        int comboExists = 0;
	        
        try {
        	cn= getConnection();
			ps = cn.prepareStatement("SELECT COUNT(*) FROM stage WHERE ((stage.title = ?) AND (stage.stage_id != ?) AND (stage.stage_user_id_fk = ?))");
			ps.setString(1, newStage.getTitle().trim());
			ps.setInt(2, newStage.getStageID());
			ps.setInt(3, newStage.getUserID());

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
	
	private Stage stageTemplateCreate(Connection cn, Stage newStageTemplate) throws ValidationException, DatabaseException{
		//Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();

    		String sql = "INSERT INTO `cggcodin_doitright`.`stage` (`stage_user_id_fk`, `title`, `description`, `stage_order`, `is_template`) "
            		+ "VALUES (?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newStageTemplate.getUserID());
            ps.setString(2, newStageTemplate.getTitle().trim());
            ps.setString(3, newStageTemplate.getDescription().trim());
            ps.setInt(4, newStageTemplate.getStageOrder());
            ps.setInt(5, 1);

            int success = ps.executeUpdate();
            
            generatedKeys = ps.getGeneratedKeys();
   
            while (generatedKeys.next()){
            	newStageTemplate.setStageID(generatedKeys.getInt(1));
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			//DbUtils.closeQuietly(cn);
        }
        
        return newStageTemplate;
	}

	
	@Override
	public boolean stageTemplateUpdate(Stage newStageTemplate) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        int success = 0;
        
        try {
        	cn = getConnection();
        	
        	if(stageTemplateValidateUpdatedTitle(cn, newStageTemplate)){
	        	//TODO needs to include updating of all the list properties of Stage.java
        		
        		
	    		String sql = "UPDATE stage SET title=?, description=?, completed=?, `stage_order`=?, percent_complete=?, is_template=? WHERE stage_id=?";
	        	
	            ps = cn.prepareStatement(sql);
	            
	            ps.setString(1, newStageTemplate.getTitle().trim());
	            ps.setString(2, newStageTemplate.getDescription().trim());
	            ps.setBoolean(3, newStageTemplate.isCompleted());
	            ps.setInt(4, newStageTemplate.getStageOrder());
	            ps.setInt(5, newStageTemplate.getPercentComplete());
	            ps.setBoolean(6, newStageTemplate.isTemplate());
	            ps.setInt(7, newStageTemplate.getStageID());
	
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
	
	public List<Stage> stagesGetDefaults() throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Stage> defaultStageList = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	List<Integer> adminIDList = userGetAdminIDs(cn);
        	
        	String sql = SqlBuilders.includeMultipleIntParams("SELECT * FROM `cggcodin_doitright`.`stage` WHERE `stage_user_id_fk` in (", adminIDList);
        	/*StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `cggcodin_doitright`.`stage` WHERE `stage_user_id_fk` in (");
        	
        	for(Integer adminID : adminIDList){
        		sqlBuilder.append("?,");
        	}	
        	
        	sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
        	sqlBuilder.append(")");
    		String sql = sqlBuilder.toString();*/
    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	defaultStageList.add(stageLoad(rs.getInt("stage_id")));
            	
            	//defaultStageList.add(Stage.getInstance(rs.getInt("stage.stage_id"), rs.getInt("stage_user_id_fk"), rs.getString("stage.title"), rs.getString("stage.description"), rs.getInt("stage.stage_order")));
            }
        	//}

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

	
	/* TODO - delete? - realized I don't ever need the user ID to get a stage from the database - could keep it as parameter for an extra security measure I suppose.
	@Override
	public Stage stageLoad(int userID, int stageID) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Stage stage = null;
        
        try {
        	cn = getConnection();

    		String sql = "SELECT * FROM `cggcodin_doitright`.`stage` WHERE `stage_user_id_fk`=? AND `stage.stage_id`=?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, userID);
            ps.setInt(2, stageID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	stage = Stage.getInstance(stageID, userID, rs.getString("stage.title"), rs.getString("stage.description"), rs.getInt("stage.stage_order"));
            	
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return stage;
	}
	*/
	@Override
	public Stage stageLoad(int stageID) throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Stage stage = null;
        
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
            	
            	boolean completed = rs.getInt("completed") == 1;
            	//boolean inProgress = rs.getInt("") == 1;
            	boolean isTemplate = rs.getInt("is_template") == 1;
            	
            	stage = Stage.getInstance(stageID, rs.getInt("stage_treatment_plan_id_fk"), rs.getInt("stage.stage_user_id_fk"), rs.getString("stage.title"), rs.getString("stage.description"), rs.getInt("stage.stage_order"), tasks, extraTasks, completed, rs.getInt("percent_complete"), goals, isTemplate);
            }
        	

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return stage;
	}
	
	public StageGoal stageGoalValidateAndCreate(StageGoal goal) throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int goalID = 0;
        
        try {
	        if(goal.getStageID() != 0 && !goal.getDescription().isEmpty()){
	        	
	        	cn = getConnection();
	
	        	String sql = "INSERT INTO stage_goal (stage_goal_stage_id_fk, description) VALUES (?, ?)";
	        	
	            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            
	            ps.setInt(1, goal.getStageID());
	            ps.setString(2, goal.getDescription());
	
	            int success = ps.executeUpdate();
	            
	            generatedKeys = ps.getGeneratedKeys();
	   
	            while (generatedKeys.next()){
	            	goalID = generatedKeys.getInt(1);;
	            }
	            
	            goal.setStageGoalID(goalID);
        	} else {
        		throw new ValidationException(ErrorMessages.STAGE_GOAL_VALIDATION_ERROR);
        	}
        		

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } catch (ValidationException e){
        	e.printStackTrace();
        }finally {
        	DbUtils.closeQuietly(generatedKeys);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
        }
        
        return goal;
	}
	
	public List<Task> taskGetDefaults() throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        List<Task> defaultTaskList = new ArrayList<>();
        
        try {
        	cn = getConnection();
        	
        	List<Integer> adminIDList = userGetAdminIDs(cn);
        	
        	String sql = SqlBuilders.includeMultipleIntParams("SELECT * FROM task_generic WHERE task_generic_user_id_fk in (", adminIDList);
        	
    		ps = cn.prepareStatement(sql);
    		
    		for(int i = 0; i < adminIDList.size(); i++){
    			ps.setInt(i+1, adminIDList.get(i));
    		}
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	defaultTaskList.add(taskGenericLoad(rs.getInt("task_id")));
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
	
	public Task taskGenericLoad(int taskID) throws DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Task task = null;
        
        try {
        	cn = getConnection();

    		String sql = "SELECT * FROM task_generic WHERE task_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, taskID);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	task = GenericTask.getInstance(rs.getInt("task_id"), rs.getInt("task_generic_stage_id_fk"), rs.getInt("task_generic_user_id_fk"), rs.getInt("task_generic_task_type_id_fk"), rs.getInt("parent_task_id"), rs.getString("title"), rs.getString("instructions"), rs.getString("resource_link"), rs.getInt("task_order"), rs.getBoolean("is_extra_task"), rs.getBoolean("is_template"));
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
	
	public Task taskTemplateValidateAndCreate(Task newTask) throws DatabaseException, ValidationException{
		Connection cn = null;

        try {
        	cn= getConnection();
			if(taskTemplateValidate(cn, newTask)){
				return taskGenericCreate(cn, newTask);
			}
			
        } finally {
			DbUtils.closeQuietly(cn);
		}
        
        return null;
		
	}
	
	private boolean taskTemplateValidate(Connection cn, Task newTask) throws ValidationException{

		if(newTask.getTitle() == null || newTask.getTitle().isEmpty() || 
				newTask.getInstructions() == null || newTask.getInstructions().isEmpty() ||
				newTask.getTaskTypeID() == 0){
			throw new ValidationException(ErrorMessages.TASK_MISSING_INFO);
		}
		
		return true;
	}
	
	/**Inserts a Generic Task template into the database.  Since it is a template, it does not insert for the 
	 * fields: task_id, task_stage_id_fk, date_completed, or parent_task_id, and it sets is_extra_task = 0(false) and is_template = 1(true)
	 * @param cn
	 * @param newTask - Task object to be inserted.
	 * @return
	 * @throws DatabaseException
	 */
	private Task taskGenericCreate(Connection cn, Task newTask) throws DatabaseException{
		PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();

        	String sql = "INSERT INTO task_generic (task_generic_task_type_id_fk, task_generic_stage_id_fk, task_generic_user_id_fk, "
        			+ "parent_task_id, title, instructions, resource_link, completed, date_completed, task_order, is_extra_task, is_template) "
    				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, newTask.getTaskTypeID());
            ps.setInt(2, newTask.getStageID());
            ps.setInt(3, newTask.getUserID());
            ps.setInt(4, newTask.getParentTaskID());
            ps.setString(5, newTask.getTitle().trim());
            ps.setString(6, newTask.getInstructions().trim());
            ps.setString(7, newTask.getResourceLink().trim());
            ps.setBoolean(8, newTask.isCompleted());
            Date dateCompleted = newTask.getDateCompleted()==null ? null : Date.valueOf(newTask.getDateCompleted());
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
			//DbUtils.closeQuietly(cn);
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
            	taskTypeMap.put(rs.getInt("task_type_id"), rs.getString("type"));
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
	public TreatmentIssue treatmentIssueCreate(TreatmentIssue treatmentIssue) throws ValidationException, DatabaseException{
		Connection cn = null;
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
        	cn = getConnection();
        	
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
	public boolean treatmentIssueValidateNewName(String issueName, int userID) throws ValidationException, DatabaseException{
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
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(issueCount);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(cn);
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
            		+ "FROM (user) INNER JOIN treatment_issue ON user.user_id = treatment_issue.treatment_issue_user_id_fk "
            		+ "WHERE (((user.user_id)=?))";
        	
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
