package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class UserAdmin extends User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	private List<TreatmentIssue> customTreatmentIssues;

	public UserAdmin(int userID, String userName, String firstName, String lastName, String email) {
		super(userID, userName, firstName, lastName, email);
		this.setRoleID(Constants.ADMIN_ROLE_ID);
		this.addRole(Constants.USER_ADMIN);
		this.setRole(Constants.USER_ADMIN);
		this.customTreatmentIssues = new ArrayList<>();
		setMainMenuURL(Constants.URL_ADMIN_MAIN_MENU);
	}

	@Override
	public boolean isAuthorizedForTreatmentPlan(int treatmentPlanID) throws DatabaseException {
		/*Connection cn = null;
		boolean isAuthorized = false;
		try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);

        	isAuthorized = super.userOwnsTreatmentPlan(cn, treatmentPlanID);

        	cn.commit();
        	
        } catch (SQLException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
        }
		
		return isAuthorized;*/
		
		return true;
	}

	@Override
	public boolean isAuthorizedForStage(int stageID) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAuthorizedForTask(int taskID) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void processInvitationAcceptance(Connection cn, String invitationCode) throws SQLException, ValidationException {
		//nothing to do here - only applies to therapists and clients
		
	}

	@Override
	protected void performLoginSpecifics() throws DatabaseException {
		// so far nothing to do here
		
	}

}
