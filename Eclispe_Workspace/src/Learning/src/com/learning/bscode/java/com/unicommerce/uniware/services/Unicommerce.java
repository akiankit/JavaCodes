/**
 * Unicommerce.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public interface Unicommerce extends java.rmi.Remote {
    public com.unicommerce.uniware.services.ServiceResponse cancelSaleOrder(com.unicommerce.uniware.services.CancelSaleOrderRequest cancelSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.GetExportJobStatusResponse getExportJobStatus(com.unicommerce.uniware.services.GetExportJobStatusRequest getExportJobStatusRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.SearchSaleOrderResponse searchSaleOrder(com.unicommerce.uniware.services.SearchSaleOrderRequest searchSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse createItemType(com.unicommerce.uniware.services.ItemTypeRequest createItemTypeRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrder(com.unicommerce.uniware.services.UnholdSaleOrderRequest unholdSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrderItems(com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest unholdSaleOrderItemsRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.CreateExportJobResponse createExportJob(com.unicommerce.uniware.services.CreateExportJobRequest createExportJobRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.GetSaleOrderResponse getSaleOrder(com.unicommerce.uniware.services.GetSaleOrderRequest getSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse holdSaleOrderItems(com.unicommerce.uniware.services.HoldSaleOrderItemsRequest holdSaleOrderItemsRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse editSaleOrderAddress(com.unicommerce.uniware.services.EditSaleOrderAddressRequest editSaleOrderAddressRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse createSaleOrder(com.unicommerce.uniware.services.CreateSaleOrderRequest createSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse createOrEditItemType(com.unicommerce.uniware.services.ItemTypeRequest createOrEditItemTypeRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse createVendorItemType(com.unicommerce.uniware.services.CreateVendorItemTypeRequest createVendorItemTypeRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.AddPurchaseOrderResponse addPurchaseOrder(com.unicommerce.uniware.services.AddPurchaseOrderRequest addPurchaseOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse holdSaleOrder(com.unicommerce.uniware.services.HoldSaleOrderRequest holdSaleOrderRequest) throws java.rmi.RemoteException;
    public com.unicommerce.uniware.services.ServiceResponse editItemType(com.unicommerce.uniware.services.ItemTypeRequest editItemTypeRequest) throws java.rmi.RemoteException;
}
