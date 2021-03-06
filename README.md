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
* Login - Any user that has already created an account can login using their library card number and password assocaited to their account. 
  * Their credentials will be authenticate once they have been submitted to the system. 
    * If the credentials are validated, then the user will be able to login to the system. 
    * If the credentials are invalidated, then a message will be sent that either the wrong library card number of password was inputted.
* View Account - Any user will be able to view their personal details associated to their account.
* Edit Account/Change Password - Any user will be able to edit their account's personal details and change their password.

### Catalog
The catalog contains all the books within the library management system, including libraries, authors, and subjects that can be associated to books.

* Libraries - Contains the books within the system. Books cannot be added to the system unless they are within a library.
  * Each library will have their unique name and an address of the location of the library.
  * Each library can be empty or contain many books, with each book being place on a rack. 
    * The rack is labelled with a number and a location identifer.
* Authors - Contains the writers of the books within the system.
  * Each author will have a unique name and an optional description.
  * Each author can have no books or be writers of many books within the system.
* Subjects - Contains the genres that can be labelled onto books
  * Each book can have one or many subjects labelled. 
* Search - Any user can use the catalog to search through books based on
   * Library
   * Title 
   * Author
   * Subject
   * Publication Date 

### Librarians
Librarians are responsible for adding, modifying, and removing objects within the system.

* View all Members/Librarians - View all user accounts, both members and librarians, within the system.
* Add/Remove Librarians - Optionally add or remove librarians from the system.
* Block/Unblock Members - Librarians can block members to prevent them from doing any actions within the system. They can also unblock those members to allow them to proceed with any action within the system.
* View all Book Loans/Reservations - View all records of book loans and reservations made within the system by members. This will contain information of the date of when the action was made and the what book a member has borrowed or reserverd.
* Add/Remove Library - Librarians can add/remove libraries to the system which will contain the books. Libraries cannot only be removed if they are empty, in other words have no books associated to them.
* Add/Modify/Remove Book - Librarians can add, modify, and remove books within the system.
  * When modifying books, not only can its properties be changed but new subjects can be added and a new author can be associated to the book.
  * Another way of modifying the book is by moving the book to another library and rack within the system. The library it is being moved to has to exist within the system in order to modify the books location.
  * Removing books will remove the association it has with the library, subjects, and author within its properties. The book will no longer be listed under the previously associated library, subjects, and author when viewed in the catalog.
* Add/Remove Subjects - Subjects can be added and removed within the system. Multiple subjects can be associated to multiple books within the system.
* Add/Modify/Remove Authors - Authors can be added and removed within the system. Each author can have multiple books associated to them within the system. Optionally, authors can be modified to add or change their current description.

### Members
Members will primarily use their accounts to borrow, reserve, renew, and return books. Their accounts also have a limited amount of books that can be issued to their account through both book loans and reservations, and must return any borrowed books within the due date, otherwise they will have to pay a fine.

* View Book Loans/Reservations - View the current borrowed and reserved books in a member's account.
* View Book Loans/Reservations Records - View the member's history of book loans and reservations made in their account.
* View Fines/Transactions - View fines issued to a member due to late book returns and transactions made to pay fines.
* View Notifications - View notifcations on a member's account that were sent after either a book checkout, reservations, renewal, return, etc.
* Checkout Book - Borrow a selected book from the system. The book cannot be borrowed if
  * The book is a reference only
  * The book is currently loeaned to another member
  * The book is currently reserved for another member   
* Reserve Book - Reserve a selected book from the system. This can be done even a book is currently loaned to a member and only one member can reserve a book at a time.
* Cancel Reservation - Cancel a reserved book from the member's account. 
* Renew Book - Renew a borrowed book's due date from the member's account.
  * The book must be renewed before the due date in order to renew it. If a book is returned late, than the book will be returned to the system and the member is issued a fine.
  * The book cannot be renewed if another member has reserved the book and will be returned to the system.
* Return Book - Return a borrow book from the member's account.
  * If a book is return late, a fine is issued to the member and the member must pay depending on how many days the book has been returned late.
* Pay Fine - Pay for a fine in a member's account with either the 3 types of transactions
  * Credit Card
  * Check
  * Cash 
* Cancel Membership - Cancels a member's account and library card within the system.
  * Members must input their library card number and password credentials and must be validated before they can cancel their membership.
  * Members cannot cancel their account if they have books still issues to them, either through checkouts or reservations, and must return thos books or cancel reservations before they can cancel their membership.
  * Members cannot cancel their account if thye still have fines that are not paid yet and must all be paid before they can cancel their membership.
