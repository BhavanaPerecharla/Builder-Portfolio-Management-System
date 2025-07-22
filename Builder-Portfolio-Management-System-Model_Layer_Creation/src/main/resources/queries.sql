-- Project Table
CREATE TABLE project (
    project_Id VARCHAR(50) NOT NULL,
    project_Name VARCHAR(100) NOT NULL,
    project_Description VARCHAR(255),
    project_Start_Date DATE NOT NULL,
    project_Est_Complete_Date DATE,
    project_Actual_Complete_Date DATE,
    project_Status VARCHAR(50) NOT NULL CHECK (project_Status IN ('Upcoming', 'In Progress', 'Completed')),
    Manager_Id VARCHAR(50) NOT NULL,
    Client_Id VARCHAR(50) NOT NULL,
    Builder_Id VARCHAR(50) NOT NULL,
    estimated_Cost DECIMAL(10, 2),
    PRIMARY KEY (project_Id),
    FOREIGN KEY (Manager_Id) REFERENCES manager(manager_Id),
    FOREIGN KEY (Client_Id) REFERENCES client(client_Id),
    FOREIGN KEY (Builder_Id) REFERENCES builder(builder_Id)
);

-- Project Expenses Table
CREATE TABLE project_expenses (
    project_Id VARCHAR(50) NOT NULL,
    payment_Id INT NOT NULL AUTO_INCREMENT,
    payment_Date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_description VARCHAR(255),
    PRIMARY KEY (project_Id, payment_Id),
    FOREIGN KEY (project_Id) REFERENCES project(project_Id)
);


-- Task Table
CREATE TABLE task (
    task_Id INT NOT NULL ,
    project_Id VARCHAR(50) NOT NULL,
    task_name VARCHAR(100) NOT NULL,
    task_start_date DATE NOT NULL,
    task_est_complete_date DATE,
    progress INT NOT NULL,
    PRIMARY KEY (task_Id),
    FOREIGN KEY (project_Id) REFERENCES project(project_Id)
);

-- Admin Table
CREATE TABLE admin (
    admin_Id VARCHAR(50) NOT NULL,
    admin_Name VARCHAR(100) NOT NULL,
    admin_Email VARCHAR(100) NOT NULL,
    admin_Password VARCHAR(100) NOT NULL,
    admin_Contact VARCHAR(100),
    admin_AddressId INT NOT NULL,
    PRIMARY KEY (admin_Id),
    FOREIGN KEY (admin_AddressId) REFERENCES address(address_Id)
);

-- Builder Table
CREATE TABLE builder (
    builder_Id VARCHAR(50) NOT NULL,
    builder_Name VARCHAR(100) NOT NULL,
    builder_Email VARCHAR(100) NOT NULL,
    builder_password VARCHAR(100) NOT NULL,
    builder_Contact VARCHAR(100),
    builder_AddressId INT NOT NULL,
    PRIMARY KEY (builder_Id),
    FOREIGN KEY (builder_AddressId) REFERENCES address(address_Id)
);

-- Client Table
CREATE TABLE client (
    client_Id VARCHAR(50) NOT NULL,
    client_Name VARCHAR(100) NOT NULL,
    client_Email VARCHAR(100) NOT NULL,
    client_Password VARCHAR(100) NOT NULL,
    client_Contact VARCHAR(100),
    client_AddressId INT NOT NULL,
    client_type VARCHAR(50) NOT NULL CHECK (client_type IN ('INDIVIDUAL', 'CORPORATE')),
    PRIMARY KEY (client_Id),
    FOREIGN KEY (client_AddressId) REFERENCES address(address_Id)
);

-- Manager Table
CREATE TABLE manager (
    manager_Id VARCHAR(50) NOT NULL,
    manager_Name VARCHAR(100) NOT NULL,
    manager_Email VARCHAR(100) NOT NULL,
    manager_Password VARCHAR(100) NOT NULL,
    manager_Contact VARCHAR(100),
    manager_status VARCHAR(50) NOT NULL CHECK (manager_status IN ('free', 'working')),
    builder_Id VARCHAR(50) NOT NULL,
    manager_AddressId INT NOT NULL,
    PRIMARY KEY (manager_Id),
    FOREIGN KEY (builder_Id) REFERENCES builder(builder_Id),
    FOREIGN KEY (manager_AddressId) REFERENCES address(address_Id)
);

-- Address Table
CREATE TABLE address (
    address_Id INT NOT NULL AUTO_INCREMENT,
    address_Line1 VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_Code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    PRIMARY KEY (address_Id)
);
