package com.mycompany.javafxapplication1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class SecondaryController {
    
    @FXML
    private TextField userTextField;
    
    @FXML
    private TableView dataTableView;

    @FXML
    private Button secondaryButton;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private TextField customTextField;
    
    @FXML
    private Button delUserBtn;
    
    @FXML
    private Button uploadfilehandlerbtn;
    
    @FXML
    private void RefreshBtnHandler(ActionEvent event){
        Stage primaryStage = (Stage) customTextField.getScene().getWindow();
        customTextField.setText((String)primaryStage.getUserData());
    }
    
    
    
    @FXML
    private void deleteUser(ActionEvent event){
    
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete user?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
       alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES) {
    
    try {
            
            DB myObj = new DB();
            User deluser=(User) dataTableView.getSelectionModel().getSelectedItem();
            myObj.delDatafromDB(deluser.getUser());
            //Start of reset the data after deleting
            dataTableView.getColumns().clear();
            dataTableView.getItems().clear();
             //End of reset the data after deleting
            
             ObservableList<User> data;
        
            data = myObj.getDataFromTable();
            TableColumn user = new TableColumn("User");
        user.setCellValueFactory( new PropertyValueFactory<>("user"));

        TableColumn pass = new TableColumn("Pass");
        pass.setCellValueFactory( new PropertyValueFactory<>("pass"));
        dataTableView.setItems(data);
        dataTableView.getColumns().addAll(user, pass);
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
     
    }
    
    
    
     @FXML
    private void editBtnHandler(ActionEvent event) {
        Stage editUserStage = new Stage();
        Stage secondaryStage = (Stage) delUserBtn.getScene().getWindow();
        try {
           
            User edituser=(User) dataTableView.getSelectionModel().getSelectedItem();
          
            // System.out.println(edituser.getUser());
             String name=edituser.getUser();
            
            FXMLLoader loader = new FXMLLoader();
            
            
             loader.setLocation(getClass().getResource("editUser.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 640, 480);
                editUserStage.setScene(scene);
                editUserStage.setTitle("Edit User details");
           
            EditUserController editUserController = loader.getController();
            editUserController.initialize(name);
            editUserStage.show();
            secondaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
        
    }
    
    
    @FXML
    private void UploadfileBtnHandler(ActionEvent event) {
        Stage UploadfileStage = new Stage();
       Stage secondaryStage = (Stage) delUserBtn.getScene().getWindow();
        try {
           
            User edituser=(User) dataTableView.getSelectionModel().getSelectedItem();
          
            // System.out.println(edituser.getUser());
             String name=edituser.getUser();
            
            FXMLLoader loader = new FXMLLoader();
            
            
             loader.setLocation(getClass().getResource("UploadFile.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 640, 480);
                UploadfileStage.setScene(scene);
                UploadfileStage.setTitle("Upload and Download files");
           
            UploadFileController uploadFileController = loader.getController();
            uploadFileController.initialize(name);
            UploadfileStage.show();
            secondaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
        
    }
    
    
        
    @FXML
    private void switchToPrimary(){
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
        try {
            
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise(String[] credentials) {
        userTextField.setText(credentials[0]);
        DB myObj = new DB();
        ObservableList<User> data;
        try {
            data = myObj.getDataFromTable();
            TableColumn user = new TableColumn("User");
        user.setCellValueFactory(
        new PropertyValueFactory<>("user"));

        TableColumn pass = new TableColumn("Pass");
        pass.setCellValueFactory(
            new PropertyValueFactory<>("pass"));
        dataTableView.setItems(data);
        dataTableView.getColumns().addAll(user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
