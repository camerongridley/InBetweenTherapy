package com.cggcoding.utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.Task;
import com.cggcoding.utils.messaging.ErrorMessages;

public class DAOStarter {
	private static final long serialVersionUID = 1L;
	DatabaseConnection mysqlConn;
	
	public DAOStarter() {
		// TODO Auto-generated constructor stub
	}


    public Connection getConnection() throws DatabaseException{
		Connection cn = null;
		
		cn = mysqlConn.getConnection();
		if(cn == null){
			throw new DatabaseException(ErrorMessages.CONNECTION_IS_NULL);
		}
		
		return cn;
    }
    
    public Object load(Connection cn, int id) throws SQLException{
    	PreparedStatement ps = null;
        ResultSet rs = null;
        Object o = null;
        
        
        try {
    		String sql = "SELECT * FROM stage WHERE stage.stage_id =?";
        	
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
   
            while (rs.next()){
            	o = null; //here build object with constructor or static factory method 
            }

        } finally {
        	DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
        }
        
        //throwValidationExceptionIfNull(stage);
        
        return o;
    	
    }
    
    public Object createAndGetGeneratedID(Connection cn, Stage newStage) throws SQLException{
    	PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        
        try {
    		String sql = "INSERT INTO stage (stage_user_id_fk, stage_treatment_plan_id_fk, stage_title, stage_description, stage_completed, client_stage_order, percent_complete, stage_in_progress, stage_is_template, template_id) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            /* set the prepared statement arguments*/
            ps.setInt(1, newStage.getUserID());
            ps.setInt(2, newStage.getTreatmentPlanID());
            ps.setString(3, newStage.getTitle().trim());
            ps.setString(4, newStage.getDescription());
            ps.setBoolean(5,  newStage.isCompleted());
            ps.setInt(6, newStage.getClientStageOrder());
            ps.setDouble(7, newStage.getPercentComplete());
            ps.setBoolean(8, newStage.isInProgress());
            ps.setBoolean(9, newStage.isTemplate());
            ps.setInt(10, newStage.getTemplateID());

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
    
    public boolean update(Connection cn, Stage stage) throws SQLException{
    	PreparedStatement ps = null;
        int success = 0;
        
        if(stage.isTemplate()){  	
        	//as a precaution - make sure the template's order is 0 since all order values for how stage templates are a part of a treatment plan template is kept in their mapping table
        	stage.setClientStageOrder(0);
        }
        
        try {
        		
    		String sql = "UPDATE stage SET stage_treatment_plan_id_fk=?, stage_user_id_fk=?, stage_title=?, stage_description=?, stage_completed=?, client_stage_order=?, percent_complete=?, stage_in_progress=?, stage_is_template=?, template_id=? WHERE stage_id=?";
        	
            ps = cn.prepareStatement(sql);

            ps.setInt(1, stage.getTreatmentPlanID());
            ps.setInt(2, stage.getUserID());
            ps.setString(3, stage.getTitle().trim());
            ps.setString(4, stage.getDescription());
            ps.setBoolean(5, stage.isCompleted());
            ps.setInt(6, stage.getClientStageOrder());
            ps.setDouble(7, stage.getPercentComplete());
            ps.setBoolean(8, stage.isInProgress());
            ps.setBoolean(9, stage.isTemplate());
            ps.setInt(10, stage.getTemplateID());
            ps.setInt(11, stage.getStageID());

            success = ps.executeUpdate();
        	
        } finally {
			DbUtils.closeQuietly(ps);
        }
        
        return success == 1;
    	
    }
    
    public void delete(Connection cn, int objectID) throws SQLException{
    	PreparedStatement ps = null;
        
    	try{
	        ps = cn.prepareStatement("DELETE FROM stage WHERE stage_id=?");
	        ps.setInt(1, objectID);
	
	        ps.executeUpdate();
    	}finally{
    		DbUtils.closeQuietly(ps);
    	}
    	

    }
    
}
