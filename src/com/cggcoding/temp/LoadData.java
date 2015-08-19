package com.cggcoding.temp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.*;
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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//load all data - in place of a db call since the db is not yet implemented
		HttpSession session = request.getSession();
		String chosenAction = request.getParameter("chosenAction");
		User user = (User)session.getAttribute("user");

		TreatmentPlan activeTx = null;


		//session.setAttribute("stages", txPlan.getStages());
		//session.setAttribute("currentStage", currentStage);
		String forwardTo = "index.jsp";

		if(user.hasRole("client")){
			UserClient userClient = (UserClient)session.getAttribute("user");
			//load all txPlans of the user.  If one is marked as inProgress put it is session and go straight to taskReview.jsp
			//if none are inProgress then offer 1-2 lists of choices: the default templates and if applicable, any from associated therapist
			activeTx = buildClientData(userClient);
			userClient.addTreatmentPlan(activeTx);
			userClient.setActiveTreatmentPlanId(activeTx.getTreatmentPlanID());
			switch (chosenAction) {
				case "continue":
					forwardTo = "taskReview.jsp";
					break;
				case "newplan":
					forwardTo = "index.jsp";
				default:
					forwardTo = "index.jsp";
			}

		} else if(user.hasRole("therapist")){
			UserTherapist userTherapist = (UserTherapist)session.getAttribute("user");
			//put the default plan templates in session
			session.setAttribute("txPlanTemplates", getTxPlanTemplateList());
			//put the therapists owned txPlans in session - or just load them into the UserTherapist object
			activeTx = buildTherapistData(userTherapist);

			switch (chosenAction) {
				case "createplan":

					request.setAttribute("defaultTxIssues", getDefaultTxIssueList());
					request.setAttribute("customTxIssues", getCustomTxIssueList());
					request.setAttribute("defaultTaskTypes", getDefaultTasksTypes());


					forwardTo = "therapisttools/createtxplan-name.jsp";
					break;
				default:
					forwardTo = "index.jsp";
			}
		}

		//session.setAttribute("txPlan", activeTx);
		request.setAttribute("txPlan", activeTx);
		request.getRequestDispatcher(forwardTo).forward(request, response);

	}

	private List<TreatmentIssue> getDefaultTxIssueList(){
		List<TreatmentIssue> txIssues = new ArrayList<>();
		txIssues.add(new TreatmentIssue(0, "ED"));
		txIssues.add(new TreatmentIssue(1, "PE"));
		txIssues.add(new TreatmentIssue(2, "Low Desire"));
		txIssues.add(new TreatmentIssue(3, "Difficulty with Orgasm"));

		return txIssues;
	}

	private List<TreatmentIssue> getCustomTxIssueList(){
		List<TreatmentIssue> txIssues = new ArrayList<>();
		txIssues.add(new TreatmentIssue(0, "Phobia of water."));
		txIssues.add(new TreatmentIssue(1, "Anxiety with physical touch."));
		txIssues.add(new TreatmentIssue(2, "Commitment fear."));


		return txIssues;
	}

	private List<String> getDefaultTasksTypes(){
		List<String> allTaskTypes = new ArrayList<>();

		allTaskTypes.add("Generic");
		allTaskTypes.add("Psychoeducation");
		allTaskTypes.add("Relaxation");
		allTaskTypes.add("Cognitive");
		allTaskTypes.add("Relational");

		return allTaskTypes;

	}

	private TreatmentPlan buildTherapistData(UserTherapist userTherapist){
		return buildDefaultEDPlan();  //TEMP FOR NOW!!!!
	}

	private TreatmentPlan buildClientData(UserClient userClient){

		TreatmentPlan tempTxPlanForTesting = buildDefaultEDPlan();
		userClient.addTreatmentPlan(tempTxPlanForTesting);
		userClient.setActiveTreatmentPlanId(tempTxPlanForTesting.getTreatmentPlanID());
		//now only put relevant data into session
		TreatmentPlan activeTx = userClient.getActiveTreatmentPlan();
		//Stage currentStage = activeTx.getCurrentStage();

		return activeTx;
	}

	private List<TreatmentPlan> getTxPlanTemplateList(){
		List<TreatmentPlan> txPlanTemplateList = new ArrayList<>();

		txPlanTemplateList.add(buildDefaultEDPlan());

		return txPlanTemplateList;
	}

	private TreatmentPlan buildDefaultEDPlan(){
		TreatmentPlan txPlan = new TreatmentPlan(0, "ED", "Erectile dysfunction", 0);

		//create stages
		Stage psychEd = new Stage(0, "PsychoEducation", "Important concepts to learn about the problem you are experiencing.  Understanding some of these core concept can help you feel confident about the treatment strategies implemented here.");
		psychEd.setGoals(new ArrayList<>(Arrays.asList("Have a better understanding of the human sexual response.", "Learned about common myths of male sexuality.")));

		Stage relax = new Stage(1, "Relaxation", "Learning to relax your body on command is a fundamental building block of overcoming any sexual difficulty");
		relax.setGoals(new ArrayList<>(Arrays.asList("Notice the physiological sensations that indicated I am feeling stress or tension.", "Be able to begin relaxation on cue.", "To mindfully meditate for 15 minutes.")));

		Stage cognitive = new Stage(2, "Cognitive", "Here we help you monitor and respond differently to unhelpful thinking.");
		cognitive.setGoals(new ArrayList<>(Arrays.asList("Be able to identify irrational thoughts and form a balanced and realistic replacement thought.", "Breathe and feel my body shift out of negative thinking and release/relax.")));

		Stage relational = new Stage(3, "Relational", "Including your partner.");
		relational.setGoals(new ArrayList<>(Arrays.asList("Have conversation x with my partner.", "Done sensate focus to the point of comfort.")));

		//create and load tasks for each stage
		psychEd.addTask(new PsychEdTask(0, "Coping with ED", "Chapter 3 - Developing Realistic Expectations", 1));
		Task completedTask = new PsychEdTask(1, "New Male Sexuality", "Chapter 2 - Male Sexual Myths", 1);
		completedTask.markComplete();
		psychEd.addTask(completedTask);
		psychEd.updateProgress();


		relax.addTask(new RelaxationTask(3, "Body Scan 1", "Do 1 body scan meditation.", 2, 30));
		relax.addTask(new RelaxationTask(4, "Body Scan 2", "Do 1 body scan meditation.", 1, 30));
		relax.addTask(new RelaxationTask(5, "Mindful Meditation 1", "Do a breath awareness or breath and body awareness mindful meditation.", 3, 10));
		//relax.addTask(new RelaxationTask(6, "Mindful Meditation 2", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(7, "Mindful Meditation 3", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(8, "Mindful Meditation 4", "Do a breath awareness or breath and body awareness mindful meditation.", 10));
		//relax.addTask(new RelaxationTask(9, "Mindful Meditation 5", "Do a breath awareness or breath and body awareness mindful meditation.", 10));


		CognitiveTask cog1 = new CognitiveTask(10, "Replace Negative Coalitions 1", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realiztic and balanced replacement though. "
				+ "Click here for further details.",4);
		cog1.setAutomaticThought("Everything I do fails.");
		cog1.setAlternativeThought("Sometimes I struggle, but other times I succeed.");
		cognitive.addTask(cog1);
		cognitive.addTask(new CognitiveTask(11, "Replace Negative Coalitions 2", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.",3));
		cognitive.addTask(new CognitiveTask(12, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.",1));

		relational.addTask(new CognitiveTask(13, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.",3));
		relational.addTask(new CognitiveTask(14, "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.",2));

		//psychEd.markComplete();
		//relax.markComplete();

		//load stages into plan
		txPlan.addStage(psychEd, 0);
		txPlan.addStage(relax, 1);
		txPlan.addStage(cognitive, 2);
		txPlan.addStage(relational, 3);

		return txPlan;
	}
}
