/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class Files  {
    private SimpleStringProperty owner;
    private SimpleStringProperty file_name;
    private SimpleStringProperty file_path;
    private SimpleIntegerProperty file_size;
    private  ObjectProperty<Timestamp> creation_date;
    private  ObjectProperty<Timestamp> modification_date;

    
    
    

    Files(String owner, String file_name,String file_path, int file_size,Timestamp creation_date ) {
        this.creation_date = new SimpleObjectProperty(creation_date);
        this.owner = new SimpleStringProperty(owner);
        this.file_name = new SimpleStringProperty(file_name);
        this.file_path = new SimpleStringProperty(file_path);
        this.file_size = new SimpleIntegerProperty(file_size);
        
    }

    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }

    public String getFile_name() {
        return file_name.get();
    }

    public void setFile_name(String file_name) {
        this.file_name.set(file_name);
    }
    
    public String getFile_path() {
        return file_path.get();
    }

    public void setFile_path(String file_path) {
        this.file_path.set(file_path);
    }

    public int getFile_size() {
        return file_size.get();
    }

    public void setFile_size(int file_size) {
        this.file_size.set(file_size);
    }
    
    
    public Timestamp getCreation_date() {
        return creation_date.get();
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date.set(creation_date);
    }
    
     public Timestamp getModification_date() {
        return creation_date.get();
    }

    public void setModification_date(Timestamp modification_date) {
        this.creation_date.set(modification_date);
    }


}
