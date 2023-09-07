package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    Just by using JPA's Repository you have lots of methods by default
    See : https://www.tutorialspoint.com/spring_boot_jpa/spring_boot_jpa_repository_methods.htm
 */

/*
    Not only that but by using Spring Data REST we will expose the following endpoints for free (CRRUD):

    POST    /books          Create a new book
    GET     /books          Read all the books
    GET     /book/{id}      Read one book
    PUT     /books/{id}     Update one book
    DELETE  /books/{id}     Delete one book
 */
public interface BookRepository extends JpaRepository<Book, Long> {


}
