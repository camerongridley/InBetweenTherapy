package com.cggcoding.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Application Lifecycle Listener implementation class DatasourceCreator
 *
 */
@WebListener
public class DatasourceCreator implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public DatasourceCreator() {
        
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
         
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	/*
         PoolProperties props = new PoolProperties();
         props.setUrl("jdbc:mysql://localhost/cggcodin_doitright");
         props.setDriverClassName( "com.mysql.jdbc.Driver" );
         props.setUsername( "admin" );
         props.setPassword( "admin" );
         
         DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource( props );
         
        event.getServletContext().setAttribute("datasource", datasource);
        */
    }
	
}
