package com.example.jsonview.model;

import com.example.jsonview.util.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long amount;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany
    private List<Product> products;
}
