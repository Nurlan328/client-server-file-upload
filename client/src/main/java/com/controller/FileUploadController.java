package com.controller;

import com.exception.StorageFileNotFoundException;
import com.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws Exception {

        storageService.store(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        sendFile(file);

        return "redirect:/";
    }

    private static void sendFile(MultipartFile file) throws IOException {

        try {
            // Create a socket connection to connect with the server
            Socket socket = new Socket("localhost", 900);

            File path = FileUtils.getFile(file.getOriginalFilename());
            final File[] fileToSend = new File[(int) file.getSize() + 100];
            fileToSend[0] = multipartToFile(file, String.valueOf(path));

            // Create an input stream into the file you want to send
            FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());

            System.out.println("Connected with server!");

            // Create an output stream to write to write to the server over the socket connection
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Get the name of the file you want to send and store it in filename
            String fileName = fileToSend[0].getName();

            // Convert the name of the file into an array of bytes to be sent to the server
            byte[] fileNameBytes = fileName.getBytes();

            // Create a byte array the size of the file so don't send too little or too much data to the server
            byte[] fileBytes = new byte[(int) fileToSend[0].length()];

            // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server
            fileInputStream.read(fileBytes);

            // Send the length of the name of the file so server knows when to stop reading
            dataOutputStream.writeInt(fileNameBytes.length);

            // Send the file name
            dataOutputStream.write(fileNameBytes);

            // Send the length of the byte array so the server knows when to stop reading
            dataOutputStream.writeInt(fileBytes.length);

            // Send the actual file
            dataOutputStream.write(fileBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadFile(@PathVariable String filename) throws Exception {

        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws Exception {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "loadFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
