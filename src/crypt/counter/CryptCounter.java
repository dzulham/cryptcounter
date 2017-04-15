/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypt.counter;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author dzulh
 */
public class CryptCounter extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Crypt.Counter v0.1");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField plaintextField = new TextField();
        plaintextField.setPromptText("Plaintext");
        grid.add(plaintextField, 0, 1);

        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        grid.add(keyField, 1, 1);
        
        TextField encryptedField = new TextField();
        encryptedField.setDisable(true);
        encryptedField.setPromptText("Encrypted");
        grid.add(encryptedField, 0, 3, 2, 1);
        
        Button encBtn = new Button("Encrypt");
        encBtn.setOnAction((ActionEvent e) -> {
            try {
                AES_CTR_PKCS5Padding ACP = new AES_CTR_PKCS5Padding();
                String encrypted = ACP.encrypt(plaintextField.getText(), keyField.getText());
                encryptedField.setText(encrypted);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                Logger.getLogger(CryptCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Button genBtn = new Button("Generate");
        grid.add(encBtn, 0, 2);
        grid.add(genBtn, 1, 2);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
