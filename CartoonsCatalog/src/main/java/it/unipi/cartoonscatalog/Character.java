/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.cartoonscatalog;

/**
 *
 * @author guidi
 */
public class Character {
    
    private Integer id;
    private String name;
    private String status;
    private String species;
    private String gender;
    private String origin;
    private String imageURL;
    
    public Character(Integer id, String name, String status, String species, String gender, String origin, String imageURL) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = gender;
        this.origin = origin;
        this.imageURL = imageURL;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public String getOrigin() {
        return origin;
    }

    public String getImageURL() {
        return imageURL;
    }

}
