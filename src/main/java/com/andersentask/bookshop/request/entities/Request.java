package com.andersentask.bookshop.request.entities;

import com.andersentask.bookshop.request.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Book> requestedBooks;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

}
