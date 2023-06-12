package com.example.demo.student;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "student")

// Use Lombok annotations to generate all the class methods
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Student {

    // Generate id attribute sequence
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    // User attributes
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Integer age;
    private String password;

}
