package main.java.controller;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import org.json.JSONObject;


public class FileStorageController {

    private static File file;
    private static FileWriter fileWriter;
    public static FileStorageController datafile = new FileStorageController();

    private FileStorageController(){
        String filename = "datafiles/garuda_"+System.currentTimeMillis()/1000+".txt";
        file = new File(filename);

        try {
            if (file.createNewFile()) {
              System.out.println("File created: " + file.getName());
            } else {
              System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }  

        try{
            fileWriter = new FileWriter(filename);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static FileStorageController getFileInstance(){
        return datafile;
    }

    public void storeData(JSONObject data){
        try{
            fileWriter.write(data+"\n");

        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void close(){
        fileWriter.close();
    }
}
