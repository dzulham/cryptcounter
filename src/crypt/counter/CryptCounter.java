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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author dzulh
 */
public class CryptCounter extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Crypt.Counter v0.2");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField plaintextField = new TextField();
        plaintextField.setPromptText("Plaintext/Ciphertext");
        grid.add(plaintextField, 0, 1);

        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        grid.add(keyField, 1, 1);
        
        TextField encryptedField = new TextField();
        encryptedField.setEditable(false);
        encryptedField.setPromptText("Encrypted");
        grid.add(encryptedField, 0, 3);
        
        TextField decryptedField = new TextField();
        decryptedField.setEditable(false);
        decryptedField.setPromptText("Decrypted");
        grid.add(decryptedField, 1, 3);
        
        Text logText = new Text();
        logText.setFill(Color.FIREBRICK);
        grid.add(logText, 0, 5, 2, 2);
        
        Button encBtn = new Button("Encrypt");
        encBtn.setOnAction((event) -> {
            String log = "";
            try {
                String m = AES_CTR_PKCS5Padding.encrypt(keyField.getText(), plaintextField.getText());
                encryptedField.setText(m);
            } catch (Exception e) {
               log = e.getMessage();
            }
            logText.setText(log);
        });
        encBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(encBtn, 0, 2);
        
        Button decBtn = new Button("Decrypt");
        decBtn.setOnAction((event) -> {
            String log = "";
            try {
                String m = AES_CTR_PKCS5Padding.decrypt(keyField.getText(), plaintextField.getText());
                encryptedField.setText(m);
            } catch (Exception e) {
               log = e.getMessage();
            }
            logText.setText(log);
        });
        decBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(decBtn, 1, 2);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        AES_CTR_PKCS5Padding.init();
        launch(args);
    }
    
}
