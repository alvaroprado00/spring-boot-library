package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /*
        How you write the name of the method determines what it does. Is not the same
        findByTitleContaining thn findByTitle
     */

    Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);

    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);


    /*
         Spring works intuitively when passing just one argument, but if you want to use a list of arguments you need
         to be more specific
     */

    @Query("select o from Book o where id in :book_ids")
    List<Book> findBooksByBooksIds (@Param("book_ids") List<Long> bookIds);

}
