package com.cggcoding.models;

import java.sql.Connection;
import java.sql.SQLException;

import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class MapTreatmentPlanStageTemplate {
	private int treatmentPlanID;
	private int stageID;
	private int templateStageOrder;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	public MapTreatmentPlanStageTemplate() {
		
	}
	
	public MapTreatmentPlanStageTemplate(int treatmentPlanID, int stageID, int templateStageOrder) {
		this.treatmentPlanID = treatmentPlanID;
		this.stageID = stageID;
		this.templateStageOrder = templateStageOrder;
	}



	public int getTreatmentPlanID() {
		return treatmentPlanID;
	}

	public void setTreatmentPlanID(int treatmentPlanID) {
		this.treatmentPlanID = treatmentPlanID;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	public int getTemplateStageOrder() {
		return templateStageOrder;
	}

	public void setTemplateStageOrder(int stageOrder) {
		this.templateStageOrder = stageOrder;
	}
	
	/**Since templateTaskOrder is based off List indexes, it starts with 0.  So for displaying the order to users on the front end, add 1 so
	 *the order values start with 1.
	 * @return
	 */
	public int getTemplateStageOrderForUserDisplay(){
		return templateStageOrder + 1;
	}

	protected boolean validate(Connection cn) throws ValidationException, SQLException{
		return dao.mapTreatmentPlanStageTemplateValidate(cn, this.stageID, this.treatmentPlanID);
	}
	
	protected void update(Connection cn) throws SQLException{
		dao.mapTreatmentPlanStageTemplateUpdate(cn, this);
	}
	
	protected void create(Connection cn) throws SQLException{
		dao.mapTreatmentPlanStageTemplateCreate(cn, this);
	}
	
	protected static void delete(Connection cn, int treatmentPlanID, int stageID) throws SQLException{
		dao.mapTreatmentPlanStageTemplateDelete(cn, treatmentPlanID, stageID);
	}

}
