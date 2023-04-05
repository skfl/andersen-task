package com.andersentask.bookshop.request.entities;

import com.andersentask.bookshop.request.entities.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book requestedBook;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

}
