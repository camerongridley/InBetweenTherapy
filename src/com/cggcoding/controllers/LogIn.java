package com.cggcoding.controllers;

import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.database.MySQLActionHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Servlet implementation class MasterController
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userRole = "";
        DataSource datasource = (DataSource)request.getServletContext().getAttribute("datasource");
        MySQLActionHandler mySQLActionHandler = new MySQLActionHandler(datasource);

    	
    	//DELETE
    	mySQLActionHandler.testPool();
    	//END DELETE
    	
        boolean userExists = mySQLActionHandler.validateUser(email, password);

        //use the above to get authenticate the user and get create a User object
        if(userExists){
        	User user = mySQLActionHandler.getUserInfo(email, password);
        	request.getSession().setAttribute("user", user);
        	
        	if(user.hasRole("admin")){
                request.getRequestDispatcher("admintools/adminMainMenu.jsp").forward(request, response);
        	} else if(user.hasRole("therapist")){
                request.getRequestDispatcher("therapisttools/therapistMainMenu.jsp").forward(request, response);
        	}if(user.hasRole("client")){
                request.getRequestDispatcher("clienttools/clientmainmenu.jsp").forward(request,response);
        	}
        	
/*        	
            ResultSet userInfoRS = null;
            try {
            	
            	

                userInfoRS = mySQLActionHandler.getUserInfo(email, password);
                while (userInfoRS.next()){
                    userRole = userInfoRS.getString("role");

                    if(userRole.equals("client")){
                        System.out.println("logging in as client");
                        int userID = userInfoRS.getInt("user_id");
                        int activeTreatmentPlanID = userInfoRS.getInt("active_treatment_plan_id");

                        UserClient client = new UserClient(userID, email);
                        client.addRole(userInfoRS.getString("role"));
                        client.setActiveTreatmentPlanId(activeTreatmentPlanID);
                        request.getSession().setAttribute("user", client);
                        request.getRequestDispatcher("clienttools/clientmainmenu.jsp").forward(request,response);

                    } else if (userRole.equals("therapist")){
                        System.out.println("logging in as therapist");
                        int userID = userInfoRS.getInt("user_id");

                        UserTherapist therapist = new UserTherapist(userID, email);
                        therapist.addRole(userInfoRS.getString("role"));

                        request.getSession().setAttribute("user", therapist);
                        request.getRequestDispatcher("therapisttools/therapistMainMenu.jsp").forward(request, response);

                    } else if (userRole.equals("admin")){
                    	System.out.println("logging in as admin");
                    	int userID = userInfoRS.getInt("user_id");

                        UserAdmin admin = new UserAdmin(userID, email);
                        admin.addRole(userInfoRS.getString("role"));

                        request.getSession().setAttribute("user", admin);
                        request.getRequestDispatcher("admintools/adminMainMenu.jsp").forward(request, response);

                    }
                }



            } catch (SQLException e) {

                e.printStackTrace();
            } finally {
                try {
                    userInfoRS.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mySQLActionHandler.closeConnection();
            }
*/


        } else {
            System.out.println("Error Logging in.  Try a different email or password.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
