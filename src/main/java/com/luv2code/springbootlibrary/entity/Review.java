package com.luv2code.springbootlibrary.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/*
    @Data belongs to the lombok project which automatically builds the getters/setters/constructor
    The rest of @ are part of javax persistence JPA
 */
@Entity
@Table(name="review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="date")
    private Date date;

    @Column(name="rating")
    private double rating;

    @Column(name="book_id")
    private Long bookId;

    @Column(name="review_description")
    private String reviewDescription;

}
