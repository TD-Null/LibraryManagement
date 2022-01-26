# Library Management System Project
This project consists of a software implementation of a library management system, built to simulate the main functions of a library. This systems helps keep in track of book checkouts, reservations, renewals, and returns, as well as members' profiles and subscriptions to the system. Librarians will be responsible for adding, removing, and modifying libraries and books to the system as well as managing the records of book loans and reservations.

## System Requirements
The Library Management System will keep these rules in mind when considering its software implementation:
1. Any user with an account and a library card witin the system will be able to search for books based on optional parameters:
   * Library
   * Title 
   * Author
   * Subject
   * Publication Date 
2. Members will also have the option to reserve books that are currently not available. Only one member can be able to reserve a book at a time.
3. Each book will have unique identification, as well as other properties, such as the title, publisher, language etc. There will also be established relationship between the book and other components, such as its library, author, and multiple subjects.
4. The system can contain copies of a book, and members are able to borrow or reserve any of these copies. The copies of a book will be referred as book items within the software implementation.
5. The system will record information of which members have currently either checked-out or reserved certain book items as well have a record of the system's checkout and reservation history.
6. Members will have limits regarding when they check-out books from system:
   * There should be a maximum limit (5) on how many books are issued to a member, including both checkouts and reservations.
   * There should be a maximum limit (10) on how many days a member can keep a book.
7. If a member returns a book past its due date, a fine will be issue to them and they must pay for those fines. A member can pay these fines with the following transactions:
   * Credit card
   * Check
   * Cash
8. The system will send notifications to a member regarding book checkouts, reservations, and renewals.
9. Each book and member's library card will have a unique barcode. The system will used these barcodse during checkouts, reservations, and renewals.
10. Librarians will be essential in adding, modifying, and removing libraries and book items to the system, as well manage the current checkouts and reservations made by members.

## Main Components
These will be the main components used for the software implementation of the library management system.

### Accounts
Each user registered into the system will have a single account associated to them. These users could either be librarians or members, each with their own different authorizations to certain requests. Each user will also have a single library card associated to their account that will be given to them once they have registered an account in the system.

* Signup/Register - Any new user, both a librarian and member, will be able to register an account to system. 
  * Each account upon successful registration will be given a library card with a random 6-digit card number and a label of either a librarian or member to that card depending on what type of account is being created. 
  * When a user starts creating their account, they will need to input their
    * Name
    * Password
    * Email Address
    * Home Address
    * Phone Number
  * Each account must have a unique email address, otherwise the user will not be able to register their account to the system. This is to avoid account duplication by the same user.
* Login - Any user that has already created an account can login using their library card number and password assocaited to their account. After inputting their credentials, the input will be authenticated. If the right credentials were inputted, then the user will be able to login to the system. If invalid credentials were inputted, then a message will be sent that either the wrong library card number of password was inputted.
* View Account - Any user will be able to view their personal details they 

### Catalog


### Librarians


### Members

