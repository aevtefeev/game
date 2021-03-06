package com.game.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Player {

    //private long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race; //Race
    @Enumerated(EnumType.STRING)
    private Profession profession; //Profession
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel() {
        this.level = (int) ((Math.sqrt(2500 + (200 * this.getExperience())) - 50) /100);
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel() {
        this.untilNextLevel =  (50*(this.getLevel()+1)*(this.getLevel()+2)-this.getExperience());
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = new Date(birthday);
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Boolean isNull(){
        return !(this.getName() != null
                || this.getTitle() != null
                || this.getRace() != null
                || this.getProfession() != null
                || this.getBirthday() != null
                || this.getBanned() != null
                || this.getExperience() !=null);
    }

}
