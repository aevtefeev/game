package com.game.controller;


import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(path = "/rest/players")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    // Получаем всех игроков
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<List<Player>> getAllPlayers(@RequestParam(value = "pageSize",defaultValue = "0") Integer pageSize,
                               @RequestParam(value = "pageNumber",defaultValue = "3") Integer pageNumber,
                               @RequestParam(value = "order", defaultValue = "id") String order) {
        PlayerOrder playerOrder = PlayerOrder.valueOf(order);
        List<Player> result = playerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(playerOrder.getFieldName()).ascending())).getContent();
        return new ResponseEntity<>(result, HttpStatus.OK);
        //  Добавить фильтрацию тут
    }


    // Поиск игрока по id
    @GetMapping(path = "{id}")
    public @ResponseBody
    ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        if (id>0) {
            if (playerRepository.findById(id).isPresent()){
                final Player result = playerRepository.findById(id).get();
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else if (id<=0){ //BAD_REQUEST
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Получаем колличество записей
    @GetMapping(path = "/count")
    public @ResponseBody
    Long getPlayersCount() {
        return  playerRepository.count();
        // Добавить фильтрацию для корректного отображения колличества записей при фильтрации
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        playerRepository.save(player);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id > 0) {
            if (playerRepository.findById(id).isPresent()) {
                playerRepository.deleteById(id);
                return ResponseEntity.ok().body(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}