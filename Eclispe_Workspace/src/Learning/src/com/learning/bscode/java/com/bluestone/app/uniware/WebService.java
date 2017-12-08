package com.bluestone.app.uniware;

import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.rpc.ServiceException;

import com.google.common.base.Throwables;
import com.unicommerce.uniware.services.CancelSaleOrderRequest;
import com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrder;
import com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem;
import com.unicommerce.uniware.services.CreateExportJobRequest;
import com.unicommerce.uniware.services.CreateExportJobResponse;
import com.unicommerce.uniware.services.CreateSaleOrderRequest;
import com.unicommerce.uniware.services.Error;
import com.unicommerce.uniware.services.GetExportJobStatusRequest;
import com.unicommerce.uniware.services.GetExportJobStatusResponse;
import com.unicommerce.uniware.services.GetSaleOrderRequest;
import com.unicommerce.uniware.services.GetSaleOrderRequestSaleOrder;
import com.unicommerce.uniware.services.GetSaleOrderResponse;
import com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrder;
import com.unicommerce.uniware.services.HoldSaleOrderItemsRequest;
import com.unicommerce.uniware.services.HoldSaleOrderRequest;
import com.unicommerce.uniware.services.ItemTypeRequest;
import com.unicommerce.uniware.services.SaleOrder;
import com.unicommerce.uniware.services.ServiceResponse;
import com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest;
import com.unicommerce.uniware.services.UnholdSaleOrderRequest;
import com.unicommerce.uniware.services.Unicommerce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.ProductionAlert;

/**
 * @author Rahul Agrawal
 *         Date: 9/27/12
 */
@Service
public class WebService {

    @Value("${uniware.webservice.url}")
    private String endpoint;

    @Value("${uniware.webservice.username}")
    private String userName;

    @Value("${uniware.webservice.password}")
    private String password;

    private static final Logger log = LoggerFactory.getLogger(WebService.class);
    private Unicommerce stub;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;


    public WebService() {
    }

    @PostConstruct
    private void init() {
        log.info("Creating a new stub for {} , user name={} ", endpoint, userName);
        ClientFactory clientFactory = new ClientFactory(endpoint, userName, password);
        try {
            stub = clientFactory.getStub();
        } catch (ServiceException e) {
            log.error("Failed to get the uniware webservice stub", e);
            throw new RuntimeException("Failed to get the stub to invoke the webservice", e);
        }
    }

    GetSaleOrderResponseSaleOrder getSaleOrder(String orderCode) throws RemoteException {
        log.debug("WebService.getSaleOrder() for OrderId={}", orderCode);
        GetSaleOrderResponseSaleOrder saleOrder = null;

        GetSaleOrderRequest saleOrderRequest = new GetSaleOrderRequest();
        saleOrderRequest.setSaleOrder(new GetSaleOrderRequestSaleOrder(orderCode));

        GetSaleOrderResponse response = stub.getSaleOrder(saleOrderRequest);
        if (response.isSuccessful()) {
            saleOrder = response.getSaleOrder();
            ErrorHandler.parseWarnings(response.getWarnings());
        } else {
            ErrorHandler.parseErrors(response.getErrors(), orderCode);
        }
        return saleOrder;
    }

    boolean createNewSaleOrder(SaleOrder saleOrder) throws RemoteException {
        log.info("WebService.create a New SaleOrder(): orderCode={}", saleOrder.getCode());
        CreateSaleOrderRequest request = new CreateSaleOrderRequest(saleOrder);
        boolean isSuccess = false;
        ServiceResponse response = null;
        try {
            response = stub.createSaleOrder(request);
            isSuccess = parseResponse(response, saleOrder.getCode());
            if (!isSuccess) {
                raiseAlert(response, saleOrder.getCode());
            }
        } catch (Exception e) {
            log.error("Error: WebService.createNewSaleOrder(): OrderId={}", saleOrder.getCode(), Throwables.getRootCause(e));
            Throwables.propagateIfInstanceOf(e, RemoteException.class);
            Throwables.propagateIfPossible(e);
        }
        return isSuccess;
    }

