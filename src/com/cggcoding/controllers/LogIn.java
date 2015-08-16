package com.cggcoding.controllers;

import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;

import java.io.IOException;
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
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter("userType");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //use the above to get authenticate the user and get create a User object - for now we create mock ones
        if(userType.equals("client")){
            System.out.println("logging in as client");

            User client = new UserClient(1, email);
            client.addRole("client");
            request.getSession().setAttribute("user", client);
            request.getRequestDispatcher("clienttools/clientmainmenu.jsp").forward(request,response);

        } else if (userType.equals("therapist")){
            System.out.println("logging in as therapist");

            User therapist = new UserTherapist(2, email);
            therapist.addRole("therapist");
            request.getSession().setAttribute("user", therapist);
            request.getRequestDispatcher("therapisttools/therapistMainMenu.jsp").forward(request, response);

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
