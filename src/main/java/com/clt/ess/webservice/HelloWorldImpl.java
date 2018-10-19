package com.clt.ess.webservice;

import javax.jws.WebService;

@WebService(endpointInterface = "com.clt.ess.webservice.IHelloWorld")
public class HelloWorldImpl implements IHelloWorld {

    @Override
    public String sayHello(String username) {
        return "Hello, wolaile";
    }
    @Override
    public void setUser(String username) {

    }

}