# ExpenseTracker


## Expense Tracker Spring Boot Application Documentation

Expense Tracker is a Spring Boot web application that allows users to manage their expenses and track their budgets. This application is built using Java with Spring Boot, which offers a rapid development platform with built-in support for dependency injection, security, and data access.

## Overview

This application is built using the following features provided by Spring Boot:

* Spring MVC for building web applications
* Spring Data JPA for data access
* Spring Security for authentication and authorization
* Thymeleaf template engine for rendering views
* PostgreSQL databse as a main database for application
* H2 database for running tests
* OAuth 2.0 Log In with OpenID Connect and non-standard OAuth 2.0 Login (GitHub)
* Deployed In Heroku
* Spring boot testing
* The application provides a simple user interface for managing expenses, categories, budgets, and user profiles. The main class of the application is the ExpenseController class, which handles the HTTP requests, interacts with the repositories, and returns the appropriate views.

## Key features of the Expense Tracker application include:

* User Authentication: Users can signup or sign in using their GitHub account through OAuth2, providing a secure and convenient way to access the application.
* Expense Management: Users can add, edit, and delete expenses, as well as associate them with different categories.
* Expense Reporting: Users can view a summary of their expenses, including total expenses, remaining budget, and a list of expenses filtered by various criteria.
* Expense Search: Users can search by date, remark, category
* User Profile Management: Users can update their profile information and manage their budget.
* Admin Features: Administrators have the ability to manage user accounts.

Components
Repositories
The application uses Spring Data JPA repositories to handle data access. The following repositories are used:

ExpenseRepository: Repository for managing the Expense entities.
CategoryRepository: Repository for managing the Category entities.
AppUserRepository: Repository for managing the AppUser entities.
Entities
The application has the following entities:

Expense: Represents an expense entry with information such as date, amount, category, and remark.
Category: Represents a category of expenses.
AppUser: Represents a user of the application with information such as first name last name, password, role, username, email, and budget.

Controller
The ExpenseController class is responsible for handling HTTP requests and managing the flow of the application. It uses the repositories to interact with the data and returns the appropriate views. The controller has methods for the following operations:

* Displaying the login page
* Displaying the expense list
* Adding, editing, and deleting expenses
* Searching for expenses based on date, category, or remark
* Managing user profiles
* Managing budgets (updating and adding to the budget)
* Displaying the user list (only for admins)
* Deleting users (only for admins)
* Security
* The application uses Spring Security for authentication and authorization. Users are required to log in with a username and password to access the application. Spring Security is also used to restrict access to certain pages and actions based on the user's role (admin or regular user).

The UserController class is resposible for handling HTTP requests related to signing up a new user.

Redirecting to signup form.
Saving a new user.

Views
The application uses Thymeleaf templates to render the views. The following views are available:

* login: The login page
* expenselist: The main expense list view
* addexpense: The form for adding a new expense
* editexpense: The form for editing an existing expense
* userprofile: The user's profile page
* userlist: The list of users (only for admins)
* Building the Application
* This application is built using Maven as the build tool. To build the application, run the following command from the root directory of the project:

`mvn clean install`


This will compile the application, run the tests, and generate an executable JAR file in the target directory.

To run the application, execute the following command:

`java -jar target/sakhi-expense-tracker-0.0.1-SNAPSHOT.jar`

The application will be accessible at http://localhost:8080.

To deploy in Heroku should be installed Heroku CLI. 

should be added adds-on Heroku PostgreSQL database and configured application.properties. 


    ```
    spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    ```



