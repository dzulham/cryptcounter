package crypt.counter;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.crypto.KeyGenerator;

/**
 *
 * @author dzulh
 */
public class CryptCounter extends Application {
    
    private static final int BUTTON_WIDTH = 60;
    private static final int APP_PADDING = 10;
    private static final int H_SPACING = 5;
    private static final int V_SPACING = 10;
    
    private static File inputFile = null;
    private static File outputFile = null;
    private static TextField keyField = null;
    
    private static TextArea logs = null;
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Crypt.Counter v0.8");
        
        VBox base = (VBox) createBase();
        
        //-----< File Management Section -----
        Node inputFileSection = createInputFileSection(primaryStage);
        Node outputFileSection = createOutputFileSection(primaryStage);
        VBox fileSectionBox = new VBox(inputFileSection, outputFileSection);
        fileSectionBox.setSpacing(V_SPACING);
        TitledPane fileSectionPane = new TitledPane("File Management", fileSectionBox);
        fileSectionPane.setCollapsible(false);
        //----- File Management Section >-----
        
        //-----< Key and IV Input Section -----
        Node keyInputSection = createKeyInputSection(primaryStage);
        Node ivInputSection = createIVInputSection();
        VBox inputSectionBox = new VBox(keyInputSection, ivInputSection);
        inputSectionBox.setSpacing(V_SPACING);
        TitledPane inputSectionPane = new TitledPane("Input Generator", inputSectionBox);
        inputSectionPane.setCollapsible(false);
        //----- Key and IV Input Section >-----
        
        //-----< Action Buttons Section -----
        Node buttonsSection = createButtonsSection();
        //----- Action Buttons Section >-----
        
        //-----< Feedback Section -----
        Node feedbackSection = createFeedbackSection();
        TitledPane feedbackSectionPane = new TitledPane("Log", feedbackSection);
        //----- Feedback Section >-----
        
        base.getChildren().addAll(fileSectionPane, inputSectionPane, buttonsSection, feedbackSectionPane);
        
        Scene scene = new Scene(base, 450, 450);
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
    
    public static Parent createBase() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(APP_PADDING));
        vbox.setSpacing(V_SPACING);
        return vbox;
    }
    
    public static FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        return fileChooser;
    }
    
    public static byte[] readFile(File file) throws IOException {
        byte[] array = Files.readAllBytes(file.toPath());
        return array;
    }
    
    public static byte[] stringToHex(String hexString) throws IOException {
        byte[] array = new BigInteger(hexString, 16).toByteArray();
        return array;
    }
    
    public static Node createInputFileSection(Stage stage) {
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        TextField filepathField = new TextField();
        filepathField.setPromptText("Choose file for input...");
        filepathField.setFocusTraversable(false);
        HBox.setHgrow(filepathField, Priority.ALWAYS);
        Button openButton = new Button("Input");
        openButton.setMinWidth(BUTTON_WIDTH);
        openButton.setOnAction((event) -> {
            FileChooser fileChooser = getFileChooser();
            inputFile = fileChooser.showOpenDialog(stage);
            filepathField.setText(inputFile.getAbsolutePath());
        });
        hbox.getChildren().addAll(filepathField, openButton);
        return hbox;
    }
    
    public static Node createOutputFileSection(Stage stage) {
        HBox hbox = new HBox();
        hbox.setSpacing(H_SPACING);
        TextField filepathField = new TextField();
        filepathField.setPromptText("Choose file for output...");
        HBox.setHgrow(filepathField, Priority.ALWAYS);
        Button openButton = new Button("Output");
        openButton.setMinWidth(BUTTON_WIDTH);
        openButton.setOnAction((event) -> {
            FileChooser fileChooser = getFileChooser();
            String filename = inputFile == null? "output" : inputFile.getName();
            fileChooser.setInitialFileName(filename + ".crypt");
            outputFile = fileChooser.showSaveDialog(stage);
            filepathField.setText(outputFile.getAbsolutePath());
        });
        hbox.getChildren().addAll(filepathField, openButton);
        return hbox;
    }
    
    public static Node createKeyInputSection(Stage stage) {
        HBox hbox = new HBox();
        hbox.setSpacing(H_SPACING);
        keyField = new TextField("128 bit");
        keyField.setPromptText("Input key here...");
        HBox.setHgrow(keyField, Priority.ALWAYS);
        
        Button openKeyButton = new Button("...");
        openKeyButton.setOnAction((event) -> {
            FileChooser fileChooser = getFileChooser();
            File file = fileChooser.showOpenDialog(stage);
            try {
                keyField.setText(Files.readAllLines(file.toPath()).get(0));
            } catch (IOException ex) {
                Logger.getLogger(CryptCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        hbox.getChildren().addAll(keyField, openKeyButton);
        return hbox;
    }
    
    public static Node createIVInputSection() {
        HBox hbox = new HBox();
        hbox.setSpacing(H_SPACING);
        TextField ivField = new TextField(Arrays.toString(AES_CTR_PKCS5Padding.IV));
        ivField.setEditable(false);
        HBox.setHgrow(ivField, Priority.ALWAYS);
        Button genButton = new Button("Generate IV");
        genButton.setOnAction((event) -> {
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                AES_CTR_PKCS5Padding.init(keyGen.generateKey().getEncoded());
            } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                Logger.getLogger(CryptCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
            ivField.setText(Arrays.toString(AES_CTR_PKCS5Padding.IV));
        });
        
        hbox.getChildren().addAll(ivField, genButton);
        return hbox;
    }
    
    public static Node createButtonsSection() {
        HBox hbox = new HBox();
        hbox.setSpacing(H_SPACING);
        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction((event) -> {
            try {
                byte[] value = readFile(inputFile);
                byte[] key = stringToHex(keyField.getText());
                byte[] result = AES_CTR_PKCS5Padding.encrypt(key, value);
                Files.write(outputFile.toPath(), result, StandardOpenOption.CREATE);
                logs.appendText("Encryption succeed\n");
            } catch (Exception ex) {
                if(ex != null)
                    logs.appendText(ex.getMessage());
                logs.appendText("Encryption failed\n");
            }
        });
        Button decryptButton = new Button("Decrypt");
        decryptButton.setOnAction((event) -> {
            try {
                byte[] value = readFile(inputFile);
                byte[] key = stringToHex(keyField.getText());
                byte[] result = AES_CTR_PKCS5Padding.decrypt(key, value);
                Files.write(outputFile.toPath(), result, StandardOpenOption.CREATE);
                logs.appendText("Decrytion succeed\n");
            } catch (Exception ex) {
                Logger.getLogger(CryptCounter.class.getName()).log(Level.SEVERE, null, ex);
                logs.appendText("Decryption failed\n");
            }
        });
        encryptButton.setMaxWidth(Double.MAX_VALUE);
        decryptButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(encryptButton, Priority.ALWAYS);
        HBox.setHgrow(decryptButton, Priority.ALWAYS);
        hbox.getChildren().addAll(encryptButton, decryptButton);
        return hbox;
    }
    
    public static Node createFeedbackSection() {
        TextArea feedbackArea = new TextArea("-- Crypt.Counter ver 0.8\n");
        feedbackArea.setEditable(false);
        CryptCounter.logs = feedbackArea;
        return feedbackArea;
    }
}
