/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.CartoonsCatalogServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author guidi
 */
@Controller
@RequestMapping(path="/607453")
public class MainController {
    @Autowired
    private CharacterRepository characterRepository;
    
    @GetMapping(path="/caricadati")
    public @ResponseBody long loadCharacters() throws IOException {
        
        URL url = new URL("https://rickandmortyapi.com/api/character");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        
        String inputLine;
        StringBuffer content = new StringBuffer();
        
        while((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }
        in.close();
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonObject rootObject = json.getAsJsonObject();
        JsonArray characters = rootObject.get("results").getAsJsonArray();

        for (int i = 0; i < characters.size(); i++) {
            JsonObject d = characters.get(i).getAsJsonObject();
            // inserimento dati dei characters nel database
            Character c = new Character(d.get("id").getAsInt(), d.get("name").getAsString(), d.get("status").getAsString(),
                                        d.get("species").getAsString(), d.get("gender").getAsString(),
                                        d.get("origin").getAsJsonObject().get("name").getAsString(), d.get("image").getAsString());
            characterRepository.save(c);
        }
        
        // ritorna il numero dei personaggi caricati nel database
        return characterRepository.count();
    }
        
    @GetMapping(path="/tutti")
    public @ResponseBody Iterable<Character> getAllCharacters(){
        return characterRepository.findAll();
    }
            
    @PostMapping(path="/rimuovi")
    public @ResponseBody String removeCharacterById(@RequestParam Integer id){
        characterRepository.deleteById(id);
        return "Rimosso dal database personaggio id=" + id;
    }
}
