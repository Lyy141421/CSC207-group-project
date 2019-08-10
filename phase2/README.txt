These are the assumptions that we made:
- Usernames are unique
- Company names are unique
- A company can have multiple branches --> the branches would represent the physical workplaces for one company
- Each branch can have multiple HR Coordinators
- Each branch can have multiple interviewers, each belonging to a certain jobApplicationSystem field
- Each interview is between one interviewer and one applicant
- At every moment in time, all the interviews for a single jobApplicationSystem posting are for the same round


These are some key design decisions we made:
- An HR Coordinator does not have to sign-up to an existing company and/or branch
    > If the company and/or branch is not yet registered in the system, the application adds it when HR signs up
- An interviewer must sign-up to an existing branch
- Job postings are considered 'open' on the same day that it is said to close
- Applicants can submit different CVs, cover letters and other types of documents for each jobApplicationSystem posting
  they apply to
	> For the purposes of this project, we are assuming that all files submitted from the user’s local computer are .txt
	  files --> this is so that the job application grader can work
- HR Coordinators do not explicitly match interviewers with applicants for one-on-one interviews
    > This is done automatically in the system when the interview configuration is initialized by the HR coordinator and
      the system reads that the next round of interviews is one-on-one
    > Interviewers are chosen such that the interviewer assigned has the least number of interviews scheduled at the
      time of set-up in the branch for the specific job field
- Interviewers schedule the date and time for one-on-one interviews --- the applicant has no choice
- The HR Coordinator must choose the interviewers involved in each group interview session
	> Each group interview can have multiple interviewers and multiple applicants
    > Only the interviewer designated as the interview coordinator can set the results of the interview (ie pass/fail
      for each applicant involved)
    > The other (secondary) interviewers may only write interview notes
- Interviewers can log into the application and fill out the necessary information to pass/fail an applicant after the
  interview date
- If the number of applicants in consideration is less than the number of positions, those in consideration are
  immediately hired and the jobApplicationSystem posting closes permanently
- If no applications are submitted for a jobApplicationSystem posting, the HR Coordinator can update its fields,
  including the close date if they want to
- If at any point in time, there are no job applications in consideration for a job posting (ie all applicants have been
  rejected by interviewers along the way), the job posting will be automatically closed and the number of positions will be set to 0


To run the program:
- Go into the "files" directory. It should have a CMA_per_FSA_Centroid.json file. If there is any other files in this
  directory please delete them.
    > The additional files are .txt and .ser, which stores anything created/changed while running the program. You might
      encounter errors due to information not saved properly (especially if you exit the program, which we advice
      against), which is explained further below. If you get any unexpected behaviours that you suspect is related to
      memory, please remove the .ser and .txt files created and try the test again without exiting or interrupting the
      program.
- Documents submitted by users are stored in the "uploadedDocuments" directory. Whenever you decide to clear the files
  in "files" directory, you should also clear the "uploadedDocuments" directory.
- The main method is in the "Main" package in JobApplicationSystem class. Please run the code from there and you should
  see a GUI show up.


The following operations should be avoided, as they may lead to from minor bugs to the program crashing:
- When you run the program for the first time, please do not exit the program immediately. You should at least get to
  the stage where you set today's date.
- When there is a pop-up dialog, never close the dialog with x button.
- Please add an interviewer in the field of the job posting created before logging in as HR to review the applications
  after the job posting close date passes.
- We have a memory inconsistency issue due to Swing that we were unable to completely resolve.
	> More specifically, when logging out and logging back into the system without even exiting the program, some
	  changes would be lost or overridden by a previous change --> we have no control over this and we spoke to Lindsey
	  about this whose solution was to save and load the data multiple times, but we found this to aggravate the whole
	  situation even more
	> Because of this, completely exiting the program and restarting it would also worsen the memory inconsistency
	  problem
	> For the most part, the program should run as it is supposed to, but at some times, there would be some unusual
	  results
- HR coordinator has many tasks to complete. We have a separate file on things to note when logging in as HR. It is in
  phase2\src\GUIClasses\HRInterface\HRInstructions.txt.


** As mentioned, implementing GUI with Swing led to some memory inconsistency issue that were not directly related to
   our design. We tried our best to fix it as much as possible but couldn't get to every single occurrence by the
   deadline. Past the submission deadline, we continued to work on resolving the memory issue and have came to a
   slightly better version of code. We have submitted the update as Phase2LessCorruptedVersion.zip. The version
   submitted by deadline has everything design-wise, but if you ran the GUI and encountered some unexpected behaviours,
   we suggest trying the code in Phase2LessCorruptedVersion.zip as the new version fixed some memory corruption related
   issues.
** We have test code and println statements left in some places that we were using to attempt to fix the memory problem.
   We didn't have enough time to remove them at the end.
