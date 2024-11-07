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
public class UserModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ID;
  // todo:spring security may require refactoring of userID
  private String username;

  private String password;

}