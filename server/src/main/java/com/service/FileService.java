package com.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.io.File;

@WebService(targetNamespace = "http://localhost:1010/FileService/", name = "FileService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface FileService {
	
	@WebResult(name = "return", targetNamespace = "")
	@RequestWrapper(
			localName = "getNameOfFile",
			targetNamespace = "http://localhost:1010/FileService/",
			className = "sample.ws.service.RequestNameOfFile")
	@WebMethod(action = "urn:getNameOfFile")
	@ResponseWrapper(
			localName = "getNameOfFileResponse",
			targetNamespace = "http://localhost:1010/FileService/",
			className = "sample.ws.service.getNameOfFileResponse")
	String getNameOfFile(@WebParam(name = "name", targetNamespace = "") String name);

}
