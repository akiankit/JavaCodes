/**
 * UnicommerceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class UnicommerceServiceLocator extends org.apache.axis.client.Service implements com.unicommerce.uniware.services.UnicommerceService {

    public UnicommerceServiceLocator() {
    }


    public UnicommerceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UnicommerceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UnicommerceSoap11
    private java.lang.String UnicommerceSoap11_address = "https://sbluestone.unicommerce.com:443/services/soap/?version=1.2";

    public java.lang.String getUnicommerceSoap11Address() {
        return UnicommerceSoap11_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UnicommerceSoap11WSDDServiceName = "UnicommerceSoap11";

    public java.lang.String getUnicommerceSoap11WSDDServiceName() {
        return UnicommerceSoap11WSDDServiceName;
    }

    public void setUnicommerceSoap11WSDDServiceName(java.lang.String name) {
        UnicommerceSoap11WSDDServiceName = name;
    }

    public com.unicommerce.uniware.services.Unicommerce getUnicommerceSoap11() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UnicommerceSoap11_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUnicommerceSoap11(endpoint);
    }

    public com.unicommerce.uniware.services.Unicommerce getUnicommerceSoap11(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.unicommerce.uniware.services.UnicommerceSoap11Stub _stub = new com.unicommerce.uniware.services.UnicommerceSoap11Stub(portAddress, this);
            _stub.setPortName(getUnicommerceSoap11WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUnicommerceSoap11EndpointAddress(java.lang.String address) {
        UnicommerceSoap11_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.unicommerce.uniware.services.Unicommerce.class.isAssignableFrom(serviceEndpointInterface)) {
                com.unicommerce.uniware.services.UnicommerceSoap11Stub _stub = new com.unicommerce.uniware.services.UnicommerceSoap11Stub(new java.net.URL(UnicommerceSoap11_address), this);
                _stub.setPortName(getUnicommerceSoap11WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("UnicommerceSoap11".equals(inputPortName)) {
            return getUnicommerceSoap11();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnicommerceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnicommerceSoap11"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UnicommerceSoap11".equals(portName)) {
            setUnicommerceSoap11EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
