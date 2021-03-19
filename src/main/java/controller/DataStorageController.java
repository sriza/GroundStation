package main.java.controller;

public class DataStorageController {
    public static DataStorageController data = new DataStorageController();

    Integer window_size =10;

    private DataStorageController(){ }

    public static DataStorageController getDashboardInstance(){
        return data;
    }
    
}
