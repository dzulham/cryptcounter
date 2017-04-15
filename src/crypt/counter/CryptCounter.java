/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypt.counter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author dzulh
 */
public class CryptCounter extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Crypt.Counter v0.5");
        
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        
        TextArea plaintextField = new TextArea();
        plaintextField.setPromptText("Plaintext/Ciphertext");
        plaintextField.setWrapText(true);

        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        
        TextArea resultField = new TextArea();
        resultField.setEditable(false);
        resultField.setPromptText("Result");
        resultField.setWrapText(true);
        
        Text logText = new Text();
        logText.setFill(Color.FIREBRICK);
        
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        
        Button encBtn = new Button("Encrypt");
        encBtn.setMaxWidth(Double.MAX_VALUE);
        encBtn.setOnAction((ActionEvent event) -> {
            String log = "";
            try {
                String m = AES_CTR_PKCS5Padding.encrypt(keyField.getText(), plaintextField.getText());
                resultField.setText(m);
            } catch (Exception e) {
                log = e.getMessage();
            }
            logText.setText(log);
        });
        
        Button decBtn = new Button("Decrypt");
        decBtn.setMaxWidth(Double.MAX_VALUE);
        decBtn.setOnAction((event) -> {
            String log = "";
            try {
                String m = AES_CTR_PKCS5Padding.decrypt(keyField.getText(), plaintextField.getText());
                resultField.setText(m);
            } catch (Exception e) {
                log = e.getMessage();
            }
            logText.setText(log);
        });
        buttons.getChildren().addAll(encBtn, decBtn);
        HBox.setHgrow(encBtn, Priority.ALWAYS);
        HBox.setHgrow(decBtn, Priority.ALWAYS);
        
        root.getChildren().addAll(plaintextField, keyField, buttons, resultField, logText);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setResizable(false);
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
