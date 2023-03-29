package com.bookinghotel.repository;

import com.bookinghotel.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

  //room
  @Query("SELECT m FROM Media m WHERE m.room.id = ?1 AND m.deleteFlag = false")
  Set<Media> findByRoomToSet(Long roomId);

  @Query("SELECT m FROM Media m WHERE m.room.id = ?1 AND m NOT IN ?2 AND m.deleteFlag = false")
  List<Media> findByRoomIdAndNotInMedia(Long roomId, List<Media> list);

  @Transactional
  @Modifying
  @Query("delete from Media m where m.deleteFlag = ?1")
  void deleteByDeleteFlag(boolean isDelete);

}
