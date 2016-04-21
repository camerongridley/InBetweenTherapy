package com.cggcoding.models;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.dbutils.DbUtils;

//UNSURE Consider creating subclasses of Stage: ClientStage and TemplateStage since some vairables and methods are only valid for use with each type - now they are all housed in one class, so would be cleaner if they were split up
public class Stage implements Serializable, Completable, DatabaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int stageID;
	private int treatmentPlanID;
	private int userID;
	private String title;
	private String description;
	private int clientStageOrder; //the order of the stage within client-owned treatment plans - order for admin and therapist owned treatment plans will be templates and so will be stored in the mapping class
	private List<Task> tasks;
	private List<Task> extraTasks; //for when user chooses to do more tasks than asked of - won't count toward progress meter but can be saved for review or other analysis (e.g. themes)
	private List<MapStageTaskTemplate> stageTaskTemplateMapList;
	private boolean completed;
	private double percentComplete;
	private List<StageGoal> goals;
	private boolean inProgress;
	private boolean template;
	private int templateID;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();

	private Stage (int treatmentPlanID, int userID, String title, String description, int clientStageOrder, boolean template){
		this.stageID = 0;
		this.treatmentPlanID = treatmentPlanID;
		this.userID = userID;
		this.title = title;
		this.description = description;
		this.clientStageOrder = clientStageOrder;
		this.tasks = new ArrayList<>();;
		this.extraTasks = new ArrayList<>();
		this.stageTaskTemplateMapList = new ArrayList<>();
		this.completed = false;
		this.percentComplete = 0;
		this.goals = new ArrayList<>();
		this.inProgress = false;
		this.template = template;
		this.templateID = 0;
	}
	
	//Full constructor - asks for every argument stage has
	private Stage(int stageID, int treatmentPlanID, int userID, String title, String description, int clientStageOrder,
			List<Task> tasks, List<Task> extraTasks, boolean completed, double percentComplete, List<StageGoal> goals,
			boolean inProgress, boolean template, int templateID) {
		this.stageID = stageID;
		this.treatmentPlanID = treatmentPlanID;
		this.userID = userID;
		this.title = title;
		this.description = description;
		this.clientStageOrder = clientStageOrder;
		this.tasks = tasks;
		this.extraTasks = extraTasks;
		this.stageTaskTemplateMapList = new ArrayList<>();
		this.completed = completed;
		this.percentComplete = percentComplete;
		this.goals = goals;
		this.inProgress = inProgress;
		this.template = template;
		this.templateID = templateID;
	}

	/**Gets a complete instance of Stage and asks for every argument in class
	 * @param stageID id of this stage
	 * @param treatmentPlanID id of the parent TreatmentPlan
	 * @param userID is of the user who owns this
	 * @param title Title of the stage
	 * @param description Description of the stage
	 * @param clientStageOrder Order of the stage within the parent TreatmentPlan
	 * @param tasks List of tasks within this stage
	 * @param extraTasks List of extra tasks within this stage
	 * @param completed True if all tasks in the stage have been completed
	 * @param percentComplete Percentage stage is complete based on number of tasks completed
	 * @param goals List of the goals designated for this stage
	 * @param template True if this is a Stage template and therefore has no concrete parent TreatmentPlan
	 * @return Stage object
	 */
	public static Stage getInstance(int stageID, int treatmentPlanID, int userID, String title, String description, int clientStageOrder, List<Task> tasks, 
			List<Task> extraTasks, boolean completed, double percentComplete, List<StageGoal> goals, boolean inProgress, boolean template, int templateID){
		return new Stage(stageID, treatmentPlanID, userID, title, description, clientStageOrder, tasks, extraTasks, completed, percentComplete, goals, inProgress, template, templateID);
	}
	
	/**For use when creating a new Stage. As such, these are the only parameters that could be available for saving to the database.  
	 * Other parameters such as tasks, extraTasks, and goals are Lists that are added to the stage once it has already been created and 
	 * has had a stageID generated. At that point one should use the regular getInstance() method.
	 * @param treatmentPlanID
	 * @param userID
	 * @param title
	 * @param description
	 * @param clientStageOrder
	 * @param template
	 * @return
	 */
	public static Stage getInstanceWithoutID(int treatmentPlanID, int userID, String title, String description, int clientStageOrder, boolean template) {
		Stage stage = new Stage(treatmentPlanID, userID, title, description, clientStageOrder, template);
		return stage;
	}

	public int getStageID() {
		return stageID;
	}
	
	public void setStageID(int stageID) {
		this.stageID = stageID;
	}
	
	public int getTreatmentPlanID() {
		return treatmentPlanID;
	}

	public void setTreatmentPlanID(int treatmentPlanID) {
		this.treatmentPlanID = treatmentPlanID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> taskList) {
		this.tasks = taskList;
	}

	public List<Task> getExtraTasks() {
		return extraTasks;
	}

	public void setExtraTasks(List<Task> extraTasks) {
		this.extraTasks = extraTasks;
	}

	public List<MapStageTaskTemplate> getMapStageTaskTemplates() {
		return stageTaskTemplateMapList;
	}

	public void setMapStageTaskTemplates(List<MapStageTaskTemplate> mapStageTaskTemplates) {
		this.stageTaskTemplateMapList = mapStageTaskTemplates;
	}

	public Task getTaskByID(int taskID){
		Task returnMe = null;
		for(Task task : tasks){
			if(task.getTaskID() == taskID){
				returnMe = task;
			}
		}

		return returnMe;
	}

	public String getTaskTypeNameByID(int taskID){
		Task returnMe = null;
		for(Task task : tasks){
			if(task.getTaskID() == taskID){
				returnMe = task;
			}
		}

		return returnMe.getTaskTypeName();
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}

	public int getClientStageOrder() {
		return clientStageOrder;
	}

	//sets the order of the stage in the treatment plan if relevant
	public void setClientStageOrder(int clientStageOrder) {
		this.clientStageOrder = clientStageOrder;
	}
	
	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}
	
	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

	//Tasks will be displayed in the order in which they are in the List
	public void addTask(Task task){
		task.setStageID(this.stageID);
		tasks.add(task);
	}
	
	public void addExtraTask(Task extraTask){
		extraTasks.add(extraTask);
	}

	public List<StageGoal> getGoals() {
		return goals;
	}

	public void setGoals(List<StageGoal> goals) {
		this.goals = goals;
	}

	@Override
	public boolean isCompleted(){
		return completed;
	}

	@Override
	public void markComplete(){
		completed = true;
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	//returns a double digit number representing percentage of stage completion
	@Override
	public double getPercentComplete(){
/*		double roundedPercent = 0;
		
		if(!tasks.isEmpty()){
			roundedPercent = Math.floor(percentComplete * 100) / 100;
		}
		
		return (roundedPercent);*/
		
		if(!tasks.isEmpty()){
			return (int) (percentComplete*100);
		} else {
			return 0;
		}
	}

	public int getNumberOfTasksCompleted() {
		int numberOfTasksCompleted = 0;
		for(Task task : tasks){
			if(task.isCompleted()){
				numberOfTasksCompleted++;
			}
		}

		return numberOfTasksCompleted;
	}

	public int getTotalNumberOfTasks() {
		return tasks.size();
	}
	
	
	private int getTaskOrderDefaultValue(){
		return tasks.size();
	}
	
	public boolean isDisabledForModification(){
		return (this.completed == false && this.inProgress==false);
	}

	//when a task's completion state is changed it checks if all tasks are complete and if will lead to stage being complete and any other actions desired at this time
	public Stage updateTaskList(Map<Integer, Task> updatedTasksMap) throws ValidationException, DatabaseException{
		//iterate through task map to update with info from updatedTasks list
		for(Task persistentTask : this.tasks){
			Task taskWithNewInfo = updatedTasksMap.get(persistentTask.getTaskID());
			persistentTask.transferGeneralData(taskWithNewInfo);
		}

		updateProgress();
		return this;
	}
	
	/**Once a task is completed this is called to update the progress meter and associated metrics.  Also determines if the Stage will be marked completed.
	 * @throws DatabaseException 
	 * @throws ValidationException 
	 */
	public void updateProgress() throws ValidationException, DatabaseException{
		
		percentComplete = ((double)getNumberOfTasksCompleted()/(double)getTotalNumberOfTasks());
		
		if(getPercentComplete()==100){
			this.markComplete();
			//this.setInProgress(false);
		} else {
			this.markIncomplete();
			//this.setInProgress(true);
		}
		
		update();
	}
	
	
	public List<Task> getIncompleteTasks(){
		List<Task> incompleteTasks = new ArrayList<>();

		for(Task task : tasks){
			if(!task.isCompleted()){
				incompleteTasks.add(task);
			}
		}
		return incompleteTasks;
	}

	public List<Task> getCompletedTasks(){
		List<Task> completeTasks = new ArrayList<>();

		for(Task task : tasks){
			if(task.isCompleted()){
				completeTasks.add(task);
			}
		}
		return completeTasks;
	}
	
	public StageGoal getGoalByID(int stageGoalID){
		for(StageGoal goal : goals){
			if(goal.getStageGoalID() == stageGoalID){
				return goal;
			}
		}
		
		return null;
	}
	
	public MapStageTaskTemplate getMappedTaskTemplateByTaskID(int taskID){
		MapStageTaskTemplate found = new MapStageTaskTemplate();
		for(MapStageTaskTemplate stageTaskTemplate : stageTaskTemplateMapList){
			if(taskID == stageTaskTemplate.getTaskID()){
				found = stageTaskTemplate;
				break;
			}
		}
		return found;
	}

	/**Inserts the new goal into the database, then adds it the this Stage's goal list.
	 * @param goal
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	public void saveNewGoal(StageGoal goal) throws ValidationException, DatabaseException{
		goal.create();
		this.goals.add(goal);
	}
	
	/**Deletes goal from the database then removes it from this instance of Stage
	 * @param goalID
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	public void deleteGoal(int goalID) throws ValidationException, DatabaseException{
		StageGoal.delete(goalID);
		int indexToRemove = 0;
		for(int i=0; i<goals.size(); i++){
			if(goals.get(i).getStageGoalID() == goalID){
				indexToRemove = i;
				break;
			}
		}
		
		goals.remove(indexToRemove);
	}
	
	/**Loads the stage and all associated Tasks.  Checks if the Stage is a template.  If so, then it's Tasks are also templates and 
	 * the database loads the Tasks using the task_template_stage_template_mapping table to get the taskIDs to load.  If not a template then it 
	 * just loads tasks straight from the task_generic table
	 * @param stageID
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public static Stage load(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
		Stage stage = null;

		try{
			cn = dao.getConnection();

			stage = load(cn, stageID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }

		return stage;
		
	}
	
	/**Loads the stage and all associated Tasks.  Checks if the Stage is a template.  If so, then it's Tasks are also templates and 
	 * the database loads the Tasks using the task_template_stage_template_mapping table to get the taskIDs to load.  If not a template then it 
	 * just loads tasks straight from the task_generic table
	 * @param cn
	 * @param stageID
	 * @return
	 * @throws SQLException
	 * @throws ValidationException
	 */
	public static Stage load(Connection cn, int stageID) throws SQLException, ValidationException{
		Stage stage = null;
        
		if(stageID!=0){
			dao.throwValidationExceptionIfTemplateHolderID(stageID);
	        
	    	stage = dao.stageLoadBasic(cn, stageID);
	    	
	    	stage.loadGoals(cn);
	    	
	    	if(stage.isTemplate()){
	    		//get list of templates and set local variable
	    		List<MapStageTaskTemplate> taskMap = dao.mapStageTaskTemplateLoad(cn, stageID);
	    		stage.setMapStageTaskTemplates(taskMap);
	    		
	    		//loop through map and load Task templates to local List
	    		for(MapStageTaskTemplate stageTaskTempaltes : taskMap){
	    			stage.addTask(Task.load(cn, stageTaskTempaltes.getTaskID()));
	    		}
	    	}else{
	    		stage.setTasks(dao.stageLoadClientTasks(cn, stage.getStageID()));
	    	}
			

	        
	        dao.throwValidationExceptionIfNull(stage);
		}
        
        
        return stage;
	}

	public static Stage loadBasic(int stageID) throws DatabaseException, ValidationException{
		Connection cn = null;
		Stage stage = null;

		try{
			cn = dao.getConnection();

			stage = loadBasic(cn, stageID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }

		return stage;
	}
	
	public static Stage loadBasic(Connection cn, int stageID) throws SQLException, ValidationException{
		Stage stage = null;
        
		if(stageID!=0){
			dao.throwValidationExceptionIfTemplateHolderID(stageID);
	        
	    	stage = dao.stageLoadBasic(cn, stageID);
	    	
	    	if(stage.isTemplate()){
	    		stage.setMapStageTaskTemplates(dao.mapStageTaskTemplateLoad(cn, stageID));
	    	}
	    	
	        dao.throwValidationExceptionIfNull(stage);
		}
        
        
        return stage;
	}

	
	/**---Database Interaction---
	 * Creates a new Stage in the database that only contains the basic (i.e. Tasks, Goals, etc. or other lists) stage information contained in the Stage model.
	 * @param cn
	 * @throws ValidationException
	 * @throws SQLException
	 */
	protected void createBasic(Connection cn) throws ValidationException, SQLException{
		
		if(this.title.isEmpty()){
    		throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
    	}
		
		if(dao.stageValidateNewTitle(cn, this)){
			dao.stageCreateBasic(cn, this);
		}
	}
	
	@Override
	public Stage create() throws ValidationException, DatabaseException{
		Connection cn = null;
		
		dao.throwValidationExceptionIfTemplateHolderID(this.stageID);
		
        try {
        	cn= dao.getConnection();
        	
        	cn.setAutoCommit(false);
        	
			create(cn);
			
			cn.commit();
			
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
		}
        
        dao.throwValidationExceptionIfNull(this);
        
        return this;
		
		
		/*Stage savedStage = dao.stageValidateAndCreate(this);
		this.stageID = savedStage.getStageID();
		return savedStage;*/
	}
	
	protected void create(Connection cn) throws ValidationException, SQLException{
		
		if(this.title.isEmpty()){
    		throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
    	}
		
		if(dao.stageValidateNewTitle(cn, this)){
			createBasic(cn);
			
			createGoals(cn);
			
			for(Task task : getTasks()){
				//set the newly generated stageID in the task
				task.setStageID(getStageID());
				task.create(cn);
			}
		}
	}

	/**---Database Interaction---
	 * Creates a new Stage template with the supplied userID, title and description.
	 * Sets treatmentPlanID to Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID, templateStageOrder to Constants.TEMPLATE_ORDER_NUMBER and template to true.
	 * Then it inserts the new Stage into the database with Stage.create().
	 * @param userID
	 * @param title
	 * @param description
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	public static Stage createTemplate(int userID, String title, String description) throws ValidationException, DatabaseException{
		Stage stageTemplate = new Stage(Constants.TEMPLATES_HOLDER_PRIMARY_KEY_ID, userID, title, description, Constants.TEMPLATE_ORDER_NUMBER, true);
		
		stageTemplate.create();
		
		return stageTemplate;
	}
	
	@Override
	public void update()  throws ValidationException, DatabaseException {
		Connection cn = null;
              
        try {
        	cn = dao.getConnection();
        	
        	update(cn);
        	
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {

			DbUtils.closeQuietly(cn);
        }
		
	}
	
	public void update(Connection cn)  throws SQLException, ValidationException {

    	updateBasic(cn);
    	
    	for(StageGoal goal : goals){
    		goal.update(cn);
    	}
    	
    	if(this.template){
    		for(MapStageTaskTemplate stageTaskTemplate : this.stageTaskTemplateMapList){
        		stageTaskTemplate.update(cn);
        	}  
    	}
    	 	
		
	}
	
	public void updateBasic()  throws ValidationException, DatabaseException {
		Connection cn = null;
              
        try {
        	cn = dao.getConnection();
        	
        	updateBasic(cn);
        	
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {

			DbUtils.closeQuietly(cn);
        }
		
	}
	
	protected void updateBasic(Connection cn) throws ValidationException, SQLException{
		if(dao.stageValidateUpdatedTitle(cn, this)){
			dao.stageUpdateBasic(cn, this);
		}
	}
	
	@Override
	public void delete() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
            
        	delete(cn);
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(cn);
        }
		
	}
	
	protected void delete(Connection cn) throws SQLException, ValidationException, DatabaseException{      
        dao.throwValidationExceptionIfTemplateHolderID(this.stageID);
        
        dao.stageDelete(cn, this.stageID);
	
	}
	
	public static void delete(int stageID) throws ValidationException, DatabaseException {
		Connection cn = null;

		try {
        	cn = dao.getConnection();
        	dao.stageDelete(cn, stageID);
            
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }	
	}
	
	
	protected void loadGoals(Connection cn) throws SQLException, ValidationException{
		this.setGoals(dao.stageLoadGoals(cn, stageID));
	}
	
	public void createGoals() throws DatabaseException, ValidationException{
		Connection cn = null;
		
        try {
        	cn= dao.getConnection();
        	
        	cn.setAutoCommit(false);
        	
			createGoals(cn);
			
			cn.commit();
			
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
		}
	}
	
	protected void createGoals(Connection cn) throws SQLException, ValidationException{
		for(StageGoal goal : getGoals()){
			if(goal.isValidGoal()){
				//set the newly generated stageID in the goal
				goal.setStageID(this.stageID);
				goal.create(cn);
			}
		}
	}
	
	protected static void delete(Connection cn, int stageID) throws ValidationException, SQLException {
        	dao.stageDelete(cn, stageID);

	}
	
	/**Adds a task template to a stage template.  Inserts into taskTemplate-stageTemplate mapping table. Both the Task and Stage must be templates to be valid.

	 * @param taskTemplateID
	 * @param templateRepetitions - minimum value of 1 - is set to 1 if less
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public void addTaskTemplate(int taskTemplateID, int templateRepetitions) throws DatabaseException, ValidationException{
		Connection cn = null;
	
		if(this.isTemplate()){
			try {
				
	        	cn = dao.getConnection();
	        	if(dao.mapStageTaskTemplateValidate(cn, taskTemplateID, this.getStageID())){
	        		if(templateRepetitions < 1){
	        			templateRepetitions = 1;
	        		}
	        		MapStageTaskTemplate map = new MapStageTaskTemplate(this.stageID, taskTemplateID, this.getTaskOrderDefaultValue(), templateRepetitions);
	        		map.create(cn);

	        		stageTaskTemplateMapList.add(map);
	        		this.tasks.add(Task.load(cn, taskTemplateID));
	        	}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			} finally {
				DbUtils.closeQuietly(cn);
		    }		
		}else{
			throw new ValidationException(ErrorMessages.STAGE_IS_NOT_TEMPLATE);
		}
		
	}
	
	public List<Task> createTaskFromTemplate(int taskIDBeingCopied, MapStageTaskTemplate stageTaskInfo) throws DatabaseException, ValidationException{
		Connection cn = null;
		List<Task> createdTasks = new ArrayList<>();
		
		//TODO decide if need a template check
		//if(this.isTemplate()){
			try {
				
	        	cn = dao.getConnection();

	        	createdTasks = createTaskFromTemplate(cn, taskIDBeingCopied, stageTaskInfo);

			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			} finally {
				DbUtils.closeQuietly(cn);
		    }		
		//}else{
		//	throw new ValidationException(ErrorMessages.STAGE_IS_NOT_TEMPLATE);
		//}
		
		return createdTasks;
		
	}
	
	protected List<Task> createTaskFromTemplate(Connection cn, int taskIDBeingCopied, MapStageTaskTemplate stageTaskInfo) throws SQLException, ValidationException{
		List<Task> createdTasks = new ArrayList<>();
		int taskReps = stageTaskInfo.getTemplateTaskRepetitions();
		Task task = Task.load(cn, stageTaskInfo.getTaskID());
		task.setUserID(this.getUserID());
		task.setStageID(this.stageID);
		task.setTemplate(false);
		task.setTemplateID(task.getTaskID());
		
		for(int i = 0; i<taskReps; i++){
			Task taskRep = task.copy();
			taskRep.setClientRepetition(i+1);
			
			//if more than 1 repetition, change the Task title to reflect repetition number
			if(taskReps > 1){
				taskRep.setTitle(task.getTitle() + " (" + (i+1) + ")");
			}
			
			taskRep.setClientTaskOrder(tasks.size());
			
			taskRep.create(cn);
			this.addTask(taskRep);
			createdTasks.add(taskRep);
		}
		
		
		
		return createdTasks;
	}
	
	/**---Database Interaction---
	 * Creates a new Stage for an existing client-owned TreatmentPlan with the supplied title and description. 
	 * Sets treatmentPlanID with this plan's ID, userID with this plan's userID, clientStageOrder based on the number of existing Stages in the TreatmentPlan, and template is set to false.
	 * Then it inserts the new stage into the database with stage.create() and then adds the stage to the local Stages list.
	 * @param taskTitle - Title of new Task
	 * @param taskInstructions - Description of the Task
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	public Task createClientTask(Task clientTask) throws ValidationException, DatabaseException{
		if(!this.template){
			clientTask.setStageID(this.stageID);
			clientTask.setUserID(this.userID);
			clientTask.setClientTaskOrder(this.getTaskOrderDefaultValue());

			clientTask.create();
			
			this.addTask(clientTask);
		} else {
			throw new ValidationException(ErrorMessages.STAGE_CLIENT_ONLY_ALLOWED_IN_PLAN_TEMPLATE);
		}
		
		
		return clientTask;
	}
	
//TODO delete
/*	public Task copyTaskIntoClientStage(int taskIDBeingCopied) throws DatabaseException, ValidationException{
		Task task = Task.load(taskIDBeingCopied);
		task.setTemplate(false);
		task.setUserID(this.userID);
		task.setStageID(this.stageID);
		task.setTemplateID(taskIDBeingCopied);
		task.setClientTaskOrder(this.getTaskOrderDefaultValue());
		
		task.create();
		this.addTask(task);
		
		return task;
	}*/
	
	//TODO delete since not being used?
	/*public Task createNewTask(Task taskBeingCopied) throws DatabaseException, ValidationException{
		taskBeingCopied.setUserID(this.userID);
		taskBeingCopied.setStageID(this.stageID);
		taskBeingCopied.setClientTaskOrder(this.getTaskOrderDefaultValue());
		
		return taskBeingCopied.create();
	}*/
	
	/**---Database Interaction---
	 * Creates a new Stage for an existing client-owned TreatmentPlan with the supplied title and description. 
	 * Sets treatmentPlanID with this plan's ID, userID with this plan's userID, clientStageOrder based on the number of existing Stages in the TreatmentPlan, and template is set to false.
	 * Then it inserts the new stage into the database with stage.create() and then adds the stage to the local Stages list.
	 * @param stageTitle - Title of new Stage
	 * @param stageDescription - Description of the Stage
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	/*public Task createClientTask(String taskTitle, String taskInstructions) throws ValidationException, DatabaseException{
		Task clientTask = null;
		if(!this.template){
			clientTask = Stage.getInstanceWithoutID(this.treatmentPlanID, this.userID, stageTitle, stageDescription, this.getStageOrderDefaultValue(), false);

			clientStage.create();
			
			this.addStage(clientStage);
		} else {
			throw new ValidationException(ErrorMessages.TASK_CLIENT_ONLY_ALLOWED_IN_STAGE_TEMPLATE);
		}
		
		
		return clientStage;
	}*/
	
	public Stage deleteTask(int taskToDeleteID) throws ValidationException, DatabaseException{
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			cn.setAutoCommit(false);
			Task task = null;
			//find task and delete from database then remove from local List
			for(int i = 0; i < tasks.size(); i++){
				task = tasks.get(i);
				if(task.getTaskID() == taskToDeleteID){
					
					if(task.isTemplate()){ //UNSURE Does it matter if I check Task or Stage isTemplate() method here?  Since keeping the tasks as templates when they are part of stage templates, it really shouldn't but I fear I am overlooking something.
						MapStageTaskTemplate.delete(cn, taskToDeleteID, this.stageID);
						stageTaskTemplateMapList.remove(i);//this list is only populated when the stage is a template whereas the tasks list is always populated so remove from that below where all conditions hit it
					}else{
						task.delete(cn);
					}
					
					tasks.remove(i);
					break;
				}
			}
			
			reorderTasks();
			
			//update the remaining tasks with their new order value
			if(task.isTemplate()){
				updateTaskTemplateList(cn, tasks);
			}else{
				//OPTIMIZE Could replace this with method in dao that takes List<Task> and loops through updating using preparedStatement.addBatch()
				for(Task updateTask : tasks){
					updateTask.update(cn);
				}
			}

			cn.commit();
		} catch (SQLException | ValidationException e) {
            e.printStackTrace();
            try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
			}
            if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
        } finally {
        	try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
        }

		return this;
	}
	
	protected List<MapStageTaskTemplate> updateTaskTemplateList(Connection cn, List<Task> taskTemplates) throws SQLException{
		return dao.stageUpdateTemplateTasks(cn, this.stageID, stageTaskTemplateMapList);
	}
	
	private void reorderTasks(){
		//if this Stage is a template then it's task order info is going to be in the mapping table so reorder those.  Otherwise, the task order is a prop of the task
		if(this.template){
			for(int i=0; i < this.stageTaskTemplateMapList.size(); i++){
				stageTaskTemplateMapList.get(i).setTemplateTaskOrder(i);
			}
		} else {
			for(int i=0; i < this.tasks.size(); i++){
				tasks.get(i).setClientTaskOrder(i);
			}
		}
		
	}
	
	//TODO decide if I want to keep int mainTaskID as an argument.  It isn't being used right now.
	public void orderIncrementTask(int mainTaskID, int originalOrder) throws DatabaseException, ValidationException{
		Connection cn = null;
		
		if(originalOrder <= 0){
			throw new ValidationException(ErrorMessages.TASK_IS_FIRST);
		}
		
		try {
			cn = dao.getConnection();
    		
			//update the order in the actual tasks templates - happens for templates and client tasks
			Task mainTask = tasks.get(originalOrder);
			Task prevTask = tasks.get(originalOrder-1);
			this.tasks.set(originalOrder-1, mainTask);
			this.tasks.set(originalOrder, prevTask);
			
			//if this Stage is a template, then update the stage-mapping info
			if(this.template){
				MapStageTaskTemplate mainStageTaskMap = this.stageTaskTemplateMapList.get(originalOrder);
				MapStageTaskTemplate prevStageTaskMap = this.stageTaskTemplateMapList.get(originalOrder-1);
				
				//swap order values in mapping info
				mainStageTaskMap.setTemplateTaskOrder(originalOrder-1);
				prevStageTaskMap.setTemplateTaskOrder(originalOrder);
				
				//swap places in both the stageTaskMap and tasks List
				this.stageTaskTemplateMapList.set(originalOrder-1, mainStageTaskMap);
				this.stageTaskTemplateMapList.set(originalOrder, prevStageTaskMap);
				
				//update in database
				mainStageTaskMap.update(cn);
				prevStageTaskMap.update(cn);
			} else {
				//this is a client task so update the task's clientOrder prop
				mainTask.setClientTaskOrder(originalOrder-1);
				prevTask.setClientTaskOrder(originalOrder);
				
				//update it in the database
				mainTask.update(cn);
				prevTask.update(cn);
			}
			
			
        	
        	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }		
			
	}
	
	//TODO decide if I want to keep int mainTaskID as an argument.  It isn't being used right now.
	public void orderDecrementTask(int mainTaskID, int originalOrder) throws DatabaseException, ValidationException{
		Connection cn = null;
		
		if(originalOrder == this.tasks.size()-1){
			throw new ValidationException(ErrorMessages.TASK_IS_LAST);
		}
		
		try {
			cn = dao.getConnection();
			
			//get the task whose order is being decremented and the task after that it is going to swap with
			//update the local task list with the rearranged orders
			//- happens for templates and client tasks as the local list is populated either way, just uses different sources
			Task mainTask = tasks.get(originalOrder);
			Task nextTask = tasks.get(originalOrder+1);
			this.tasks.set(originalOrder+1, mainTask);
			this.tasks.set(originalOrder, nextTask);
			
			//if this Stage is a template, then update the stage-mapping info
			if(this.template){
				
				MapStageTaskTemplate mainStageTaskMap = this.stageTaskTemplateMapList.get(originalOrder);
				MapStageTaskTemplate nextStageTaskMap = this.stageTaskTemplateMapList.get(originalOrder+1);
				
				//swap order values in mapping info
				mainStageTaskMap.setTemplateTaskOrder(originalOrder+1);
				nextStageTaskMap.setTemplateTaskOrder(originalOrder);

				//swap places in the local List
				this.stageTaskTemplateMapList.set(originalOrder+1, mainStageTaskMap);
				this.stageTaskTemplateMapList.set(originalOrder, nextStageTaskMap);
				
				//update in database
				mainStageTaskMap.update(cn);
				nextStageTaskMap.update(cn);

			} else {
				
				
				
				
				//this is a client task so update the task's clientOrder prop
				mainTask.setClientTaskOrder(originalOrder+1);
				nextTask.setClientTaskOrder(originalOrder);
				
				//update it in the database
				mainTask.update(cn);
				nextTask.update(cn);
			}
			
			
        	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }	
		
	}
	
	
	//TODO delete because not being used?
	/**Creates a copy of the Stage and sets the copy's stageID to 0.  DOES NOT save anything to database.
	 * @param treatmentPlanIDToCopy - treatmentPlanID the Stage is being copied into
	 * @param userIDToCopy - userID of the User that owns the TreatmentPlan the Stage is being copied into
	 * @param isTemplate - Designates whether this Stage should be copied as a template or not. Set "true" if it is to be a template in the TreatmentPlan it is being copied into, and "false" if it is not a template.
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	public Stage copy(){
		boolean falseForTemplate = false;
		Stage copiedStage = getInstanceWithoutID(this.treatmentPlanID, this.userID, this.title, this.description, this.clientStageOrder, falseForTemplate);

		for(StageGoal goal : this.goals){
			copiedStage.getGoals().add(goal.copy());
		}
		
		for(Task task : this.tasks){
			task = task.copy();
			copiedStage.addTask(task.copy());
		}
		
		return copiedStage;
	}
	
	public static List<Stage> getCoreStages() throws DatabaseException, ValidationException{
		return dao.stagesGetCoreList();
	}

	
}
