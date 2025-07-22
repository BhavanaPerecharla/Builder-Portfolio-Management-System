# 🏗️ Builder Portfolio Management System

The local Builders' Association requested a system to manage their construction projects portfolio. Previously, project records were maintained manually using spreadsheets, leading to frequent data loss, human errors, and poor client communication.

The **Builder Portfolio Management System** is a **role-based Java application** designed to streamline operations for builders, project managers, and clients. This system enables builders to manage construction projects digitally, reducing data loss while improving coordination, budgeting, and project tracking.

---

## 🚀 System Features

- ✅ Register and log in (Role-based: Admin, Builder, Client, Manager)
- ✅ Add, update, and delete project entries
- ✅ Assign project status: Upcoming, In Progress, or Completed
- ✅ Associate projects with clients and project managers
- ✅ Upload documents (blueprints, permits, images)
- ✅ Track budget vs actual spending

---

## ⚙️ Tech Stack

- **Language:** Java
- **Database:** PostgreSQL
- **Database Connectivity:** JDBC
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA / Eclipse
- **Execution:** Console-based / CLI interaction

---

## 📋 How to Set Up and Run the Project Locally



✅ 1. Clone the Repository

git clone https://github.com/BhavanaPerecharla/Builder-Portfolio-Management-System

cd BuilderPortfolioManagementSystem


 ✅ 2. Set Up PostgreSQL Database
 
Ensure PostgreSQL is installed and running.

Open PostgreSQL terminal (psql).

CREATE DATABASE test;
\c test


✅ 3. Configure Database Credentials

Open this file:

src/main/java/com/resources/DBProperties.java


Update your PostgreSQL credentials:

public class DBConstants {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    
    public static final String DB_USERNAME = "your_postgres_username";
    
    public static final String DB_PASSWORD = "your_postgres_password";
}

✅ 4. Execute SQL Setup Script
Locate the queries.sql file.


In PostgreSQL terminal, run:


\i /path/to/queries.sql


Or manually paste the SQL commands into the terminal after connecting to the test database.


✅ 5. Build the Project
In the project’s root directory, execute:

mvn clean install

✅ 6. Run the Project
Option 1: Using IDE

Open the project in IntelliJ IDEA, Eclipse, etc.




Locate and run the Main class.
-----------------------------------------------------
🎉 Application should now be running locally!
📌 Important Notes
Ensure PostgreSQL server is running before starting the system.

Password hashing and encryption utilities are handled via Maven dependencies.

------------------------------------------------------
👤 Author
Bhavana Perecharla
📧 bhavanaperecharla@gmail.com
📍 Guntur, Andhra Pradesh, India



