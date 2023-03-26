package com.bookinghotel.entity;

import com.bookinghotel.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends DateAuditing {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(insertable = false, updatable = false, nullable = false, columnDefinition = "CHAR(36)")
  private String id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Nationalized
  @Column(nullable = false)
  private String firstName;

  @Nationalized
  @Column(nullable = false)
  private String lastName;

  @Nationalized
  @Column(nullable = false)
  private String gender;

  @Column(nullable = false)
  private LocalDate birthday;

  @Nationalized
  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Boolean enabled;

  //Link to table Role
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"))
  private Role role;

  //Link to table Booking
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<Booking> bookings = new HashSet<>();

  //Link to table Post
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<Post> posts = new HashSet<>();

  //Link to table RoomRating
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
  @JsonIgnore
  private Set<RoomRating> roomRatings = new HashSet<>();

  @PrePersist
  public void prePersist() {
    if (this.enabled == null) {
      this.enabled = Boolean.FALSE;
    }
  }

  public User(String email, String phoneNumber, String password, String firstName, String lastName,
              String gender, LocalDate birthday, String address, Boolean enabled, Role role) {
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthday = birthday;
    this.address = address;
    this.enabled = enabled;
    this.role = role;
  }

}
