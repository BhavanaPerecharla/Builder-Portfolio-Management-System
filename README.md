# Builder-Portfolio-Management-System

The local Builders' Association has requested a system to manage their construction projects portfolio. Currently, records of ongoing, completed, and upcoming projects are maintained manually using spreadsheets, leading to frequent data loss, errors, and poor client communication. 


The Builder Portfolio Management System is a role-based web application designed to streamline the operations of builders, project managers, and clients. The system allows builders to manage construction projects digitally, reducing the risk of data loss and improving coordination, budgeting, and progress tracking.


The System Features:  

1.Register and log in. 

2.Add, update, and delete project entries. 

3.Assign project status: Upcoming, In Progress, or Completed. 

4.Associate projects with clients and project managers. 

5.Upload relevant documents like blueprints, permits, and images. 

6.Track budget vs actual spends on projects. 




-------------⚙️ Tech Stack:----------------

1.Language: Java

2.Database: PostgreSQL

3.Database Connectivity: JDBC

4.Build Tool: Maven

5.IDE: IntelliJ IDEA / Eclipse

6.Execution: Console-based / CLI interaction or API-less logic simulation

-------------How to Set Up and Run Builder Portfolio Management System Locally------------

📋 How to Set Up and Run Builder Portfolio Management System Locally
Follow these steps to clone, configure, and run the system on your local machine.

✅ 1. Clone the Repository
Use the following commands to clone the repository and navigate into the project directory:

bash
Copy
Edit
git clone https://github.com/KattaPraneeth/BuilderPortfolioManagementSystem.git
cd BuilderPortfolioManagementSystem
✅ 2. Set Up PostgreSQL Database
Ensure PostgreSQL is installed and running on your machine.

Open the PostgreSQL terminal (psql).

Execute the following commands to create and connect to the database:

sql
Copy
Edit
CREATE DATABASE test;
\c test
✅ 3. Configure Database Credentials
Open the following file in your IDE or text editor:

swift
Copy
Edit
src/main/java/com/praneeth/Constants/DBConstants.java
Update the database connection details:

java
Copy
Edit
public class DBConstants {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    public static final String DB_USERNAME = "your_postgres_username";
    public static final String DB_PASSWORD = "your_postgres_password";
}
Replace your_postgres_username and your_postgres_password with your actual PostgreSQL credentials.

✅ 4. Execute SQL Setup Script
Locate the queries.sql file in your project.

Use the \i command in the PostgreSQL terminal to execute the SQL script:

sql
Copy
Edit
\i /path/to/queries.sql
Alternatively, copy-paste the SQL commands from queries.sql directly into your terminal after connecting to the test database.

✅ 5. Build the Project
From your project’s root directory, run the following Maven command to build and install dependencies:

bash
Copy
Edit
mvn clean install
Ensure Maven is installed on your system.

✅ 6. Run the Project
Option 1: Using an IDE
Open the project in IntelliJ IDEA, Eclipse, or any Java IDE.

Locate the Main class (containing the main method).

Run the application directly from the IDE.

Option 2: Using Terminal
If running from the command line:

bash
Copy
Edit
javac Main.java
java Main
Make sure you compile all necessary .java files if you're not using an IDE.

🎉 The application should now be running locally.
📌 Notes
Ensure your PostgreSQL server is running before starting the project.

If using features like password hashing, ensure required libraries are included (already handled via Maven if using the project’s pom.xml).




-----------👤 Author:- --------------------

Bhavana Perecharla

bhavanaperecharla@gmail.com

Guntur, Andhra Pradesh, India
