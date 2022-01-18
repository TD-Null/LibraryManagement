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

### Accounts


### Catalog


### Librarians


### Members

