/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import com.jcraft.jsch.*;
import java.io.File;

/**
 *
 * @author ntu-user
 */
public class RemoteContainers {
    
    
    private static final String USERNAME = "root";
    private static final String PASSWORD = "soft40051_pass";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    
    /**
     * @brief SendToContainer method
     * @param file  of type File
     * @param REMOTE_HOST  of type String
     * @param index  of type int
     */
    public void SendToContainer(File file,String REMOTE_HOST, int index){
       //= "172.22.0.3";
    String localFile = (file.getParent()+"/"+file.getName()+".zip.00"+index);
    String remoteFile = ("/root/");
     
    Session jschSession = null;
    try {

            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");

            // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            
          
           
            // transfer file from local to remote server
            channelSftp.put(localFile, remoteFile);
            System.out.println("File has been moved to remote server " +index);
            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
    }
    
    public void GetFromContainer(File file, String REMOTE_HOST, int index){
        String localFile = (file.getParent()+"/"+file.getName()+".zip.00"+index);
    String remoteFile = ("/root/");
     
    Session jschSession = null;
    try {

            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");

            // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            
          
           
            // transfer file from local to remote server
            channelSftp.put(localFile, remoteFile);
            System.out.println("File has been moved to remote server " +index);
            channelSftp.exit();

        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        
    }
}
