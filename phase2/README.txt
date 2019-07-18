Hello,

We've created a jobApplicationSystem application system using standard input. It requires 8 files for it to run.
7 of them are .json files that are used to store data relating to the application users, the companies, jobApplicationSystem postings
and jobApplicationSystem applications created and updated when the program is in use. As a result, YOU MUST ADD ALL THE LIBRARIES IN THE
'lib' DIRECTORY SO THAT THE APPLICATION WILL RUN!

The last file is a .txt file to store the previous login date so that you may test the 30-day applicant document delete
function outlined in the specification.
All of these files contain sample data so that the application should run when you first use it.

The file loading data into memory occurs when you first press the run button in the IDE and the file saving occurs when
you enter '-1' to close the program when prompted.

These are the assumptions that we made:
- Usernames are unique
- Company names are unique
- Each jobApplicationSystem posting can have multiple positions
- Each branch can have multiple HR Coordinators
- Each branch can have multiple interviewers, each belonging to a certain jobApplicationSystem field
- Each interview is between one interviewer and one applicant
- At every moment in time, all the interviews for a single jobApplicationSystem posting are for the same round

These are some key design decisions we made:
- An HR Coordinator does not have to sign-up to an existing branch
    - If the branch is not yet registered in the system, the application adds it when HR signs up
- An interviewer must sign-up to an existing branch
- Job postings are considered 'open' on the same day that it is said to close
- Applicants can submit different CV's and cover letters for each jobApplicationSystem posting they apply to
- HR Coordinators do not explicitly match interviewers with applicants for interviews
    - This is done automatically in the system when the HR Coordinator selects the option to 'set-up interviews'
    such that the interviewer assigned has the least number of interviews scheduled at the time of set-up
- Interviewers schedule the date and time for the interview --- the applicant has no choice
- Interviewers can log into the application and fill out the necessary information to pass/fail an applicant on and
    after the interview date (the time slot is assumed to be followed if on the day of)
- If the number of applicants in consideration is less than the number of positions, those in consideration are
    immediately hired and the jobApplicationSystem posting closes permanently
- If no applications are submitted for a jobApplicationSystem posting, the HR Coordinator can update its fields, including the close date
    if they want to. Or else, the jobApplicationSystem posting is removed from the system.
    - This is the only case when jobApplicationSystem postings are removed from the system. Everything else remains in the system and
    can be accessed by the HR Coordinator.
-
