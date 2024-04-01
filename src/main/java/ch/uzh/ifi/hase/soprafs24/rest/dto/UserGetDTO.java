package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class UserGetDTO {

  private Long id;
  private String username;
  private UserStatus status;
  private String password;
  private String token;
  private LocalDateTime creation_date;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getPassword(){
    return password;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public LocalDateTime getCreation_date(){
    return creation_date;
  }
  public void setCreation_date(LocalDateTime creation_date){
    this.creation_date = creation_date;
  }
}

