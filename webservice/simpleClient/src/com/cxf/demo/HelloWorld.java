package com.cxf.demo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.0
 * 2015-05-29T15:27:28.730+08:00
 * Generated source version: 3.1.0
 * 
 */
@WebService(targetNamespace = "http://server.hw.demo/", name = "HelloWorld")
@XmlSeeAlso({ObjectFactory.class})
public interface HelloWorld {

    @WebMethod
    @RequestWrapper(localName = "sayHiToUser", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.SayHiToUser")
    @ResponseWrapper(localName = "sayHiToUserResponse", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.SayHiToUserResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String sayHiToUser(
        @WebParam(name = "arg0", targetNamespace = "")
        com.cxf.demo.User arg0
    );

    @WebMethod
    @RequestWrapper(localName = "getUsers", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.GetUsers")
    @ResponseWrapper(localName = "getUsersResponse", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.GetUsersResponse")
    @WebResult(name = "return", targetNamespace = "")
    public com.cxf.demo.IntegerUserMap getUsers();

    @WebMethod
    @RequestWrapper(localName = "sayHi", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.SayHi")
    @ResponseWrapper(localName = "sayHiResponse", targetNamespace = "http://server.hw.demo/", className = "com.cxf.demo.SayHiResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String sayHi(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    );
}
