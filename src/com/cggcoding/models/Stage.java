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
	private int stageOrder; //the order of the stage within its treatment plan - if present decides the index it will be in the TreatmentPlan's List of Stages
	private List<Task> tasks;
	private List<Task> extraTasks; //for when user chooses to do more tasks than asked of - won't count toward progress meter but can be saved for review or other analysis (e.g. themes)
	private LinkedHashMap<Integer, MapStageTaskTemplate> mapStageTaskTemplates;
	private boolean completed;
	private double percentComplete;
	private List<StageGoal> goals;
	private boolean inProgress;
	private boolean template;
	private int templateID;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();

	private Stage (int treatmentPlanID, int userID, String title, String description, int stageOrder, boolean template){
		this.stageID = 0;
		this.treatmentPlanID = treatmentPlanID;
		this.userID = userID;
		this.title = title;
		this.description = description;
		this.stageOrder = stageOrder;
		this.tasks = new ArrayList<>();;
		this.extraTasks = new ArrayList<>();
		this.mapStageTaskTemplates = new LinkedHashMap<>();
		this.completed = false;
		this.percentComplete = 0;
		this.goals = new ArrayList<>();
		this.inProgress = false;
		this.template = template;
		this.templateID = 0;
	}
	
	//Full constructor - asks for every argument stage has
	private Stage(int stageID, int treatmentPlanID, int userID, String title, String description, int stageOrder,
			List<Task> tasks, List<Task> extraTasks, boolean completed, double percentComplete, List<StageGoal> goals,
			boolean inProgress, boolean template, int templateID) {
		this.stageID = stageID;
		this.treatmentPlanID = treatmentPlanID;
		this.userID = userID;
		this.title = title;
		this.description = description;
		this.stageOrder = stageOrder;
		this.tasks = tasks;
		this.extraTasks = extraTasks;
		this.mapStageTaskTemplates = new LinkedHashMap<>();
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
	 * @param stageOrder Order of the stage within the parent TreatmentPlan
	 * @param tasks List of tasks within this stage
	 * @param extraTasks List of extra tasks within this stage
	 * @param completed True if all tasks in the stage have been completed
	 * @param percentComplete Percentage stage is complete based on number of tasks completed
	 * @param goals List of the goals designated for this stage
	 * @param template True if this is a Stage template and therefore has no concrete parent TreatmentPlan
	 * @return Stage object
	 */
	public static Stage getInstance(int stageID, int treatmentPlanID, int userID, String title, String description, int stageOrder, List<Task> tasks, 
			List<Task> extraTasks, boolean completed, double percentComplete, List<StageGoal> goals, boolean inProgress, boolean template, int templateID){
		return new Stage(stageID, treatmentPlanID, userID, title, description, stageOrder, tasks, extraTasks, completed, percentComplete, goals, inProgress, template, templateID);
	}
	
	/**For use when creating a new Stage. As such, these are the only parameters that could be available for saving to the database.  
	 * Other parameters such as tasks, extraTasks, and goals are Lists that are added to the stage once it has already been created and 
	 * has had a stageID generated. At that point one should use the regular getInstance() method.
	 * @param treatmentPlanID
	 * @param userID
	 * @param title
	 * @param description
	 * @param stageOrder
	 * @param template
	 * @return
	 */
	public static Stage getInstanceWithoutID(int treatmentPlanID, int userID, String title, String description, int stageOrder, boolean template) {
		Stage stage = new Stage(treatmentPlanID, userID, title, description, stageOrder, template);
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

	public LinkedHashMap<Integer, MapStageTaskTemplate> getMapStageTaskTemplates() {
		return mapStageTaskTemplates;
	}

	public void setMapStageTaskTemplates(LinkedHashMap<Integer, MapStageTaskTemplate> mapStageTaskTemplates) {
		this.mapStageTaskTemplates = mapStageTaskTemplates;
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

	public int getStageOrder() {
		return stageOrder;
	}

	//sets the order of the stage in the treatment plan if relevant
	public void setStageOrder(int stageOrder) {
		this.stageOrder = stageOrder;
	}
	
	//FIXME currently not being used since linking Treatment Plan templates directly to Stage templates and Stage templates all have an order value of 0. The order for these tasks is stored in the stage-plan mapping table.
	/**Since stageOrder is based off List indexes, it starts with 0.  So for displaying the order to users on the front end, add 1 so
	 *the order values start with 1.
	 * @return
	 */
	public int getStageOrderForUserDisplay(){
		return this.stageOrder + 1;
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
		double roundedPercent = 0;
		
		if(!tasks.isEmpty()){
			roundedPercent = Math.floor(percentComplete * 100) / 100;
		}
		
		return (roundedPercent);
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
	
	/**Once a task is completed this is called to update the progress meter and associated metrics
	 * @throws DatabaseException 
	 * @throws ValidationException 
	 */
	public void updateProgress() throws ValidationException, DatabaseException{
		
		percentComplete = ((double)getNumberOfTasksCompleted()/(double)getTotalNumberOfTasks());
		
		if(getPercentComplete()==1){
			this.markComplete();
		} else {
			this.markIncomplete();
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
	
	public void addGoal(StageGoal goal){
		this.goals.add(goal);
	}
	
	public StageGoal getGoalByID(int stageGoalID){
		for(StageGoal goal : goals){
			if(goal.getStageGoalID() == stageGoalID){
				return goal;
			}
		}
		
		return null;
	}
	
	public MapStageTaskTemplate getMappedTaskTemplate(int taskID){
		return mapStageTaskTemplates.get(taskID);
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
        
        dao.throwValidationExceptionIfTemplateHolderID(stageID);
        
    	stage = dao.stageLoadBasic(cn, stageID);
    	
    	stage.setGoals(dao.stageLoadGoals(cn, stage.getStageID()));
    	
    	if(stage.isTemplate()){
    		//get map of templates and set local map variable
    		LinkedHashMap<Integer, MapStageTaskTemplate> taskMap = dao.mapStageTaskTemplateLoad(cn, stageID);
    		stage.setMapStageTaskTemplates(taskMap);
    		
    		//loop through map and load Task templates to local List
    		for(Integer taskID : taskMap.keySet()){
    			stage.addTask(Task.load(cn, taskID));
    		}
    	}else{
    		stage.setTasks(dao.stageLoadClientTasks(cn, stage.getStageID()));
    	}
		

        
        dao.throwValidationExceptionIfNull(stage);
        
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
        
        dao.throwValidationExceptionIfTemplateHolderID(stageID);
        
    	stage = dao.stageLoadBasic(cn, stageID);

        dao.throwValidationExceptionIfNull(stage);
        
        return stage;
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
			dao.stageCreateBasic(cn, this);
			
			for(StageGoal goal : getGoals()){
				if(goal.isValidGoal()){
					//set the newly generated stageID in the goal
					goal.setStageID(this.stageID);
					goal.create(cn);
				}
			}
			
			for(Task task : getTasks()){
				//set the newly generated stageID in the task
				task.setStageID(getStageID());
				task.create(cn);
			}
		}
	}

	public static Stage createTemplate(int userID, String title, String description) throws ValidationException, DatabaseException{
		Stage stageTemplate = new Stage(Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID, userID, title, description, Constants.TEMPLATE_ORDER_NUMBER, true);
		
		stageTemplate.create();
		
		return stageTemplate;
	}
	
	public static Stage createTemplate(Stage templateStage) throws ValidationException, DatabaseException{
		return createTemplate(templateStage.getUserID(), templateStage.getTitle(), templateStage.getDescription());
	}
	
	@Override
	public void update()  throws ValidationException, DatabaseException {
		Connection cn = null;
              
        try {
        	cn = dao.getConnection();
        	
        	updateBasic(cn);
        	
        	for(StageGoal goal : goals){
        		goal.update(cn);
        	}
        	
        	for(MapStageTaskTemplate stageTaskTemplate : this.mapStageTaskTemplates.values()){
        		stageTaskTemplate.update(cn);
        	}
        	
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {

			DbUtils.closeQuietly(cn);
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
	
	protected static void delete(Connection cn, int stageID) throws ValidationException, SQLException {
        	dao.stageDelete(cn, stageID);

	}
	
	/**Adds a task template to a stage template.  Inserts into taskTemplate-stageTemplate mapping table. Both the Task and Stage must be templates to be valid.
	 * @param taskTemplateID
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public void addTaskTemplate(int taskTemplateID, int templateRepetitions) throws DatabaseException, ValidationException{
		Connection cn = null;
	
		if(this.isTemplate()){
			try {
				
	        	cn = dao.getConnection();
	        	if(dao.mapStageTaskTemplateValidate(cn, taskTemplateID, this.getStageID())){
	        		MapStageTaskTemplate map = new MapStageTaskTemplate(this.stageID, taskTemplateID, this.getTaskOrderDefaultValue(), templateRepetitions);
	        		map.create(cn);
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
	
	public Task copyTaskIntoStage(int taskIDBeingCopied) throws DatabaseException, ValidationException{
		Task task = Task.load(taskIDBeingCopied);
		task.setTemplate(false);
		task.setUserID(this.userID);
		task.setStageID(this.stageID);
		task.setClientTaskOrder(this.getTaskOrderDefaultValue());
		
		task.create();
		this.addTask(task);
		
		return task;
	}
	
	public Task createNewTask(Task taskBeingCopied) throws DatabaseException, ValidationException{
		taskBeingCopied.setUserID(this.userID);
		taskBeingCopied.setStageID(this.stageID);
		taskBeingCopied.setClientTaskOrder(this.getTaskOrderDefaultValue());
		
		return taskBeingCopied.create();
	}
	
	//XXX rename to deleteTaskTemplate?
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
				//OPTIMIZE Could replace this with method in dao that takes List<Task> and loops through updating
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
	
	protected List<Task> updateTaskTemplateList(Connection cn, List<Task> taskTemplates) throws SQLException{
		return dao.stageUpdateTemplateTasks(cn, this.stageID, taskTemplates);
	}
	
	private void reorderTasks(){
		for(int i=0; i < this.tasks.size(); i++){
			tasks.get(i).setClientTaskOrder(i);
		}
	}
	
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
		Stage copiedStage = getInstanceWithoutID(this.treatmentPlanID, this.userID, this.title, this.description, this.stageOrder, falseForTemplate);

		for(StageGoal goal : this.goals){
			copiedStage.addGoal(goal.copy());
		}
		
		for(Task task : this.tasks){
			task = task.copy();
			copiedStage.addTask(task.copy());
		}
		
		return copiedStage;
	}
	
	public static List<Stage> getDefaultStages() throws DatabaseException, ValidationException{
		return dao.stagesGetDefaults();
	}
}
