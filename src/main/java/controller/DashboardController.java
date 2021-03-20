package main.java.controller;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.PerspectiveCamera;
import javax.swing.GroupLayout.Group;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.lang.Double; 

//google maps
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.MapTypeIdEnum;
// import java.text.DecimalFormat;
// import javafx.scene.web.WebView;
// import com.dlsc.gmapsfx.MapComponentInitializedListener;
// import com.dlsc.gmapsfx.service.directions.DirectionsServiceCallback;

//3d modal
import javafx.scene.shape.Cylinder;
import com.dlsc.gmapsfx.javascript.object.MarkerOptions;
import com.dlsc.gmapsfx.javascript.object.Marker;

//initializable
import javafx.fxml.Initializable;

//flight computer
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable{

    private static Boolean origin = true;
    private static Boolean init = true;
    private static Double altitudeA = 0.0;
    private static Double initAlt = 0.0;
    private static Boolean launch = true;
    private static Long startTime;
    private static Boolean ignitecheck = true;
    private static Long initTime;
	
    @FXML
    private LineChart<Number, Number> temperatureLineGraph;
    XYChart.Series<Number, Number> tempData;

    @FXML
    private LineChart<Number, Number> pressureLineGraph;
    XYChart.Series<Number, Number> pressureData;

    @FXML
    private LineChart<Number, Number> batteryLineGraph;
    XYChart.Series<Number, Number> batteryData;

    @FXML
    private Label xtransformlabel;

    @FXML
    private Label ytransformlabel;
    
    @FXML
    private Label ztransformlabel;

    @FXML
    private Label accelerationlabel;
    
    @FXML
    private Label velocitylabel;

    @FXML
    private Label altitudelabel;

    @FXML
    private Label tempgraphlabel;

    @FXML
    private Label pressuregraphlabel;

    @FXML
    private Label batterygraphlabel1;

    @FXML
    private Label batterygraphlabel2;

    @FXML
    private Label latlabel;

    @FXML
    private Label longlabel;

    @FXML
    private Label signallabel;

    @FXML
    private Label satlabel;

    @FXML
    private Label apogeelabel;

    @FXML
    private Label relabel;

    @FXML
    private Label ignitelabel;

    @FXML
    private Label timelabel;

    @FXML
    private Hyperlink loadbutton;

    @FXML
    private Label activestagelabel;

    @FXML
    private GoogleMapView googleMapView;

    private GoogleMap map;

    @FXML
    private Group axisgroup;

    //flight computer
    @FXML
    private Circle liftnode;

    @FXML
    private Circle burnoutnode;

    @FXML
    private Circle apogeenode;

    @FXML
    private Circle chutenode;

    @FXML
    private Circle landingnode;

    //3d modal
    @FXML
    private Cylinder cylinder;

    @FXML
    private PerspectiveCamera camera;


    public static DashboardController dashboard = new DashboardController();

    Integer window_size = 5;

    private DashboardController(){ }

    public static DashboardController getDashboardInstance(){
        return dashboard;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGraphs();     
        // this.googleMapView.addMapInitializedListener(() -> configureMap());
     }

    //updatedata
    public void plotData(JSONObject data){

        if(init){
            initTime = System.currentTimeMillis();
            initAlt = data.getDouble("alt");
            init = false;
        }

        if(ignitecheck && origin && data.getBoolean("ignite")){
            ignitecheck = false;
            startTime = System.currentTimeMillis();
        }

        data.put("time", Double.parseDouble(""+(System.currentTimeMillis()-initTime)));

        if(startTime!= null){
            data.put("time_minsec", (""+((System.currentTimeMillis()-startTime)/1000)/60 +" : "+ ((System.currentTimeMillis()-startTime)/1000)%60 ));
        }
        // data.put("time",System.currentTimeMillis());
        addGraphData(data);
        updateLabel(data);
        // updateMap(data);
        updateFlightComputer(data);
    }

    //graphs
    public void initializeGraphs() {
        initTemperatureLineGraph();
        initPressureLineGraph();
        initBatteryLineGraph();
	}
   
    private void initTemperatureLineGraph(){
        tempData = new XYChart.Series<>();
        tempData.setName("temperatureData");
        temperatureLineGraph.setAnimated(false);
        temperatureLineGraph.getData().add(tempData);

        NumberAxis yAxis = (NumberAxis) temperatureLineGraph.getYAxis();
        yAxis.setLabel("temperature (C)");
		yAxis.setForceZeroInRange(false);
        yAxis.setAnimated(false);

        NumberAxis xAxis = (NumberAxis) temperatureLineGraph.getXAxis();
        xAxis.setLabel("time (ms)");
		xAxis.setForceZeroInRange(false);
        xAxis.setAnimated(false);
    }

    private void initPressureLineGraph(){
        pressureData = new XYChart.Series<>();
        pressureData.setName("pressureData");
        pressureLineGraph.setAnimated(false);
        pressureLineGraph.getData().add(pressureData);

        NumberAxis yAxis = (NumberAxis) pressureLineGraph.getYAxis();
        yAxis.setLabel("pressure (N/m^2)");
		yAxis.setForceZeroInRange(false);
        yAxis.setAnimated(false);
		
		NumberAxis xAxis = (NumberAxis) pressureLineGraph.getXAxis();
        xAxis.setLabel("time (ms)");
		xAxis.setForceZeroInRange(false);
        xAxis.setAnimated(false);


    }

    private void initBatteryLineGraph(){
        batteryData = new XYChart.Series<>();
        batteryData.setName("batteryData");
        batteryLineGraph.setAnimated(false);
        batteryLineGraph.getData().add(batteryData);

        NumberAxis yAxis = (NumberAxis) batteryLineGraph.getYAxis();
        yAxis.setLabel("percentage (%)");
		yAxis.setForceZeroInRange(false);
        yAxis.setAnimated(false);
		
		NumberAxis xAxis = (NumberAxis) batteryLineGraph.getXAxis();
        xAxis.setLabel("time (ms)");
		xAxis.setForceZeroInRange(false);
        xAxis.setAnimated(false);
    }
	
    public void addGraphData(JSONObject data) {
        try{
            addTemperatureData(data.getDouble("time")/1000, data.getDouble("temp") );
            addPressureData(data.getDouble("time")/1000, data.getDouble("pressure") );
            // addBatteryData(data.getDouble("time"), data.getJSONObject("bat").getDouble("per") );
        } catch (Exception ex){
            System.out.print("add graph data:" +ex);
        }
	
	}

	private void addTemperatureData(Double x, Double y) {
		tempData.getData().add(new XYChart.Data<>(x, y));

        if (tempData.getData().size() > window_size)
            tempData.getData().remove(0);	
    }


    private void addPressureData(Double x, Double y) {
		pressureData.getData().add(new XYChart.Data<>(x, y));

		if (pressureData.getData().size() > window_size)
			pressureData.getData().remove(0);
	}
	
	private void addBatteryData(Double x, Double y) {
		batteryData.getData().add(new XYChart.Data<>(x, y));

        if (batteryData.getData().size() > window_size)
			batteryData.getData().remove(0);
	}
    //graphs end

    //label update
    public void updateLabel(JSONObject data){
        xtransformlabel.setText(data.getDouble("x")+"°");
        ytransformlabel.setText(data.getDouble("y")+"°");
        ztransformlabel.setText(data.getDouble("z")+"°");

        latlabel.setText(data.getDouble("lat")+"");
        longlabel.setText(data.getDouble("lon")+"");

        altitudelabel.setText(data.getDouble("alt")+" ft");
        velocitylabel.setText(data.getDouble("speed")+" ft");
        accelerationlabel.setText(data.getDouble("acc")+" ft");
        satlabel.setText(data.getDouble("sats")+"");
        ignitelabel.setText((data.getBoolean("ignite")+"").toUpperCase()+"");

        // relabel.setText(data.getBoolean("re")?"TRUE":"FALSE");
        // airlabel.setText(data.getBoolean("air")?"TRUE":"FALSE");
        if(!ignitecheck){
            timelabel.setText("T + "+(data.getString("time_minsec")));
        }

        tempgraphlabel.setText(data.getDouble("temp")+"°C");
        pressuregraphlabel.setText(data.getDouble("pressure")+" ft");
        batterygraphlabel1.setText(data.getDouble("battemp")+" °C");
        batterygraphlabel2.setText(data.getDouble("volt")+" V");
    }

    //google map
    protected void configureMap() {
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(28.3949, 84.1240))
                .mapType(MapTypeIdEnum.HYBRID)
                .zoom(9);
        map = this.googleMapView.createMap(mapOptions, true);

        this.googleMapView.setKey("AIzaSyC2hJ67rwbmIhXJa2D9gAa2m1OxgsWTHnw");

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            // latlabel.setText(formatter.format(latLong.getLatitude()));
            // longlabel.setText(formatter.format(latLong.getLongitude()));
        });
    }


    protected void updateMap(JSONObject data){
        if(this.map != null) {
            if(origin){
                MarkerOptions launchsite = new MarkerOptions();
                launchsite.position(new LatLong(data.getDouble("lat"), data.getDouble("lon")));
                Marker launch = new Marker(launchsite);
                map.addMarker( launch );
                origin = false;
            } else {
                MarkerOptions launchsite = new MarkerOptions();
                launchsite.position(new LatLong(data.getDouble("lat"), data.getDouble("lon")));
                Marker launch = new Marker(launchsite);
                map.addMarker( launch );
            }
        }
    }

    // load table view
    @FXML
    void loadTableView(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/fxml/tabulate.fxml"));
            fxmlLoader.setController(TableController.getTableInstance());
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

    //3d modal 
    protected void initializeModal(){
        
    }


    /*
        * pitch (around its X axis), yaw (around its Y axis) and roll (around its Z axis),
        * alf is roll, bet is pitch and gam is yaw.
        * https://stackoverflow.com/questions/30145414/rotate-a-3d-object-on-3-axis-in-javafx-properly
    */
    // void protected updateModal( {

    // }


    
	// 	private void matrixRotateNode(Node n, double alf, double bet, double gam){
	// 	    double A11=Math.cos(alf)*Math.cos(gam);
	// 	    double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
	// 	    double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
	// 	    double A21=-Math.cos(gam)*Math.sin(alf);
	// 	    double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
	// 	    double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
	// 	    double A31=Math.sin(gam);
	// 	    double A32=-Math.cos(gam)*Math.sin(bet);
	// 	    double A33=Math.cos(bet)*Math.cos(gam);

	// 	    double d = Math.acos((A11+A22+A33-1d)/2d);
	// 	    if(d!=0d){
	// 	        double den=2d*Math.sin(d);
	// 	        Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
	// 	        n.setRotationAxis(p);
	// 	        n.setRotate(Math.toDegrees(d));                    
	// 	    }
	// 	}

    //update flight computer
    protected void updateFlightComputer(JSONObject data) {
        // altitudeA = data.getDouble();
        if(launch && data.getBoolean("ignite")){
            launch = false;
            liftnode.setFill(Color.valueOf("6efa7c"));
        }

        if(data.getDouble("alt") < altitudeA) {
            apogeenode.setFill(Color.valueOf("6efa7c"));
        }

        // if(data.getBoolean("re")) {
        //     chutenode.setFill(Color.valueOf("6efa7c"));
        // }

        if(data.getString("time_minsec").contains("0 : 10")){
            burnoutnode.setFill(Color.valueOf("6efa7c"));
        }

        if(data.getBoolean("parachute")) {
            chutenode.setFill(Color.valueOf("6efa7c"));
        }

        // if(Double.compare(data.getDouble("alt"), initAlt)==0 || Double.compare(data.getDouble("alt"), initAlt)<0) {
        //     landingnode.setFill(Color.valueOf("6efa7c"));
        // }

        altitudeA = data.getDouble("alt");


    }

}