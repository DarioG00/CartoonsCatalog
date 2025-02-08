package it.unipi.cartoonscatalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrimaryController {
    
    private static final Logger logger = LogManager.getLogger(PrimaryController.class);
    
    @FXML
    ImageView logo;
    
    @FXML
    Button loadButton;
    
    @FXML
    void initialize(){
        logo.setImage(new Image(getClass().getResource("img/RickAndMortyLogo.png").toExternalForm()));
    }

    @FXML
    private void switchToSecondaryAndLoad() throws IOException {
        
        Task task = new Task<Void>(){
            @Override
            public Void call() throws IOException {
        
                // richiesta al server di caricare i dati
                URL url = new URL("http://127.0.0.1:8080/607453/caricadati");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer content = new StringBuffer();

                while((inputLine = in.readLine()) != null){
                    content.append(inputLine);
                }
                in.close();
                
                logger.info("Ritorno della richiesta = " + content.toString());

                int result = Integer.parseInt(content.toString());

                if(result > 0){
                    logger.info("Catalogo personaggi 'Rick and Morty' caricato.");
                    App.setRoot("secondary");
                }else{
                    // gestisci errore caricamento
                    logger.info("Errore nel caricamento dei dati.");
                }
        
                return null;
            }
        };
        
        new Thread(task).start();
    }
}
