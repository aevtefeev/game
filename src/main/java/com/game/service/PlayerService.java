package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
//@Repository
@Transactional
public class PlayerService {
   // @Qualifier("")
   // @Autowired
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }



    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }


    public Player save(Player player) {
        return null;
    }


    public void delete(Player player) {

    }



}
