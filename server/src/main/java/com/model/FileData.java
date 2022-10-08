package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = ""
)
@XmlRootElement(
        name = "FileData"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData  {

    @XmlAttribute(
            name = "id",
            required = true
    )
    private int id;
    @XmlAttribute(
            name = "name",
            required = true
    )
    private String name;
    @XmlAttribute(
            name = "data",
            required = true
    )
    private byte[] data;
    @XmlAttribute(
            name = "fileExtension",
            required = true
    )
    private String fileExtension;
}
