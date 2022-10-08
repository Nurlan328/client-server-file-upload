package com.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileUtility {
    private Map<String,FileData> fileDataMap = new HashMap<String,FileData>();

    public FileUtility() {
        FileData fileData = new FileData();
        fileData.setId(Integer.parseInt("1"));
        fileData.setName("Philosophy of Java");
        fileData.setData(new byte[]{127});
        fileData.setFileExtension("pdf");

        FileData fileData1 = new FileData();
        fileData.setId(Integer.parseInt("2"));
        fileData.setName("NoteBook");
        fileData.setData(new byte[]{127});
        fileData.setFileExtension("txt");

        fileDataMap.put(String.valueOf(1), fileData);
        fileDataMap.put(String.valueOf(2), fileData1);
    }
}
