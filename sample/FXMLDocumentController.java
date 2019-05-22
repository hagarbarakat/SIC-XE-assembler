/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;


import java.awt.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Pass2.Pass2;
import Pass2.ObjectCode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    @FXML
    private TabPane tabpane;
    @FXML
    private TextArea code;
    @FXML
    private RadioButton fixed, free;
    @FXML
    private ToggleGroup formats;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fixed.setSelected(true);
        code.setText(".2345678901234567890123456789");
        code.appendText("\n");
        code.setPromptText("\n" + "// TODO code application logic here");
        // code.getParent().requestFocus();
    }

    @FXML
    public void Save() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showSaveDialog(null);
        fileChooser.setTitle("Save");
        if (!selectedFile.equals(null)) {

            ObservableList<CharSequence> paragraph = code.getParagraphs();
            Iterator<CharSequence> iter = paragraph.iterator();
            try {
                BufferedWriter bf = new BufferedWriter(new FileWriter(selectedFile));
                while (iter.hasNext()) {
                    CharSequence seq = iter.next();
                    bf.append(seq);
                    bf.newLine();
                }
                bf.flush();
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public int setTextArea() {
        if ((RadioButton) formats.getSelectedToggle() == free) {
            code.setText("");
            code.setPromptText("// TODO code application logic here");
            int input = 1;
            try {
                code.setText(Files.lines(Paths.get(String.valueOf(selectedFile)))
                        .collect(Collectors.joining("\n")));
            } catch (IOException e) {
                //e.printStackTrace();
            }
            System.out.println("input" + input);
            return input;
        } else {
            System.out.println("fixed");
            code.setText(".2345678901234567890123456789");
            code.appendText("\n");
            int input = 0;
            System.out.println("input" + input);
            try {
                code.setText(Files.lines(Paths.get(String.valueOf(selectedFile)))
                        .collect(Collectors.joining("\n")));
            } catch (IOException e) {
                // e.printStackTrace();
            }
            return input;
        }

    }

    private static File selectedFile;

    @FXML
    public void Open() throws IOException {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(new Stage());
        fileChooser.setTitle("Open");
        if (!selectedFile.equals(null)) {
            code.setText(Files.lines(Paths.get(String.valueOf(selectedFile)))
                    .collect(Collectors.joining("\n")));
        }
    }

    @FXML
    public void symTable() throws IOException {

        File file = new File("src/output/SymbolTable.txt");
        Desktop.getDesktop().open(file);


    }

    @FXML
    public void run() throws IOException {
        ArrayList<String> inpcode = inputcode();

        int input = 0;
        input = setTextArea();
        System.out.println("input: " + input);
        Parser parser = new Parser();
        try {
            parser.run(inpcode, input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileReader file = new FileReader();
        ArrayList<String> symbolTable = file.readFile("src/output/SymbolTable.txt");
        ArrayList<String> intermediateFile = file.readFile("src/output/IntermediateOutputFile.txt");
        Pass2 pass2 = new Pass2(intermediateFile, symbolTable);
        Tab output = new Tab();
        TextFlow immedOutput = new TextFlow();
        Tab objFile = new Tab();
        TextFlow objCodeFile = new TextFlow();
        Tab ListFile = new Tab();
        TextFlow listFileText= new TextFlow();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    code.setText(Files.lines(Paths.get(String.valueOf(selectedFile)))
                            .collect(Collectors.joining("\n")));
                } catch (IOException e) {
                    // e.printStackTrace();
                }
                tabpane.getTabs().add(output);
                tabpane.getSelectionModel().select(output);
                output.setText("Intermediate Output");
                output.setContent(immedOutput);


                //immedOutput.setDisable(true);
                //immedOutput.setStyle(" -fx-font-family: Lucida Sans Typewriter BOLD; -fx-font-size: 14  ");
                for (int i = 0; i < Parser.getInterOutput().size(); i++) {
                    if (Parser.getInterOutput().get(i).toUpperCase().contains("ERROR")) {
                        //immedOutput.setStyle("-fx-text-fill: red;");
                        System.out.println(Parser.getInterOutput().get(i));
                        Text text = new Text(Parser.getInterOutput().get(i));
                        text.setFont(Font.font("Lucida Sans Typewriter", 14));
                        text.setStyle(" -fx-fill: red;");
                        immedOutput.getChildren().add(text);

                        System.out.println("red");
                    } else {
//                        System.out.println("green");
                        // immedOutput.setStyle("-fx-text-fill: green;");
                        Text text = new Text(Parser.getInterOutput().get(i));
                        text.setFont(Font.font("Lucida Sans Typewriter", 14));
                        immedOutput.getChildren().add(text);
                        System.out.println(Parser.getInterOutput().get(i));

                    }
                }
                tabpane.getTabs().add(objFile);
                objFile.setText("Object File");
                objFile.setContent(objCodeFile);
                for (int j = 0; j < ObjectCode.getObjectFile().size(); j++) {
                    Text text = new Text(ObjectCode.getObjectFile().get(j) + "\n");
                    text.setFont(Font.font("Lucida Sans Typewriter", 14));
                    objCodeFile.getChildren().add(text);
                    System.out.println("OBJECT   " + ObjectCode.getObjectFile().get(j));
                }
                tabpane.getTabs().add(ListFile);
                ListFile.setText("LISFILE");
                ListFile.setContent(listFileText);
                for(int k=0; k<ObjectCode.getListFile().size();k++){
                    Text text = new Text(ObjectCode.getListFile().get(k) + "\n");
                    text.setFont(Font.font("Lucida Sans Typewriter", 14));
                    listFileText.getChildren().add(text);
                    System.out.println("OBJECT   " + ObjectCode.getObjectFile().get(k));
                }
            }

        });
        listFileText.setStyle("-fx-border-color: red;-fx-background-color: white;");
        listFileText.getChildren().clear();
        objCodeFile.setStyle("-fx-border-color: red;-fx-background-color: white;");
        objCodeFile.getChildren().clear();
        immedOutput.setStyle("-fx-border-color: red;-fx-background-color: white;");
        immedOutput.getChildren().clear();
    }


    public ArrayList<String> inputcode() {
        ArrayList<String> inpcode = new ArrayList<>();
        String[] tokens = code.getText().split("\n");
        for (int i = 0; i < tokens.length; i++) {
            inpcode.add(tokens[i]);
        }
        return inpcode;
    }



}
