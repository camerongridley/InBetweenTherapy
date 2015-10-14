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
import com.cggcoding.models.tasktypes.GenericTask;
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
		String requestedAction = request.getParameter("requestedAction");
		User user = (User)session.getAttribute("user");

		TreatmentPlan activeTx = null;


		//session.setAttribute("stages", treatmentPlan.getStages());
		//session.setAttribute("currentStage", currentStage);
		String forwardTo = "index.jsp";

		if(user.hasRole("client")){
			UserClient userClient = (UserClient)session.getAttribute("user");
			//load all treatmentPlans of the user.  If one is marked as inProgress put it is session and go straight to task-review.jsp
			//if none are inProgress then offer 1-2 lists of choices: the default templates and if applicable, any from associated therapist
			activeTx = buildClientData(userClient);
			
			//when actually getting data from database will need to set a few variables after the treatment plan is fully loaded - revise the following
			activeTx.initialize();
			
			userClient.addTreatmentPlan(activeTx);
			userClient.setActiveTreatmentPlanId(activeTx.getTreatmentPlanID());
			switch (requestedAction) {
				case "continue":
					forwardTo = "jsp/task-review.jsp";
					break;
				case "newplan":
					forwardTo = "index.jsp";
				default:
					forwardTo = "index.jsp";
			}

		} else if(user.hasRole("therapist")){
			UserTherapist userTherapist = (UserTherapist)session.getAttribute("user");
			//put the default plan templates in session
			session.setAttribute("treatmentPlanTemplates", getTxPlanTemplateList(userTherapist));
			//put the therapists owned treatmentPlans in session - or just load them into the UserTherapist object
			activeTx = buildTherapistData(userTherapist);

			switch (requestedAction) {
				case "createplan":

					request.setAttribute("defaultTreatmentIssues", getDefaultTxIssueList());
					request.setAttribute("customTreatmentIssues", getCustomTxIssueList());
					request.setAttribute("defaultTaskTypes", getDefaultTasksTypes());


					forwardTo = "/jsp/treatment-plans/treatment-plan-create-name.jsp";
					break;
				default:
					forwardTo = "index.jsp";
			}
		}

		request.setAttribute("treatmentPlan", activeTx);
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
		return buildDefaultEDPlan(userTherapist);  //TEMP FOR NOW!!!!
	}

	private TreatmentPlan buildClientData(UserClient userClient){

		TreatmentPlan tempTxPlanForTesting = buildDefaultEDPlan(userClient);
		userClient.addTreatmentPlan(tempTxPlanForTesting);
		userClient.setActiveTreatmentPlanId(tempTxPlanForTesting.getTreatmentPlanID());
		//now only put relevant data into session
		TreatmentPlan activeTx = userClient.getActiveTreatmentPlan();
		//Stage currentStage = activeTx.getCurrentStage();

		return activeTx;
	}

	private List<TreatmentPlan> getTxPlanTemplateList(User user){
		List<TreatmentPlan> treatmentPlanTemplateList = new ArrayList<>();

		treatmentPlanTemplateList.add(buildDefaultEDPlan(user));

		return treatmentPlanTemplateList;
	}

	private TreatmentPlan buildDefaultEDPlan(User user){
		TreatmentPlan treatmentPlan = null;//new TreatmentPlan(0,user.getUserID(), "ED", "Erectile dysfunction", 0);

		/*
		 * 
		 * //create stages
		Stage psychEdStage = Stage.getInstanceAndCreateID(user.getUserID(), treatmentPlan.getTreatmentPlanID(), "PsychoEducation", "Important concepts to learn about the problem you are experiencing.  Understanding some of these core concept can help you feel confident about the treatment strategies implemented here.", 0);
		psychEdStage.setGoals(new ArrayList<>(Arrays.asList(StageGoal.getInstance(1, psychEdStage.getStageID(), "Have a better understanding of the human sexual response."), StageGoal.getInstance(2, psychEdStage.getStageID(),"Learned about common myths of male sexuality."))));

		Stage relaxStage = Stage.getInstanceAndCreateID(user.getUserID(), treatmentPlan.getTreatmentPlanID(), "Relaxation", "Learning to relax your body on command is a fundamental building block of overcoming any sexual difficulty", 1);
		relaxStage.setGoals(new ArrayList<>(Arrays.asList(StageGoal.getInstance(3, relaxStage.getStageID(),"Notice the physiological sensations that indicated I am feeling stress or tension."), StageGoal.getInstance(4, relaxStage.getStageID(),"Be able to begin relaxation on cue."), StageGoal.getInstance(5, relaxStage.getStageID(),"To mindfully meditate for 15 minutes."))));

		Stage cognitiveStage = Stage.getInstanceAndCreateID(user.getUserID(), treatmentPlan.getTreatmentPlanID(), "Cognitive", "Here we help you monitor and respond differently to unhelpful thinking.", 2);
		cognitiveStage.setGoals(new ArrayList<>(Arrays.asList(StageGoal.getInstance(6, cognitiveStage.getStageID(),"Be able to identify irrational thoughts and form a balanced and realistic replacement thought."), StageGoal.getInstance(7, cognitiveStage.getStageID(), "Breathe and feel my body shift out of negative thinking and release/relax."))));

		Stage relationalStage = Stage.getInstanceAndCreateID(user.getUserID(), treatmentPlan.getTreatmentPlanID(), "Relational", "Including your partner.", 3);
		relationalStage.setGoals(new ArrayList<>(Arrays.asList(StageGoal.getInstance(8, relationalStage.getStageID(), "Have conversation x with my partner."), StageGoal.getInstance(9, relationalStage.getStageID(), "Done sensate focus to the point of comfort."))));

		//create and load tasks for each stage
		//TODO have Task creation go through a factory or factory method
		//psychEdStage.addTask(GenericTask.getInstanceFull(0, psychEdStage.getStageID(), user.getUserID(), 1, 0, "Coping with ED!!!", "Chapter 3 - Developing Realistic Expectations", "", false, null, 0, false, false));
		psychEdStage.addTask(new PsychEdTask(0, user.getUserID(), "Coping with ED", "Chapter 3 - Developing Realistic Expectations"));
		Task completedTask = new PsychEdTask(1, user.getUserID(), "New Male Sexuality", "Chapter 2 - Male Sexual Myths");
		completedTask.markComplete();
		psychEdStage.addTask(completedTask);
		psychEdStage.updateProgress();

		RelaxationTask relaxationTask1 = new RelaxationTask(user.getUserID(), 3, "Body Scan", "Do 1 body scan meditation.", 30);
		relaxStage.addTaskSet(relaxationTask1, 4);
		relaxStage.addTask(new RelaxationTask(4, user.getUserID(), "Mindful Mean", "Eat a meal mindfully", 30));
		relaxStage.addTask(new RelaxationTask(5, user.getUserID(), "Mindful Meditation 1", "Do a breath awareness or breath and body awareness mindful meditation.", 10));



		CognitiveTask cog1 = new CognitiveTask(10, user.getUserID(), "Replace Negative Cognition 1", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.");
		cog1.setAutomaticThought("Everything I do fails.");
		cog1.setAlternativeThought("Sometimes I struggle, but other times I succeed.");
		cognitiveStage.addTask(cog1);
		CognitiveTask cog2 = new CognitiveTask(11, user.getUserID(), "Replace Negative Cognition 2", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details.");
		cognitiveStage.addTaskSet(cog2,3);
		cognitiveStage.addTask(new CognitiveTask(12, user.getUserID(), "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));

		relationalStage.addTask(new CognitiveTask(13, user.getUserID(), "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));
		relationalStage.addTask(new CognitiveTask(14, user.getUserID(), "Replace Negative Coalitions 3", "Throughout the day, pay attention to any negative thoughts you have about ED, your body, sex, etc.  "
				+ "Once you notice it, pause, take a breath to get some distance from the thought, and then think of a realistic and balanced replacement though. "
				+ "Click here for further details."));

		//psychEd.markComplete();
		//relax.markComplete();

		//load stages into plan
		treatmentPlan.addStage(psychEdStage);
		treatmentPlan.addStage(relaxStage);
		treatmentPlan.addStage(cognitiveStage);
		treatmentPlan.addStage(relationalStage);
		
		*/

		return treatmentPlan;
	}
}
