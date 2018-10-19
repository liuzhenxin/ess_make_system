package com.clt.ess.webservice;

import javax.jws.WebService;

@WebService
public interface IHelloWorld {
    public String sayHello(String username);
    public void setUser(String username);
}