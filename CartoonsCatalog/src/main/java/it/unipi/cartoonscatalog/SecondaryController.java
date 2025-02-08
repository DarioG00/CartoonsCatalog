package it.unipi.cartoonscatalog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecondaryController {
    
    private static final Logger logger = LogManager.getLogger(SecondaryController.class);
    
    @FXML TableView<Character> charactersTable = new TableView<>();
    
    private ObservableList<Character> ol;
    
    @FXML
    private Button browseButton;
    
    @FXML
    private ContextMenu cm;
    
    @FXML
    private MenuItem menuitem1;
    
    @FXML
    private MenuItem menuitem2;
    
    @FXML
    ImageView logo;

    @FXML
    public void initialize() {
        
        logo.setImage(new Image(getClass().getResource("img/RickAndMortyLogo.png").toExternalForm()));
        
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException{
                                
                // Preparazione tabella (ID, Name)
                TableColumn idCol = new TableColumn("ID");
                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                
                TableColumn nameCol = new TableColumn("Name");
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                
                charactersTable.getColumns().addAll(idCol, nameCol);
                
                ol = FXCollections.observableArrayList();
                charactersTable.setItems(ol);

                // Richiesta della lista dei personaggi
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
                JsonArray characters = json.getAsJsonArray();
                
                logger.info("Ricevuta la risposta dal server. Inizio a caricare la tabella...");
                
                logger.info("Dimensione dati tornati per la tabella = " + characters.size());
                
                for (int i = 0; i < characters.size(); i++) {
                    
                    JsonObject d = characters.get(i).getAsJsonObject();
                    
                    Character c = new Character(d.get("id").getAsInt(), d.get("name").getAsString(), d.get("status").getAsString(),
                                                d.get("species").getAsString(), d.get("gender").getAsString(),
                                                d.get("origin").getAsString(), d.get("imageURL").getAsString());
                    
                    ol.add(c);
                }
                
                logger.info("Caricati tutti i personaggi");
                return null;
            }
        };
        
        new Thread(task).start();    
    }
    
    @FXML
    public void remove(){
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException {
                
                Character characterSelected = charactersTable.getSelectionModel().getSelectedItem();
                
                URL url = new URL("http://127.0.0.1:8080/607453/rimuovi?id=" + characterSelected.getId().toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                
                ol.remove(characterSelected);
                                
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                
                String inputLine;
                StringBuffer content = new StringBuffer();

                while((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();

                logger.info(content.toString());
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    @FXML
    public void switchToCatalog() throws IOException {
        App.setRoot("catalog");
    }
}