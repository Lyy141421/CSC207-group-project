Hello,

We've created a job application system using standard input. It requires 8 files for it to run.
7 of them are .json files that are used to store data relating to the application users, the companies, job postings
and job applications created and updated when the program is in use. The last file is a .txt file to store the previous
login date so that you may test the 30-day applicant document delete function outlined in the specification.
All of these files contain sample data so that the application should run when you first use it.

These are the assumptions that we made:
- Usernames are unique
- Company names are unique
- Each job posting can have multiple positions
- Each company can have multiple HR Coordinators
- Each company can have multiple interviewers, each belonging to a certain job field
- Each interview is between one interviewer and one applicant
- At every moment in time, all the interviews for a single job posting are for the same round

These are some key design decisions we made:
- An HR Coordinator does not have to sign-up to an existing company
    - If the company is not yet registered in the system, the application adds it when HR signs up
- An interviewer must sign-up to an existing company
- Job postings are considered 'open' on the same day that it is said to close
- Applicants can submit different CV's and cover letters for each job posting they apply to
- HR Coordinators do not explicitly match interviewers with applicants for interviews
    - This is done automatically in the system when the HR Coordinator selects the option to 'set-up interviews'
    such that the interviewer assigned has the least number of interviews scheduled at the time of set-up
- Interviewers schedule the date and time for the interview --- the applicant has no choice
- Interviewers can log into the application and fill out the necessary information to pass/fail an applicant after the
    interview date
- If the number of applicants in consideration is less than the number of positions, those in consideration are
    immediately hired and the job posting closes permanently
- If no applications are submitted for a job posting, the HR Coordinator can update its fields, including the close date
    if they want to. Or else, the job posting is removed from the system.
    - This is the only case when job postings are removed from the system. Everything else remains in the system and
    can be accessed by the HR Coordinator.