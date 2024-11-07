package com.saleshalal.SEProject.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@Entity
public class UserModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userID;

  private String email;

  private String password;

}