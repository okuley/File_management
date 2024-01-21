/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gyening
 */



public class EditUserController  {
    
    @FXML
    private TextField usernameTextField;
     @FXML
     private PasswordField oldpasswordTextField;
     
     @FXML
     private PasswordField newpasswordTextField;
     @FXML
     private PasswordField renewpasswordTextField;
     
     @FXML
     private Button updateUser;
     
     @FXML
    private TableView dataTableView;
     
     @FXML
    private Button cancelUpdatebtn;

   @FXML
    public void initialize(String nameTextField){
    try {
            
        usernameTextField.setText(nameTextField.toString());
            
        

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
    @FXML
    private void updateUser(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage editUserStage = (Stage) updateUser.getScene().getWindow();
        try {
           
            DB myObj = new DB();
            
            if (newpasswordTextField.getText().equals(renewpasswordTextField.getText()) && 
                   myObj.generateSecurePassword(oldpasswordTextField.getText()) .equals(myObj.getUserPassFromTable(usernameTextField.getText()))) {
                myObj.editUserDataDB(usernameTextField.getText(), newpasswordTextField.getText());
                dialogue("Updating information in the database for "+usernameTextField.getText(), "Successful!");
                
            FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 640, 480);
                secondaryStage.setScene(scene);
                secondaryStage.setTitle("Show Users");
                String msg="some data sent from edit Controller";
                secondaryStage.setUserData(msg);
            SecondaryController secondaryController = loader.getController();
            String[] credentials = {usernameTextField.getText(), newpasswordTextField.getText()};
            secondaryController.initialise(credentials);    
             secondaryStage.show();
            editUserStage.close();
            
            } else {
                dialogue("Passwords does not match for  "+usernameTextField.getText(), "Error!");
            }
            
            
            
            
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 private void dialogue(String headerMsg, String contentMsg) {
        Stage editUserStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }
    
 @FXML
 
 private void cancelUserUpdate(){
        Stage secondaryStage = new Stage();
        Stage editUserStage = (Stage) updateUser.getScene().getWindow();
     try {
            
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel this operation?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
       alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES){
        
        FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 640, 480);
                secondaryStage.setScene(scene);
                secondaryStage.setTitle("Show Users");
                String msg="some data sent from edit Controller";
                secondaryStage.setUserData(msg);
            SecondaryController secondaryController = loader.getController();
            String[] credentials = {usernameTextField.getText(), newpasswordTextField.getText()};
            secondaryController.initialise(credentials);    
             secondaryStage.show();
            editUserStage.close();
        }
            
        

        } catch (Exception e) {
            e.printStackTrace();
        }
 }
}
