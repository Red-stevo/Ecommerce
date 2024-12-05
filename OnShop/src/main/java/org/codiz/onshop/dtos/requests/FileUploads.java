package org.codiz.onshop.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploads {
    private String fileName;
    private byte[] fileData;
}
