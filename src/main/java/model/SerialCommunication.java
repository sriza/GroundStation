package main.java.model;

import jssc.*;
import main.java.controller.DashboardController;
import main.java.controller.FileStorageController;
import main.java.controller.TableController;
import javafx.application.Platform;
import org.json.JSONObject;

public class SerialCommunication implements SerialPortEventListener {
    SerialPort serialPort=null;
    public static double value=0;
    String buffer= "";

    public static SerialCommunication instance=new SerialCommunication();

    private SerialCommunication(){}


    public static SerialCommunication getInstance(){

        return instance;
    }

    public void connect(String portName) throws SerialPortException {

        disConnect();
        serialPort=new SerialPort(portName);
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,0);
        int mask = SerialPort.MASK_RXCHAR; 
        serialPort.setEventsMask(mask);
        serialPort.addEventListener(this::serialEvent);
    }

    public void disConnect()  {
        if(serialPort!=null) {
            if (serialPort.isOpened()) {

                try {
                    serialPort.removeEventListener();
                    serialPort.closePort();
                    FileStorageController.getFileInstance().storeData(new JSONObject(buffer));

                    System.out.println("Disconnected");
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

        if(event.isRXCHAR()) {
            try 
            {
                String data = serialPort.readString(1);

                if(data.equals("{"))
                {
                    buffer = data;
                }

                if (!data.equals("") && buffer.startsWith("{") && !data.equals("{") ) {
                        buffer= buffer+data;
                        Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(data.equals("}")){
                                // System.out.println(buffer);
                                try{
                                    DashboardController.getDashboardInstance().plotData(new JSONObject(buffer));
                                    FileStorageController.getFileInstance().storeData(new JSONObject(buffer));
                                    // TableController.getTableInstance().plotData(new JSONObject(buffer.replace(" end1 ","")));
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                    // System.out.println("Not a proper json format");
                                }
                                    
                                buffer = "";
                            }
                        }
                    });


                }
 
            }
            catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }


    static int countOccurences(String str, String word){ 
        String a[] = str.split(" "); 

        int count = 0; 
        for (int i = 0; i < a.length; i++)  
        { 
            if (word.equals(a[i])) 
                count++; 
        } 
    
        return count; 
    } 

}
