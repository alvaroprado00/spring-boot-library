package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.ReviewRepository;
import com.luv2code.springbootlibrary.entity.Review;
import com.luv2code.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository=reviewRepository;

    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception{
        Review validateReview= reviewRepository.findReviewByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if(validateReview!=null){
            throw new Exception("Review already created");
        }
        Review review = new Review();
        review.setUserEmail(userEmail);
        review.setRating(reviewRequest.getRating());
        review.setBookId(reviewRequest.getBookId());
        if(reviewRequest.getReviewDescription().isPresent()){
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    Object::toString
            ).orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));

        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long bookId){
        Review validateReview= reviewRepository.findReviewByUserEmailAndBookId(userEmail, bookId);
        return validateReview != null;
    }
}
