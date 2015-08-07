package com.cggcoding.temp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.tasktypes.CognitiveTask;
import com.cggcoding.models.tasktypes.PsychEdTask;
import com.cggcoding.models.tasktypes.RelaxationTask;

/**
 * Servlet implementation class LoadData
 */
@WebServlet("/LoadData")
public class LoadData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//load all data - in place of a db call since the db is not yet implemented
		HttpSession session = request.getSession();
		
		User user = new User(1, "cgridley@gmail.com", "password");
		
		TreatmentPlan txPlan = new TreatmentPlan("ED", "Erectile dysfunction", 1);
		
		user.addTreatmentPlan(txPlan);
		
		//create stages
		Stage psychEd = new Stage(0, "PsychoEducation", "Important concepts to learn about the problem you are experiencing.  Understanding some of these core concept can help you feel confident about the treatment strategies implemented here.");
		Stage relax = new Stage(1, "Relaxation", "Learning to relax your body on command is a fundamental building block of overcoming any sexual difficulty");
		Stage cognitive = new Stage(2, "Cognitive", "Here we help you monitor and respond differently to unhelpful thinking.");
		Stage relational = new Stage(3, "Relational", "Including your partner.");

		//create and load tasks for each stage
		psychEd.addTask(new PsychEdTask(0, "Coping with ED", "Chapter 3 - Developing Realistic Expectations"));
		Task completedTask = new PsychEdTask(1, "New Male Sexuality", "Chapter 2 - Male Sexual Myths");
		//completedTask.markComplete();
		psychEd.addTask(completedTask);
		
		
		relax.addTask(new RelaxationTask(3, "Body Scan 1", "Do 1 body scan meditation.", 30));
		relax.addTask(new RelaxationTask(4, "Body Scan 2", "Do 1 body scan meditation.", 30));
		relax.addTask(new RelaxationTask(5, "Mindful Meditation 1", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(6, "Mindful Meditation 2", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(7, "Mindful Meditation 3", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(8, "Mindful Meditation 4", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(9, "Mindful Meditation 5", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		
		
		CognitiveTask cog1 = new CognitiveTask(10, "Replace Negative Coalitions 1", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realiztic and balanced replacement though. "
				+ "Click here for further details.");
		cog1.setAutomaticThought("Everything I do fails.");
		cog1.setAlternativeThought("Sometimes I struggle, but other times I succeed.");
		cognitive.addTask(cog1);
		cognitive.addTask(new CognitiveTask(11, "Replace Negative Coalitions 2", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));
		cognitive.addTask(new CognitiveTask(12, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));

		relational.addTask(new CognitiveTask(13, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));
		relational.addTask(new CognitiveTask(14, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));

		//psychEd.markComplete();
		//relax.markComplete();

		//load stages into plan
		txPlan.addStage(psychEd, 0);
		txPlan.addStage(relax, 1);
		txPlan.addStage(cognitive, 2);
		txPlan.addStage(relational, 3);

		
		//now only put relevant data into session
		TreatmentPlan activeTx = user.getActiveTreatmentPlan();
		//Stage currentStage = activeTx.getCurrentStage();
		
		session.setAttribute("txPlan", activeTx);
		session.setAttribute("stages", txPlan.getStages());
		//session.setAttribute("currentStage", currentStage);

		
		request.getRequestDispatcher("taskReview.jsp").forward(request, response);
	}

}
