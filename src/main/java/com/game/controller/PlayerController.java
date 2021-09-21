package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(path = "/rest/players")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    // Проверяем кооректность данных при создании игрока
    public boolean checkPlayer(Player player){
        // Опыт меньше 0
        return !player.getName().isEmpty()                              // Пустое имя
                && (player.getName().length() <= 12)                   // Длинна имени
                && ((player.getBirthday().getTime()) >= 0)          // Дата рождения меньше нуля
                && ((!player.getTitle().isEmpty()))                  // Пустое описание
                && (player.getTitle().length() <= 30)                  // Длинна описания
                && (!player.getRace().toString().isEmpty())          // Пустая расса
                && (!player.getProfession().toString().isEmpty())    // Пустая профессия
                && (player.getExperience() <= 10000000)                // Опыт больше 10000000
                && (player.getExperience() >= 0);
    }



    // Получаем всех игроков
    @GetMapping(path = "")
    public @ResponseBody
    ResponseEntity<List<Player>> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) String race,
            @RequestParam(value = "profession", required = false) String profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "pageSize",defaultValue = "3") Integer pageSize,
            @RequestParam(value = "pageNumber",defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "order", defaultValue = "id") String order) {

        Race race1 = (race != null) ?  Race.valueOf(race) : null;
        Profession profession1 = (profession != null) ? Profession.valueOf(profession) : null;
        Date beforeDate = (before != null) ? new Date(before) : null;
        Date afterDate = (after != null) ? new Date(after) : null;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order).ascending());
        List<Player> result = playerRepository.findAllBy(
                name, title, race1, profession1, afterDate, beforeDate ,banned, minExperience, maxExperience, minLevel, maxLevel,
                pageable).getContent();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // Поиск игрока по id ОК
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
    Long getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) String race,
            @RequestParam(value = "profession", required = false) String profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        Race race1 = (race != null) ?  Race.valueOf(race) : null;
        Profession profession1 = (profession != null) ? Profession.valueOf(profession) : null;
        Date beforeDate = (before != null) ? new Date(before) : null;
        Date afterDate = (after != null) ? new Date(after) : null;

        return  playerRepository.count(name, title, race1, profession1, afterDate, beforeDate ,banned, minExperience,
                maxExperience, minLevel, maxLevel);
        // Добавить фильтрацию для корректного отображения колличества записей при фильтрации
    }

    // Создание игрока ОК
    @PostMapping(path = "")
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        try {
            if (player !=null && checkPlayer(player)) {
                player.setLevel();
                player.setUntilNextLevel();
                final Player result = playerRepository.save(player);
                return  new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // Обновление игрока ОК
    @PostMapping(path = "{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable(name = "id") Long id, @RequestBody Player player){
        if (id<=0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (!playerRepository.findById(id).isPresent()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        if (!player.isNull()) {
             try {
                 Player player1 = playerRepository.findById(id).get();
                 if (player.getName() != null) {player1.setName(player.getName());}
                 if (player.getTitle() != null) {player1.setTitle(player.getTitle());}
                 if (player.getRace() != null) {player1.setRace(player.getRace());}
                 if (player.getProfession() !=null) {player1.setProfession(player.getProfession());}
                 if (player.getBirthday() != null) {
                     if (player.getBirthday().getYear()>=100 && player.getBirthday().getYear()<=200) {
                         player1.setBirthday(player.getBirthday().getTime());
                     } else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                     }
                 }
                 if (player.getBanned() != null) {player1.setBanned(player.getBanned());}
                 if (player.getExperience() != null) {
                     Integer exp = player.getExperience();
                     if (exp>0) {
                         if (exp<10000000){
                             player1.setExperience(player.getExperience());
                         } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                     }
                     else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                 }
                 // Обновляем уровень и сколько до следующего уровня
                 player1.setLevel();
                 player1.setUntilNextLevel();

                 final Player result = playerRepository.save(player1);

                 return new ResponseEntity<>(result, HttpStatus.OK);

             } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
             }
        } else {
            return new ResponseEntity<>(playerRepository.save(playerRepository.findById(id).get()), HttpStatus.OK);
        }
    }

    // Удаление игрока ОК
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