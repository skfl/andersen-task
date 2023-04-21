![logo](https://static.andersenlab.com/andersenlab/new-andersensite/logo-social.png)

# Andersen Traineeship Project

# Bookstore

## Task 1

Implement bookstore console application according next requirements and functionality:

* All possible book should be known beforehand. No need to change it.
* List books sorted by name, price, availability
* Leave a request for a book
* List requests sorted by book, total number
* Change the book status to be “out of stock”
* Create an order. If the book is “out of stock”, a request for the book should be created
* Cancel an order
* Complete an order
* List orders sorted by price, completion date (period), status
* Get full information about an order: books, client
* Get the income for a certain period
* Change the book status to be “in stock”. This should also close all requests for this book.

## Task 2

* Write tests for your existing solution. Optionally, add some integration tests.
* Configure a CI pipeline for your repository.

## Task 3

* Replace loops with Stream API where it makes sense
* Serialize/deserialize the state of the application and store it on disk.
* Add a configuration using external file:
    * Turn on/off the ability to create a request for a book when creating an order for “out of stock” book

## Task 4

* Migrate your application from console interface to web interface using Servlet API and Servlet container (e.g., Apache
  Tomcat)
* Try to follow ReST guidelines
* Do not use Spring Framework yet

## Task 5

* Use a proper relational database to store application state (e.g., PostgreSQL, MySQL). You can even decide to use H2
* To access database use JDBC.
* To manage connections to database try to use a connection pool (e.g., Hikari)
* For schema migration look at these tools: Liquibase, Flyway. But you can use even plain SQL files.

## Task 6

* To access database use JPA and Hibernate.

### Technical stack: Jetty, Liquibase, PostgreSQL, HikariCP, JUnit, Mockito, Jackson, Logback, Lombok, Maven

### This project was created during Andersen java course

## Tags

Git tags used to mark different stages in projects' lifecycle. You can use them to quickly navigate to and inspect the
state.

* v0.1.0 - console application (Task 1 - 3)

## Contributors

* <a href="https://github.com/DenisBratuh/" style="text-decoration: none;color: white"> Bratukh Denys </a>
* <a href="https://github.com/antonklyuchnikov1990" style="text-decoration: none;color: white"> Kliuchnikau Anton </a>
* <a href="https://github.com/skfl" style="text-decoration: none;color: white"> Rogachkov Eduard </a>
* <a href="https://github.com/ulad-sachkovski" style="text-decoration: none;color: white">Sachkouski Uladzislau    </a>
