package com;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    
    public static void start(Stage stage) {

        try {

            // set title for the stage
            stage.setTitle("FileChooser");

            // create a File chooser
            FileChooser fil_chooser = new FileChooser();

            // create a Label
            Label label = new Label("no files selected");

            // create a Button
            Button button = new Button("Choose file MD5 hash");

            // create an Event Handler
            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e) {
                            // get the file selected
                            File file = fil_chooser.showOpenDialog(stage);

                            if (file != null) {
                                connectWithServer(file);
                                String MD5 = DigestUtils.md5Hex(file.toString());

                                System.out.println("MD5 hash = " + MD5);

                                label.setText(file.getAbsolutePath()
                                        + "  selected" + "\n"+
                                        "MD5 hash is " + MD5);
                            }
                        }
                    };

            button.setOnAction(event);

            // create a Button
            Button button1 = new Button("Choose file for SHA256 hash");

            // create an Event Handler
            EventHandler<ActionEvent> event1 =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            // get the file selected
                            File file = fil_chooser.showOpenDialog(stage);

                            if (file != null) {
                                connectWithServer(file);
                                String SHA256 = DigestUtils.sha256Hex(file.toString());

                                label.setText(file.getAbsolutePath()
                                        + "  selected"+ "\n"+"\n"+
                                        "SHA256 hash is " + SHA256);
                            }
                        }
                    };

            button1.setOnAction(event1);

            // create a VBox
            VBox vbox = new VBox(30, label, button, button1);

            // set Alignment
            vbox.setAlignment(Pos.CENTER);
            // create a scene
            Scene scene = new Scene(vbox, 900, 600);

            // set the scene
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void connectWithServer(File file) {

        try {
            // Create a socket connection to connect with the server
            Socket socket = new Socket("localhost", 900);

            // Create an input stream into the file you want to send
            FileInputStream fileInputStream = new FileInputStream(file);

            System.out.println("Connected with server!");

            // Create an output stream to write  to the server over the socket connection
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Get the name of the file you want to send and store it in filename
            String fileName = file.getName();

            // Convert the name of the file into an array of bytes to be sent to the server
            byte[] fileNameBytes = fileName.getBytes();

            // Create a byte array the size of the file so don't send too little or too much data to the server
            byte[] fileBytes = new byte[(int) file.length()];

            // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server
            fileInputStream.read(fileBytes);

            // Send the length of the name of the file so server knows when to stop reading
            dataOutputStream.writeInt(fileNameBytes.length);

            // Send the file name
            dataOutputStream.write(fileNameBytes);

            // Send the length of the byte array so the server knows when to stop reading
            dataOutputStream.writeInt(fileBytes.length);

            // Send the actual file
            dataOutputStream.write(fileBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
