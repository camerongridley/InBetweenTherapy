package com.cggcoding.controllers;

import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.database.MySQLActionHandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        MySQLActionHandler mySQLActionHandler = new MySQLActionHandler();

        mySQLActionHandler.openConnection();
        boolean userExists = mySQLActionHandler.validateUser(email, password);

        //use the above to get authenticate the user and get create a User object
        if(userExists){

            ResultSet userInfoRS = mySQLActionHandler.getUserInfo(email, password);
            try {
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

                    }
                }
            } catch (SQLException e) {
                mySQLActionHandler.closeConnection();
                e.printStackTrace();
            }

            mySQLActionHandler.closeConnection();

        } else {
            System.out.println("Error Logging in.  Try a different email or password.");
            request.getRequestDispatcher("error.jsp").forward(request,response);
        }


    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
