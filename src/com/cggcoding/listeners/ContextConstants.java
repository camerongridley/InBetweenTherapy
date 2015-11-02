package com.cggcoding.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.cggcoding.utils.Constants;

/**
 * Application Lifecycle Listener implementation class ContextConstants
 *
 */
@WebListener
public final class ContextConstants implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ContextConstants() {
        
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         arg0.getServletContext().setAttribute("constants", new Constants());
    }
	
}
