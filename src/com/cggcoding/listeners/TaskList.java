package com.cggcoding.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class TaskList
 *
 */
@WebListener
public class TaskList implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public TaskList() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent sessionEvent)  { 
         // TODO Auto-generated method stub
    	List<String> txStages = new ArrayList<String>();
    	
    	txStages.add("Education");
    	txStages.add(1, "Relaxation training");
    	txStages.add(2, "Change your thinking");
    	txStages.add(3, "Change your behavior");
    	txStages.add(4, "Get comfortable talking about sex");
    	
    	sessionEvent.getSession().setAttribute("txStages", txStages);
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
