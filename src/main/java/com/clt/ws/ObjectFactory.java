
package com.clt.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.clt.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoSomethingResponse_QNAME = new QName("http://ws.clt.com/", "doSomethingResponse");
    private final static QName _GetLoginUserInfoResponse_QNAME = new QName("http://ws.clt.com/", "GetLoginUserInfoResponse");
    private final static QName _DoSomething_QNAME = new QName("http://ws.clt.com/", "doSomething");
    private final static QName _GetLoginUserInfo_QNAME = new QName("http://ws.clt.com/", "GetLoginUserInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.clt.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLoginUserInfo }
     * 
     */
    public GetLoginUserInfo createGetLoginUserInfo() {
        return new GetLoginUserInfo();
    }

    /**
     * Create an instance of {@link GetLoginUserInfoResponse }
     * 
     */
    public GetLoginUserInfoResponse createGetLoginUserInfoResponse() {
        return new GetLoginUserInfoResponse();
    }

    /**
     * Create an instance of {@link DoSomething }
     * 
     */
    public DoSomething createDoSomething() {
        return new DoSomething();
    }

    /**
     * Create an instance of {@link DoSomethingResponse }
     * 
     */
    public DoSomethingResponse createDoSomethingResponse() {
        return new DoSomethingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoSomethingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.clt.com/", name = "doSomethingResponse")
    public JAXBElement<DoSomethingResponse> createDoSomethingResponse(DoSomethingResponse value) {
        return new JAXBElement<DoSomethingResponse>(_DoSomethingResponse_QNAME, DoSomethingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoginUserInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.clt.com/", name = "GetLoginUserInfoResponse")
    public JAXBElement<GetLoginUserInfoResponse> createGetLoginUserInfoResponse(GetLoginUserInfoResponse value) {
        return new JAXBElement<GetLoginUserInfoResponse>(_GetLoginUserInfoResponse_QNAME, GetLoginUserInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoSomething }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.clt.com/", name = "doSomething")
    public JAXBElement<DoSomething> createDoSomething(DoSomething value) {
        return new JAXBElement<DoSomething>(_DoSomething_QNAME, DoSomething.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLoginUserInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.clt.com/", name = "GetLoginUserInfo")
    public JAXBElement<GetLoginUserInfo> createGetLoginUserInfo(GetLoginUserInfo value) {
        return new JAXBElement<GetLoginUserInfo>(_GetLoginUserInfo_QNAME, GetLoginUserInfo.class, null, value);
    }

}
