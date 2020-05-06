# InBetweenTherapy

In/Between Therapy is a mobile-first, web-based suite of customization tools to help facilitate the  psychotherapy process for people struggling with various problems.

http://inbetweentherapy.com/



# Site Overview

## Detailed Description

What make psychotherapy most effective is facilitating new experiences for people. While new experiences can and do happen in therapy sessions, in order for a person to get the most out of treatment, it is important that they practice what they learn in between sessions.



As stated in a [prominent psychology journal](http://digitalcommons.unl.edu/cgi/viewcontent.cgi?article=1373&context=psychfacpub):



*“Therapist recommendations to clients to perform out-of-session actions, commonly called homework, are related to positive psychotherapy outcomes. Results of a recent meta-analysis of 27 studies of cognitive and behavioral therapies, representing 375 clients, indicate that this relationship is strong (r = .36; 95% confidence interval [CI] = 0.23–0.48). In this study, homework compliance was also significantly correlated with positive outcomes. Furthermore, studies indicate that the homework–outcome relationship is linear, with clients who do the most homework improving more than clients who do little or no homework.”*



Interesting, a common problem with psychotherapy is that clients don’t do the homework! The most common reasons this happens are 1) due to the anxiety the person feels about trying something new and 2) because when in session, people’s emotions are usually heightened, which can often make remember details about assignments difficult, and therefore harder to remember to do between sessions.



This has a number of impacts on the speed and effectiveness of the therapy process. As a therapist, when a client returns the following week not having done any of the homework, you are faced with a dilemma. Do you explore the potential anxiety or avoidance that interfered or do you stick to making behavioral changes in hopes that they will lead to a greater chance of success the following week? If this only happens once it is usually not a problem. Unfortunately, this is often not the case and clients come back week after week not having done the homework. Now the therapist is in a greater predicament as continuing to explore the avoidance can lead to the client feeling shamed. A common reaction from the therapist at this point is then to collude in the avoidance, which obviously slows down the effectiveness of the therapy. 



As a client, it affect the speed and effectiveness because they are not having new experiences that encourage them and can show and reinforce new ways of thinking, being, and feeling. 



So this application is intended to serve as a road map of the therapy treatment plan and a reminder for the client to help make doing psychotherapy homework easier to remember and more fun to do.



Additionally, an important aspect of the site is to provide small, incremental positive reinforcements as the client engages in therapeutic tasks in between sessions. Right now this is provided through the common pleasant experience people have when “checking something off” as well as seeing the progress meter fill, but future changes would implement more robust reward systems (similar to achievements seen in video games like World of Warcraft and social media sites like Audible.com, StackExchange.com, etc…).



Another important feature not yet implemented is client notifications on their mobile devices about doing tasks.



In the future, the application could also be expanded to be used as a stand-alone app for people seeking self-help on a variety of problems without the assistance of a therapist. 

## Understanding the Mechanics/Architecture

### Technology

Java, MySQL, Javascript, Bootstrap (so it’s responsive and mobile ready)

### User Types

There are 3 types of clients

- **Client -** Are the primary consumers of the site. Their primary utilization of the site consists of being guided through Treatment Plans.
- **Therapist -** Work with clients. They assign Treatment Plans to their clients and collaborate around the use of the plans. Therapists have the ability to edit Treatment Plans in order to customize the plan to better fit each client. Future functionality will allow them to create their own Treatment Plans from scratch.
- **Admin -** Manages the core components of the site, which primarily entails the creation, modification, and removal of Treatment Plan templates and their child objects (Tasks, Stages, etc. see below for more details about these objects)

### Core Components

#### Treatment Plan

The site is built around one primary object: the **Treatment Plan**. The treatment plan is what outlines and guides the client through the steps of treatment that address the targeted **treatment issue** for which the clients is seeking therapy.



The treatment plan consists of different **Stages** and each stage consists of various **Tasks** that are to be completed by the client.



Examples of Treatment Plan titles: Cognitive Behavior Treatment For Depression, Phobia Treatment, Grief and Loss Treatment, Emotionally Focused Treatment for Couple Conflict Resolution…

#### Stage

The Stage the first order child of the Treatment Plan. Each stage has **stage goals** that define the desired accomplishments of the client by going through the stage. THe primary purpose of the stage is to be a container for related **Tasks**.



Stages are independent of Treatment Plans in that the same stage can be used in many different type of Treatment Plans.



Examples of Stages: Comfort and Relaxation, Changing Your Thinking, Developing Healthy Nutrition, Improving Sleep…

#### Task

The Task is the meat of the application. Tasks are what the client actually *does*. Client’s complete Tasks in order to complete Stages in order to complete the Treatment Plan.



On the Java side, Task is an abstract class to allow for the implementation of Tasks with different properties and fields. The concrete implementation of the base Task is the class **TaskGeneric**. All other types of tasks build off this. TaskGeneric is the most basic implementation of Task and only really has an *instructions* field. Additional Task subclasses could be written that have additional fields to allow for more complex Tasks. One example that has been implemented already is TaskTwoTextBoxes which simply adds 4 fields: Label 1, Value 1, Label 2, Value 2. Now instead of just having instructions, this task has the ability to contain 2 textboxes for data entry by the client.



Examples of Tasks: Read Chapter 3, Practice Deep Breathing, Take a walk outside, Meditate, Go To Bed at 9:30…
