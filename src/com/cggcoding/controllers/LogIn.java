package com.cggcoding.controllers;

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
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        System.out.println("Inside Login Servlet");
        if(userType.equals("client")){
            System.out.println("logging in as client");
            //go to database with userName and password and get basic user info
            UserClient client = new UserClient(1, userName);
            request.getSession().setAttribute("userClient", client);
            request.getRequestDispatcher("clienttools/clientmainmenu.jsp").forward(request,response);

        } else if (userType.equals("therapist")){
            System.out.println("logging in as therapist");
            //go to database with userName and password and get basic user info
            UserTherapist therapist = new UserTherapist(1, userName);
            request.getSession().setAttribute("userTherapist", therapist);
            request.getRequestDispatcher("therapisttools/therapistMainMenu.jsp").forward(request, response);

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("test");
    }
}