    private void raiseAlert(ServiceResponse response, String orderCode) {
        Error[] errors = new Error[0];
        if (response != null) {
            errors = response.getErrors();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to create the order in uniware. ").append("Order Id = ").append(orderCode).append(" ");
        for (Error error : errors) {
            String code = error.getCode();
            String description = error.getDescription();
            String message = error.getMessage();
            stringBuilder.append(" ErrorCode=").append(code).append(" Description=").append(description).append(" Message=").append(message).append("\n");
        }
        productionAlert.send(stringBuilder.toString());
    }


    private boolean parseResponse(ServiceResponse response, String inputData) {
        boolean isSuccessful = response.isSuccessful();
        if (isSuccessful) {
            ErrorHandler.parseWarnings(response.getWarnings());
        } else {
            ErrorHandler.parseResponse(response, inputData);
            isSuccessful = false;
        }
        return isSuccessful;
    }

    boolean cancelSaleOrder(String orderCode, List<CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem> saleOrderItems) throws RemoteException {
        log.info("WebService.cancelSaleOrder(): OrderCode={} ,  No.of OrderItemId = {}", orderCode, saleOrderItems.size());
        CancelSaleOrderRequestSaleOrder order = new CancelSaleOrderRequestSaleOrder();
        order.setCode(orderCode);
        CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem[]
                array = new CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem[saleOrderItems.size()];
        order.setSaleOrderItems(saleOrderItems.toArray(array));
        ServiceResponse response = stub.cancelSaleOrder(new CancelSaleOrderRequest(order));
        return parseResponse(response, orderCode);
    }

    public boolean createEditItem(ItemTypeRequest itemTypeRequest) throws RemoteException {
        log.info("WebService.createEditItem(): Category Code={} SkuCode={}", itemTypeRequest.getCategoryCode(), itemTypeRequest.getItemSKU());
        //ServiceResponse response = stub.createItemType(itemTypeRequest);
        ServiceResponse response = stub.createOrEditItemType(itemTypeRequest);
        return parseResponse(response, ("SkuCode=" + itemTypeRequest.getItemSKU()));
    }


    public boolean holdOrder(String orderCode) throws RemoteException {
        HoldSaleOrderRequest request = HoldUnHoldRequestBuilder.getHoldOrderRequest(orderCode);
        ServiceResponse serviceResponse = stub.holdSaleOrder(request);
        return parseResponse(serviceResponse, orderCode);
    }

    public void unHoldOrder(String orderId) throws RemoteException {
        UnholdSaleOrderRequest request = HoldUnHoldRequestBuilder.getUnHoldOrderRequest(orderId);
        stub.unholdSaleOrder(request);
    }

    public boolean holdOrderItems(String bsOrderId, List<String> orderItemIds) throws RemoteException {
        HoldSaleOrderItemsRequest request = HoldUnHoldRequestBuilder.getHoldItemsRequest(bsOrderId, orderItemIds);
        ServiceResponse serviceResponse = stub.holdSaleOrderItems(request);
        return parseResponse(serviceResponse, bsOrderId);
    }

    public boolean unHoldOrderItems(String bsOrderId, List<String> orderItemIds) throws RemoteException {
        UnholdSaleOrderItemsRequest request = HoldUnHoldRequestBuilder.getUnHoldItemsRequest(bsOrderId, orderItemIds);
        ServiceResponse serviceResponse = stub.unholdSaleOrderItems(request);
        return parseResponse(serviceResponse, bsOrderId);
    }


    public String createJob(CreateExportJobRequest exportJobRequest) throws RemoteException {
        log.info("WebService.createJob():{}", exportJobRequest.getExportJobTypeName());
        CreateExportJobResponse exportJobResponse = stub.createExportJob(exportJobRequest);
        String jobCode = null;
        if (exportJobResponse.isSuccessful()) {
            jobCode = exportJobResponse.getJobCode();
        } else {
            ErrorHandler.parseErrors(exportJobResponse.getErrors(), exportJobRequest.getExportJobTypeName());
        }
        return jobCode;
    }

    public String getJobStatus(String jobCode) throws RemoteException {
        log.debug("WebService.getJobStatus(): JobCode={}", jobCode);
        String result = "";
        GetExportJobStatusResponse jobStatusResponse = stub.getExportJobStatus(new GetExportJobStatusRequest(jobCode));
        boolean isSuccessful = jobStatusResponse.isSuccessful();
        if (isSuccessful) {
            String status = jobStatusResponse.getStatus();
            log.debug("WebService.getJobStatus() returns {}", status);
            if (status != null && "COMPLETE".equals(status)) {
                result = jobStatusResponse.getFilePath();
            }
        } else {
            Error[] errors = jobStatusResponse.getErrors();
            ErrorHandler.parseErrors(errors, ("JobCode=" + jobCode));
        }
        return result;
    }
}
