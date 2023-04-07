package com.bookinghotel.entity;

import com.bookinghotel.entity.common.FlagUserDateAuditing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "medias")
public class Media extends FlagUserDateAuditing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String url;

  //Link to table Post
  @ManyToOne
  @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_MEDIA_POST"))
  private Post post;

  //Link to table Room
  @ManyToOne
  @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "FK_MEDIA_ROOM"))
  private Room room;

}
