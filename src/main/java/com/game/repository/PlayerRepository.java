package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
    @Query("select p from Player p where p.id = ?1")
    @Override
    Optional<Player> findById(Long id);

    long count();

    Page<Player> findAll(Pageable pageable);

  //  Player save(Player player);
  // void delete(Player player);
}
