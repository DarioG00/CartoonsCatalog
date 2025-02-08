/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.CartoonsCatalogServer;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author guidi
 */
public interface CharacterRepository extends CrudRepository<Character, Integer>{
    void deleteById(Integer id);
    long count();
}
