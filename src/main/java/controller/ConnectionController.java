package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import jssc.SerialPort;
import jssc.SerialPortList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import main.java.model.SerialCommunication;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;


public class ConnectionController implements Initializable {

    SerialPort serialPort = null;
    ObservableList<String> portList;

    @FXML
    private ChoiceBox<String> port_no;

    @FXML
    private Button submit;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            getAndSetPorts();
    }

    @FXML
    void connectMicroController(ActionEvent event) {
        String portSelected = port_no.getValue();
        if(portSelected != "") {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                        try {
                            SerialCommunication.getInstance().connect(portSelected);
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            });

            thread.setDaemon(true);
            thread.start();
            
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/fxml/dashboard.fxml"));
                fxmlLoader.setController(DashboardController.getDashboardInstance());
                Parent root = (Parent)fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("GARUDA");
                stage.setScene(scene);
                stage.show();

                ((Node)(event.getSource())).getScene().getWindow().hide();
                
            } catch (IOException ex) {
                System.out.println("loader :"+ex);
            }
           
        } 
    }

    void  getAndSetPorts(){
        portList= FXCollections.observableArrayList(SerialPortList.getPortNames());
        port_no.setItems(portList);
    }   
    
}

