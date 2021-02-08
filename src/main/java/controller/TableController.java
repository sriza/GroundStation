package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.json.JSONObject;
import javafx.scene.control.TableColumn; 

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.BatteryData;
import main.java.model.MainData;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;

@SuppressWarnings("unchecked")
public class TableController implements Initializable{

    @FXML
    private TableView<MainData> maintable;

    @FXML
    private Label signallabel;

    @FXML
    private Label satlabel;

    @FXML
    private Label apogeelabel;

    @FXML
    private Label relabel;

    @FXML
    private Label airlabel;

    @FXML
    private Label timelabel;

    @FXML
    private Label activestagelabel;

    @FXML
    private Label accelerationlabel;

    @FXML
    private Label velocitylabel;

    @FXML
    private Label altitudelabel;

    @FXML
    private Label pressuregraphlabel;

    @FXML
    private Label batterygraphlabel2;

    @FXML
    private Label batterygraphlabel1;

    @FXML
    private Label tempgraphlabel;

    @FXML
    private TableColumn<MainData, Double> maintime;

    @FXML
    private TableColumn<MainData, Double> mainaltitude;

    @FXML
    private TableColumn<MainData, Double> mainvelocity;

    @FXML
    private TableColumn<MainData, Double> mainacceleration;

    @FXML
    private TableView<BatteryData> batteryone;

    @FXML
    private TableColumn<BatteryData, Double> batteryonetime;

    @FXML
    private TableColumn<BatteryData, Double> batteryoneper;

    @FXML
    private TableColumn<BatteryData, Double> batteryonetemp;

    @FXML
    private TableView<BatteryData> batterytwo;

    @FXML
    private TableColumn<BatteryData, Double> batterytwotime;

    @FXML
    private TableColumn<BatteryData, Double> batterytwoper;

    @FXML
    private TableColumn<BatteryData, Double> batterytwotemp;

    @FXML
    private Hyperlink loadbutton;

    public static TableController table = new TableController();

    private TableController(){ }

    public static TableController getTableInstance(){
        return table;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable();      
     }

    public void plotData(JSONObject data){
        updateLabel(data);
    }

    //initializes table
    private void initializeTable(){
        maintable.getColumns().addAll(maintime, mainaltitude, mainvelocity, mainacceleration);
        batteryone.getColumns().addAll(batteryonetime, batteryoneper, batteryonetemp);
        batterytwo.getColumns().addAll(batterytwotime, batterytwoper, batterytwotemp);
    }

    //label update
    public void updateLabel(JSONObject data){
        altitudelabel.setText(data.getDouble("alt")+" ft");
        velocitylabel.setText(data.getDouble("speed")+" m/s");
        accelerationlabel.setText(data.getDouble("acc")+" m/s^2");

        relabel.setText(data.getBoolean("re")?"TRUE":"FALSE");
        airlabel.setText(data.getBoolean("air")?"TRUE":"FALSE");
        timelabel.setText("T + "+data.getDouble("time"));

        tempgraphlabel.setText(data.getDouble("temp")+"°");
        pressuregraphlabel.setText(data.getDouble("pressure")+" ft");
        batterygraphlabel1.setText(data.getJSONObject("bat").getDouble("per")+" %");
        batterygraphlabel2.setText(data.getJSONObject("bat").getDouble("per")+" %");
    }

    // load graph view
     @FXML
     void loadGraphView(ActionEvent event) {
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

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
