package com.example.jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;
    @JsonView(Views.Summary.class)
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @JsonView(Views.Summary.class)
    @NotEmpty(message = "Surname cannot be empty")
    private String surname;
    @JsonView(Views.Summary.class)
    @Email(message = "Invalid email format")
    private String email;
    @JsonView(Views.Details.class)
    @OneToMany()
    private List<UserOrder> orders;
}
