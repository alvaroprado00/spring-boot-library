package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository){
        this.bookRepository=bookRepository;
        this.checkoutRepository=checkoutRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{

        Optional<Book> book=bookRepository.findById(bookId);

        Checkout checkoutExists=checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(book.isEmpty() || checkoutExists != null || book.get().getCopiesAvailable()<=0){
            throw new Exception("Book doesn't exist, there are no copies available or the checkout already exists");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());

        Checkout checkout= new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId){

       Checkout checkoutExists = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        return checkoutExists != null;
    }

    public int numberOfCheckedoutBooksByUser(String userEmail){
       return checkoutRepository.findAllByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> getLoansFromUser (String userEmail) throws Exception{

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses=new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findAllByUserEmail(userEmail).stream().toList();
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i: checkoutList){
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBooksIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books){

            Optional<Checkout> checkout = checkoutList.stream().filter(
                    x -> x.getBookId().equals(book.getId())
            ).findFirst();

            if(checkout.isPresent()){

                Date d1 =sdf.parse(checkout.get().getReturnDate());
                Date d2 =sdf.parse(LocalDate.now().toString());

                TimeUnit time =TimeUnit.DAYS;

                long difference_In_Time =time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book,(int)difference_In_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }
}
