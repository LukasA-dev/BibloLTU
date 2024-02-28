# BibloLTU Library Management System

## Description
BibloLTU is a JavaFX-based application designed to streamline the management of library operations. It offers a user-friendly interface for managing book loans, returns, and inventory, making it ideal for small to medium-sized library settings.

BibloLTU is a project made in Java, for the course
D0024E, Programutveckling med Java II at LTU.

## Technology Stack

- **Programming Language**: Java
- **GUI Framework**: JavaFX
- **Database**: MySQL
- **Build Tool**: Maven
- **IDE**: IntelliJ IDEA
- **Version Control**: Git

## Functionalities

BibloLTU includes features to manage day-to-day operations of a library:

### User Management
- **Login/Logout**: Secure authentication system for different user roles (Admin, Student, Researcher, Guest).
- **User Roles**: Different access levels and functionalities based on the user role.
- **Edit User**: Admins can edit user details, including changing user roles.

### Inventory Management
- **Add/Remove Items**: Admins can add new items to the library inventory or remove existing ones.
- **Add/Remove Item Copies**: Manage individual copies of library items, including adding new copies and removing old or damaged ones.
- **Search**: Users can search the library inventory using various criteria such as title, author, ISBN, and genre.

### Loan Management
- **Issue Loans**: Users can loan items from the library. The system tracks the loan period and availability of items.
- **Return Items**: Facilitates the return process of loaned items and updates the inventory accordingly.
- **Active Loans and Overdue Items**: Users can view their active loans and any overdue items, with admins having the capability to view all user loans.
