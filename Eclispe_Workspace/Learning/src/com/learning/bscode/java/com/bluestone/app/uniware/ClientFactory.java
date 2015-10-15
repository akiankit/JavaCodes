package com.bluestone.app.uniware;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.common.base.Throwables;
import com.unicommerce.uniware.services.Unicommerce;
import com.unicommerce.uniware.services.UnicommerceServiceLocator;
import com.unicommerce.uniware.services.UnicommerceSoap11Stub;
import org.apache.axis.message.PrefixedQName;
import org.apache.axis.message.SOAPHeaderElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 9/27/12
 */
public class ClientFactory {

    private static final Logger log = LoggerFactory.getLogger(ClientFactory.class);

    private String endpoint;

    private String userName;

    private String password;

    private final PrefixedQName PREFIXED_QNAME = new PrefixedQName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                                                                   "Security",
                                                                   "wsse");

    private static AtomicInteger stubCount = new AtomicInteger(0);

    public ClientFactory(String endpoint, String userName, String password) {
        this.endpoint = endpoint;
        this.userName = userName;
        this.password = password;
    }

    Unicommerce getStub() throws ServiceException {
        log.debug("ClientFactory.getStub()");
        UnicommerceServiceLocator serviceLocator = new UnicommerceServiceLocator();
        serviceLocator.setUnicommerceSoap11EndpointAddress(endpoint);
        final Unicommerce unicommerceStub = serviceLocator.getUnicommerceSoap11();
        SOAPHeaderElement soapHeaderElement = null;
        try {
            soapHeaderElement = buildWSSEHeader(userName, password);
            ((UnicommerceSoap11Stub) unicommerceStub).setHeader(soapHeaderElement);
        } catch (SOAPException e) {
            log.error("Failed to set the wsse headers", e.toString(), e);
            throw new RuntimeException("Failed to add the wsse header to soap request", Throwables.getRootCause(e));
        }
        stubCount.incrementAndGet();
        log.info("Uniware stub count ={}", stubCount.get());
        return unicommerceStub;

    }

    private SOAPHeaderElement buildWSSEHeader(String userName, String password) throws SOAPException {
        log.debug("ClientFactory.buildWSSEHeader()");
        SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(PREFIXED_QNAME);
        soapHeaderElement.setActor(null);

        SOAPElement sub = soapHeaderElement.addChildElement("UsernameToken", "wsse");
        SOAPElement UNElement = sub.addChildElement("Username", "wsse");
        UNElement.addTextNode(userName);

        SOAPElement PwdElement = sub.addChildElement("Password");
        PwdElement.addTextNode(password);
        return soapHeaderElement;
    }
}
