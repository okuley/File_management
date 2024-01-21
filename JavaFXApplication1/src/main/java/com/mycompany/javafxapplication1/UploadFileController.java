/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



/**
 *
 * @author ntu-user
 */
public class UploadFileController {
    
    @FXML
    private  Button uploadbtn;
     
    @FXML
    private TableView fileTableView;
    
    @FXML
    private Text fileText;
    
     @FXML
    private TextField userTextField;
     
     @FXML
     private Button deleteFilebtn;
     
     @FXML
     private Button editFilebtn;
     
    
    
     @FXML
    public void initialize(String nameTextField){
    try {
            
        userTextField.setText(nameTextField.toString());
            DB myobj = new DB();
            fileTableView.getColumns().clear();
            fileTableView.getItems().clear();
        ObservableList<Files> data;
        data = myobj.getUserFilesFromTable(userTextField.getText());
        
        if(!data.isEmpty()){
        TableColumn owner_show = new TableColumn("Owner");
        owner_show.setCellValueFactory(
        new PropertyValueFactory<>("owner"));

        TableColumn file_name_show = new TableColumn("File_name");
        file_name_show.setCellValueFactory(
            new PropertyValueFactory<>("file_name"));
        
        TableColumn file_path_show = new TableColumn("File_path");
        file_path_show.setCellValueFactory(
            new PropertyValueFactory<>("file_path"));
        
        TableColumn file_size_show = new TableColumn("File_size");
        file_size_show.setCellValueFactory(
            new PropertyValueFactory<>("file_size"));
        TableColumn creation_date_show = new TableColumn("Creation_date");
        creation_date_show.setCellValueFactory(
            new PropertyValueFactory<>("creation_date"));
        TableColumn modification_date_show = new TableColumn("Modification_date");
        modification_date_show.setCellValueFactory(
            new PropertyValueFactory<>("modification_date"));
        
        fileTableView.setItems(data);
        fileTableView.getColumns().addAll(owner_show,file_name_show,file_path_show,file_size_show,creation_date_show,modification_date_show);
        }
        
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
    
     @FXML
    private void Upload(ActionEvent event)throws IOException {
        try{
        Stage UploadfileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
       File selectedFile = fileChooser.showOpenDialog(UploadfileStage);
      
        
        if(selectedFile!=null){
            
    File file = selectedFile.getCanonicalFile();
    
    //String file_name=file.getName();
    String file_name=UUID.randomUUID().toString();
    String owner=userTextField.getText();
    int file_size=(int)file.length();
    
    File userdir = new File("/home/ntu-user/App/"+owner);
if (!userdir.exists()){
    userdir.mkdirs();
}

    File newFile = new File("/home/ntu-user/App/"+owner +"/"+ file_name);
    java.nio.file.Files.copy(file.toPath(),newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    
    String file_path= newFile.getPath();
    //System.out.println(file_path);
    
    DB myobj =new DB();
    
    myobj.addDataToFileDB(owner,file_name,file_size,file_path);
        
       dialogue("Upload Confirmation","File "+file.getName()+" has been uploaded successfully");
       
            fileTableView.getColumns().clear();
            fileTableView.getItems().clear();
        ObservableList<Files> data;
        data = myobj.getUserFilesFromTable(userTextField.getText());
        
        if(!data.isEmpty()){
        TableColumn owner_show = new TableColumn("Owner");
        owner_show.setCellValueFactory(
        new PropertyValueFactory<>("owner"));

        TableColumn file_name_show = new TableColumn("File_name");
        file_name_show.setCellValueFactory(
            new PropertyValueFactory<>("file_name"));
        
        TableColumn file_path_show = new TableColumn("File_path");
        file_path_show.setCellValueFactory(
            new PropertyValueFactory<>("file_path"));
        
        TableColumn file_size_show = new TableColumn("File_size");
        file_size_show.setCellValueFactory(
            new PropertyValueFactory<>("file_size"));
        TableColumn creation_date_show = new TableColumn("Creation_date");
        creation_date_show.setCellValueFactory(
            new PropertyValueFactory<>("creation_date"));
        TableColumn modification_date_show = new TableColumn("Modification_date");
        modification_date_show.setCellValueFactory(
            new PropertyValueFactory<>("modification_date"));
        
        fileTableView.setItems(data);
        fileTableView.getColumns().addAll(owner_show,file_name_show,file_path_show,file_size_show,creation_date_show,modification_date_show);
         EncryptFile(newFile);
         
         newFile.delete();
        //DecryptFile(newFile);
        
        FileChunking(newFile);
        RemoteContainers rc = new RemoteContainers();
        //Remote host ips are dynamically assigned 
        rc.SendToContainer(newFile,"172.22.0.3",1);
        rc.SendToContainer(newFile,"172.22.0.5",2);
        rc.SendToContainer(newFile,"172.22.0.4",3);
        rc.SendToContainer(newFile,"172.22.0.2",4);
        //DecryptFile(newFile);
        }

        }
        }
       catch (Exception e) {
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

    /**
     * @brief EncryptFile method
     * @param file name of type File
     */

public void EncryptFile( File file) throws ZipException {
        //File filetoSplit;
    ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        try{
            //ZipFile zipFile = new ZipFile("/home/ntu-user/App/"+owner+"/"+file_name+".zip", "password".toCharArray());
            ZipFile zipFile = new ZipFile(file.getParent()+"/"+file.getName()+".zip", "password".toCharArray());
            //zipFile.addFile(new File("/home/ntu-user/App/"+owner+"/"+ file_name), zipParameters);
            zipFile.addFile(new File(file.getParent()+"/"+file.getName()), zipParameters);
            
           //filetoSplit= zipFile.getFile();
           
            
            System.out.println("File zipped with success" );
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            
        }

    
}

    /**
     * @brief DecryptFile method
     * @param file name of type File
     */
public void DecryptFile( File file) throws ZipException {

    ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        try{
            //ZipFile zipFile = new ZipFile("/home/ntu-user/App/"+owner+"/"+file_name+".zip", "password".toCharArray());
            ZipFile zipFile = new ZipFile(file.getParent()+"/"+file.getName()+".zip", "password".toCharArray());
            //zipFile.extractAll("/home/ntu-user/App/"+owner);
            zipFile.extractAll(file.getParent());
            //zipFile.removeFile(file_name+".zip");
            System.out.println("File unzipped with success");
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            
        }

    
}
    /**
     * @brief FileChunking method
     * @param file name of type File
     */

public List<File> FileChunking (File file) throws IOException{
    int counter = 1;
    List<File> files = new ArrayList<File>();
    //int sizeOfChunk = 0;
    File FileToSplit=  new File(file.getParent()+"/"+file.getName()+".zip");
    long size =FileToSplit.length();
    long sizeOfChunk=size/4;
    String eof = System.lineSeparator();
    try (BufferedReader br = new BufferedReader(new FileReader(FileToSplit))) {
        String name = FileToSplit.getName();
        String line = br.readLine();
        while (line != null && counter<5) {
            File newFile = new File(file.getParent(), name + "."
                    + String.format("%03d", counter++));
                  // File newFile = new File(file.getParent(), UUID.randomUUID().toString());
                  // counter++;
                  
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                long fileSize = 0;
                while (line != null) {
                    byte[] bytes = (line + eof).getBytes(Charset.defaultCharset());
                    if (fileSize + bytes.length > sizeOfChunk)
                        break;
                    out.write(bytes);
                    fileSize += bytes.length;
                    line = br.readLine();
                }
            }
            files.add(newFile);
            System.out.println("File has been splited "+newFile);
        }
    }
    return files;
    
}

@FXML
private void deleteFile(ActionEvent event){
 Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete File?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
       alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES) {
    
    try {
            
            DB myObj = new DB();
            Files fileTodelete=(Files) fileTableView.getSelectionModel().getSelectedItem();
            myObj.delFilefromDB(fileTodelete.getFile_name());
            //Start of reset the data after deleting
            fileTableView.getColumns().clear();
            fileTableView.getItems().clear();
             //End of reset the data after deleting
            ObservableList<Files> data;
        data = myObj.getUserFilesFromTable(userTextField.getText());
        
        if(!data.isEmpty()){
        TableColumn owner_show = new TableColumn("Owner");
        owner_show.setCellValueFactory(
        new PropertyValueFactory<>("owner"));

        TableColumn file_name_show = new TableColumn("File_name");
        file_name_show.setCellValueFactory(
            new PropertyValueFactory<>("file_name"));
        
        TableColumn file_path_show = new TableColumn("File_path");
        file_path_show.setCellValueFactory(
            new PropertyValueFactory<>("file_path"));
        
        TableColumn file_size_show = new TableColumn("File_size");
        file_size_show.setCellValueFactory(
            new PropertyValueFactory<>("file_size"));
        TableColumn creation_date_show = new TableColumn("Creation_date");
        creation_date_show.setCellValueFactory(
            new PropertyValueFactory<>("creation_date"));
        TableColumn modification_date_show = new TableColumn("Modification_date");
        modification_date_show.setCellValueFactory(
            new PropertyValueFactory<>("modification_date"));
        
        fileTableView.setItems(data);
        fileTableView.getColumns().addAll(owner_show,file_name_show,file_path_show,file_size_show,creation_date_show,modification_date_show);
        
          dialogue("Delete Confirmation","File has been deleted Successfully.");
        }
    }catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    
}

@FXML
private void editFile(ActionEvent event){
 
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to eidt File?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
       alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES) {
    
    try {
            
         
        
           // Desktop desktop = new Desktop();
            DB myObj = new DB();
            Files fileToedit=(Files) fileTableView.getSelectionModel().getSelectedItem();
            myObj.editFileDataDB(fileToedit.getFile_name());
            File newFile = new File("/home/ntu-user/App/"+fileToedit.getOwner() +"/"+ fileToedit.getFile_name());
            DecryptFile(newFile);
            //desktop.Open(newFile);
            //desktop.open("/home/ntu-user/App/"+fileToedit.getOwner() +"/"+ fileToedit.getFile_name()); 
            //Start of reset the data after deleting
            fileTableView.getColumns().clear();
            fileTableView.getItems().clear();
             //End of reset the data after deleting
            ObservableList<Files> data;
        data = myObj.getUserFilesFromTable(userTextField.getText());
        
        if(!data.isEmpty()){
        TableColumn owner_show = new TableColumn("Owner");
        owner_show.setCellValueFactory(
        new PropertyValueFactory<>("owner"));

        TableColumn file_name_show = new TableColumn("File_name");
        file_name_show.setCellValueFactory(
            new PropertyValueFactory<>("file_name"));
        
        TableColumn file_path_show = new TableColumn("File_path");
        file_path_show.setCellValueFactory(
            new PropertyValueFactory<>("file_path"));
        
        TableColumn file_size_show = new TableColumn("File_size");
        file_size_show.setCellValueFactory(
            new PropertyValueFactory<>("file_size"));
        TableColumn creation_date_show = new TableColumn("Creation_date");
        creation_date_show.setCellValueFactory(
            new PropertyValueFactory<>("creation_date"));
        TableColumn modification_date_show = new TableColumn("Modification_date");
        modification_date_show.setCellValueFactory(
            new PropertyValueFactory<>("modification_date"));
        
        fileTableView.setItems(data);
        fileTableView.getColumns().addAll(owner_show,file_name_show,file_path_show,file_size_show,creation_date_show,modification_date_show);
        
          dialogue("Edit Confirmation","File has been Updated Successfully.");
        }
    }catch (Exception e) {
            e.printStackTrace();
        }
    
}
}


 @FXML
 
 private void Cancel(){
        Stage secondaryStage = new Stage();
        Stage UploadfileStage = (Stage) uploadbtn.getScene().getWindow();
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
            String[] credentials = {userTextField.getText(),userTextField.getText()};
            secondaryController.initialise(credentials);    
             secondaryStage.show();
            UploadfileStage.close();
        }
            
        

        } catch (Exception e) {
            e.printStackTrace();
        }
 }

 
 @FXML
 
 private void DownloadFile(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to download File?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
       alert.showAndWait();
        
        if (alert.getResult() == ButtonType.YES) {
    
    try {
            
         
        
           
            DB myObj = new DB();
            Files fileTodownload=(Files) fileTableView.getSelectionModel().getSelectedItem();
            //myObj.editFileDataDB(fileTodownload.getFile_name());
            File newFile = new File("/home/ntu-user/App/"+fileTodownload.getOwner() +"/"+ fileTodownload.getFile_name());
            DecryptFile(newFile);
            
            File downloadFile = new File("/home/ntu-user/Downloads/"+fileTodownload.getFile_name());
    java.nio.file.Files.copy(newFile.toPath(),downloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            //Start of reset the data after deleting
            fileTableView.getColumns().clear();
            fileTableView.getItems().clear();
             //End of reset the data after deleting
            ObservableList<Files> data;
        data = myObj.getUserFilesFromTable(userTextField.getText());
        
        if(!data.isEmpty()){
        TableColumn owner_show = new TableColumn("Owner");
        owner_show.setCellValueFactory(
        new PropertyValueFactory<>("owner"));

        TableColumn file_name_show = new TableColumn("File_name");
        file_name_show.setCellValueFactory(
            new PropertyValueFactory<>("file_name"));
        
        TableColumn file_path_show = new TableColumn("File_path");
        file_path_show.setCellValueFactory(
            new PropertyValueFactory<>("file_path"));
        
        TableColumn file_size_show = new TableColumn("File_size");
        file_size_show.setCellValueFactory(
            new PropertyValueFactory<>("file_size"));
        TableColumn creation_date_show = new TableColumn("Creation_date");
        creation_date_show.setCellValueFactory(
            new PropertyValueFactory<>("creation_date"));
        TableColumn modification_date_show = new TableColumn("Modification_date");
        modification_date_show.setCellValueFactory(
            new PropertyValueFactory<>("modification_date"));
        
        fileTableView.setItems(data);
        fileTableView.getColumns().addAll(owner_show,file_name_show,file_path_show,file_size_show,creation_date_show,modification_date_show);
        
          dialogue("Edit Confirmation","File has been Updated Successfully.");
        }
    }catch (Exception e) {
            e.printStackTrace();
        }
    
}
 }

 
}

