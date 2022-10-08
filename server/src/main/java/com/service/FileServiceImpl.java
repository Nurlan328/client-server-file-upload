package com.service;

import javax.jws.WebService;
@WebService(
		serviceName = "File",
		portName = "FilePort",
		targetNamespace = "http://localhost:1010/FileService/",
		endpointInterface = "com.service.FileService")
public class FileServiceImpl implements FileService {

	@Override
	public String getNameOfFile(String name) {
		
		return "Name of File " + name;
	}
}
