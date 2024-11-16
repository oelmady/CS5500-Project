package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import lombok.*;

// Todo: implement entity using @table and @Column
@EqualsAndHashCode
@ToString
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    // todo:spring security may require refactoring of userID
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
