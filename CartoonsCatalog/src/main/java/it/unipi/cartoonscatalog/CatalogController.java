/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.cartoonscatalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author guidi
 */
public class CatalogController {
    private static final Logger logger = LogManager.getLogger(CatalogController.class);
    
    private int characters;
    private int currentCharacter;
    
    private List<Character> cl = new ArrayList<Character>();
    
    @FXML Button backButton;
    @FXML Button nextButton;
    @FXML Button previousButton;
    
    @FXML Text nameText;
    @FXML Text statusText;
    @FXML Text speciesText;
    @FXML Text genderText;
    @FXML Text originText;
    
    @FXML ImageView imageCharacter;
    
    @FXML
    void initialize(){
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException{
                
                // Richiesta dei personaggi presenti nel database
                URL url = new URL("http://127.0.0.1:8080/607453/tutti");
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
                JsonArray cs = json.getAsJsonArray();
                
                characters = cs.size();
                
                for (int i = 0; i < characters; i++) {

                    JsonObject d = cs.get(i).getAsJsonObject();

                    Character c = new Character(d.get("id").getAsInt(), d.get("name").getAsString(), d.get("status").getAsString(),
                                                d.get("species").getAsString(), d.get("gender").getAsString(),
                                                d.get("origin").getAsString(), d.get("imageURL").getAsString());

                    cl.add(c);
                }
                
                // Visualizzazione del primo personaggio
                currentCharacter = -1; // Inizializzazione di partenza, il primo elemento è 0
                goToNextCharacter();
                
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    private void loadCharacter(Character c) throws IOException{
        URL url = new URL(c.getImageURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        imageCharacter.setImage(new Image(con.getInputStream()));

        nameText.setText("Name: " + c.getName());
        statusText.setText("Status: " + c.getStatus());
        speciesText.setText("Species: " + c.getSpecies());
        genderText.setText("Gender: " + c.getGender());
        originText.setText("Origin: " + c.getOrigin());
    }
    
    @FXML
    private void goToPreviousCharacter(){
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException{
                currentCharacter--;
                
                if(currentCharacter <= 0){
                    currentCharacter = 0;
                    previousButton.setDisable(true);
                }else{
                    previousButton.setDisable(false);
                    nextButton.setDisable(false);
                }

                loadCharacter(cl.get(currentCharacter));

                return null;
            }
        };
        
        new Thread(task).start();
    }
            
    @FXML
    private void goToNextCharacter(){
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException{
        
                currentCharacter++;

                if(currentCharacter >= (characters-1)){
                    currentCharacter = characters-1;
                    nextButton.setDisable(true);
                }else{
                    previousButton.setDisable(false);
                    nextButton.setDisable(false);
                }
                
                if(currentCharacter <= 0)
                    previousButton.setDisable(true);
                    
                loadCharacter(cl.get(currentCharacter));

                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    @FXML
    private void backToSecondary() throws IOException {
        
        logger.info("Bottone di ritorno alla finestra secondary premuto!");
        App.setRoot("secondary");
    }
}
