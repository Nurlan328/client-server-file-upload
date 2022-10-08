package com.file;

import com.model.FileData;

import com.service.FileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    @Resource
    private static FileService fileService;
    private static ArrayList<FileData> fileData = new ArrayList<>();
    private static DataInputStream dataInputStream = null;


    public static void receiveFile() throws IOException {

        int fileId = 0;
        ServerSocket serverSocket = new ServerSocket(900);
        System.out.println("Waiting for client...");

        try {
            Socket socket = serverSocket.accept();

            System.out.println("Connected with client!");
            dataInputStream = new DataInputStream(socket.getInputStream());

            // Read the size of the file name so know when to stop reading
            int fileNameLength = dataInputStream.readInt();

            if (fileNameLength > 0) {

                // Byte array to hold name of file
                byte[] fileNameBytes = new byte[fileNameLength];

                // Read from the input stream into the byte array
                dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);

                // Create the file name from the byte array
                String fileName = new String(fileNameBytes);

                // Read how much data to expect for the actual content of the file
                int fileContentLength = dataInputStream.readInt();

                if (fileContentLength > 0) {
                    byte[] fileContentBytes = new byte[fileContentLength];

                    // Read from the input stream into the fileContentBytes array
                    dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);

                    // Add the new file to the array list which holds all our data
                    fileData.add(new FileData(fileId, fileName, fileContentBytes, getFileExtension(fileName)));

                    getHash(fileData.get(fileId));

                    // Increment the fileId for the next file to be received
                    fileId++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            dataInputStream.close();
        }
    }

    private static void getHash(FileData file) {
        String MD5 = DigestUtils.md5Hex(file.toString());
        System.out.println("MD5 hash = " + MD5);

        String SHA256 = DigestUtils.sha256Hex(file.toString());
        System.out.println("SHA256 hash = " + SHA256);
    }

    public static String getFileExtension(String fileName) {
        System.out.println("Name of file = " + fileName);
        // Get the file type by using the last occurence of . (for example aboutMe.txt returns txt)
        // Will have issues with files like myFile.tar.gz.
        int i = fileName.lastIndexOf('.');
        // If there is an extension.
        if (i > 0) {
            // Set the extension to the extension of the filename
            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }
}

