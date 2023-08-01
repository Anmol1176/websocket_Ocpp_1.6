package com.powerlogix.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.powerlogix.models.Websocket;

public interface WebsocketRepository extends JpaRepository<Websocket,Long>
{
	@Query("SELECT c FROM Websocket c ")
	public List<Websocket> findAll();

	@Query("SELECT u FROM Websocket u WHERE u.id = :id")
    Websocket findUserById(@Param("id") Long id);
	
    @Modifying
	@Query("UPDATE Websocket u SET u.username = :username, u.idtag = :idtag, u.expiryDate = :expiryDate, u.parentIdTag = :parentIdTag, u.status = :status WHERE u.id = :id")
    Websocket updateUser(@Param("id") Long id, @Param("username") String username, @Param("idtag") String idtag, @Param("expiryDate") String expiryDate, @Param("parentIdTag") String parentIdTag, @Param("status") String status);

    @Query("SELECT COUNT(u) > 0 FROM Websocket u WHERE u.idtag = :idtag")
	public boolean existsByIdTag(String idtag);



}
