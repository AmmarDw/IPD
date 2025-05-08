# Inpatient Care Management System (ICMS)

Welcome to the **Inpatient Care Management System**, a Spring Boot & Thymeleaf web app for handling patient help‐requests and staff task workflows. All source code lives on GitHub:


---

## Screenshots
*Figure 1: Sign In*
![Login Screen](https://github.com/user-attachments/assets/f129b7c4-34dc-4ffe-b7e9-9663bc5233e1)  

*Figure 2: Create Account*
![Signup Screen](https://github.com/user-attachments/assets/ab580ee0-af47-47ad-a1a9-21cf0900e118)  

*Figure 3: Admin Home (“Welcome back, Admin!”)*
![Admin Dashboard](https://github.com/user-attachments/assets/ccfc7d11-31b8-444b-a281-dc377296bf4a)  

*Figure 4: Manage Request Options*
![Request Options](https://github.com/user-attachments/assets/dbda7191-7446-49e7-a94a-e4b892343e61)

*Figure 5: Monitor Employees*
![Employees View](https://github.com/user-attachments/assets/9420353b-75fa-4c31-9025-fdd96fea064b)  

*Figure 6: Employee Tasks Dashboard*
![Tasks Dashboard](https://github.com/user-attachments/assets/4b2bc850-1f10-4861-bfad-2a576f34045f)  

---

## API & Interface Endpoints

| Role                | Method | Endpoint                   | Description                                                                 |
|---------------------|--------|----------------------------|-----------------------------------------------------------------------------|
| **All users**       | POST   | `/login`                   | Authenticate (form login)                                                   |
|                     | GET    | `/logout`                  | Invalidate session                                                          |
| **Admin**           | GET    | `/adminDashboard`          | Landing page                                                               |
|                     | GET    | `/viewAllEmployees`        | List & filter employees                                                    |
|                     | GET    | `/manageRequestOptions`    | CRUD `RequestOption`s                                                      |
|                     | POST   | `/addRequestOption`        | Create new request type                                                    |
|                     | POST   | `/updateRequestOption`     | Update description, priority, role                                          |
|                     | POST   | `/deleteRequestOption`     | Remove a request option                                                    |
| **Medical Staff**   | GET    | `/employeeTasksDashboard`  | View personal task buckets (pending/in-progress/completed)                  |
|                     | POST   | `/startTask`               | Mark task “In Progress”                                                     |
|                     | POST   | `/completeTask`            | Mark task “Completed”                                                       |
|                     | POST   | `/updateEmployeeStatus`    | Change own on-duty status                                                  |
| **Patients**        | GET    | `/requestOptions`          | Browse available request types                                              |
|                     | POST   | `/requestHelp`             | Submit a help request → auto-assign to least-busy eligible employee         |

---

## How to Build & Deploy

1. **Clone & Setup**  
   ```bash
   git clone https://github.com/AmmarDw/IPD
2. **Configure**

- Edit `application.properties` to include your MySQL credentials.
- Ensure roles and the initial admin user are seeded on startup.

3. **Run**

To run the application, use the following command:

```bash
./mvnw spring-boot:run
```
## Project KPIs & Course Outcomes

This work fulfills SE324 Student Outcomes SO1 and SO6:

### Outcome 1: Identify, Formulate & Solve Complex Problems

The student should be able to:

#### 1.1 Identify the Problem
- Gather background information about the system/domain.
- Define inputs, outputs, requirements & constraints.
- Formulate concise problem statements.

#### 1.2 Formulate the Problem
- Analyze via relevant theories, models & frameworks.
- Visualize structure with UML (use cases, activity, sequence, class & state diagrams).
- Prototype or simulate to explore potential solutions.

#### 1.3 Solve the Problem
- Implement efficient, maintainable code using appropriate languages & tools.
- Develop & execute test cases to validate the solution.

### Outcome 6: Experimentation & Data Analysis

The student should be able to:

#### 6.1 Develop & Conduct Experiments
- Set up and execute tests systematically.
- Use proper tools/software to collect data.

#### 6.2 Analyze Data
- Ensure accuracy & integrity of collected data.
- Identify patterns, trends & anomalies.

#### 6.3 Interpret & Report Results
- Draw insights and engineering-judgment conclusions.
- Prepare detailed reports documenting methods, analysis & outcomes.


