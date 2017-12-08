package com.unicommerce.uniware.services;

import java.rmi.RemoteException;

public class UnicommerceProxy implements com.unicommerce.uniware.services.Unicommerce {
  private String _endpoint = null;
  private com.unicommerce.uniware.services.Unicommerce unicommerce = null;
  
  public UnicommerceProxy() {
    _initUnicommerceProxy();
  }
  
  public UnicommerceProxy(String endpoint) {
    _endpoint = endpoint;
    _initUnicommerceProxy();
  }
  
  private void _initUnicommerceProxy() {
    try {
      unicommerce = (new com.unicommerce.uniware.services.UnicommerceServiceLocator()).getUnicommerceSoap11();
      if (unicommerce != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)unicommerce)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)unicommerce)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (unicommerce != null)
      ((javax.xml.rpc.Stub)unicommerce)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.unicommerce.uniware.services.Unicommerce getUnicommerce() {
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce;
  }
  
  public com.unicommerce.uniware.services.ServiceResponse cancelSaleOrder(com.unicommerce.uniware.services.CancelSaleOrderRequest cancelSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.cancelSaleOrder(cancelSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.GetExportJobStatusResponse getExportJobStatus(com.unicommerce.uniware.services.GetExportJobStatusRequest getExportJobStatusRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.getExportJobStatus(getExportJobStatusRequest);
  }
  
  public com.unicommerce.uniware.services.SearchSaleOrderResponse searchSaleOrder(com.unicommerce.uniware.services.SearchSaleOrderRequest searchSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.searchSaleOrder(searchSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse createItemType(com.unicommerce.uniware.services.ItemTypeRequest createItemTypeRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.createItemType(createItemTypeRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrder(com.unicommerce.uniware.services.UnholdSaleOrderRequest unholdSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.unholdSaleOrder(unholdSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrderItems(com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest unholdSaleOrderItemsRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.unholdSaleOrderItems(unholdSaleOrderItemsRequest);
  }
  
  public com.unicommerce.uniware.services.CreateExportJobResponse createExportJob(com.unicommerce.uniware.services.CreateExportJobRequest createExportJobRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.createExportJob(createExportJobRequest);
  }
  
  public com.unicommerce.uniware.services.GetSaleOrderResponse getSaleOrder(com.unicommerce.uniware.services.GetSaleOrderRequest getSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.getSaleOrder(getSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse holdSaleOrderItems(com.unicommerce.uniware.services.HoldSaleOrderItemsRequest holdSaleOrderItemsRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.holdSaleOrderItems(holdSaleOrderItemsRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse editSaleOrderAddress(com.unicommerce.uniware.services.EditSaleOrderAddressRequest editSaleOrderAddressRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.editSaleOrderAddress(editSaleOrderAddressRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse createSaleOrder(com.unicommerce.uniware.services.CreateSaleOrderRequest createSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.createSaleOrder(createSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse createOrEditItemType(com.unicommerce.uniware.services.ItemTypeRequest createOrEditItemTypeRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.createOrEditItemType(createOrEditItemTypeRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse createVendorItemType(com.unicommerce.uniware.services.CreateVendorItemTypeRequest createVendorItemTypeRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.createVendorItemType(createVendorItemTypeRequest);
  }

    public com.unicommerce.uniware.services.ServiceResponse holdSaleOrder(com.unicommerce.uniware.services.HoldSaleOrderRequest holdSaleOrderRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.holdSaleOrder(holdSaleOrderRequest);
  }
  
  public com.unicommerce.uniware.services.ServiceResponse editItemType(com.unicommerce.uniware.services.ItemTypeRequest editItemTypeRequest) throws java.rmi.RemoteException{
    if (unicommerce == null)
      _initUnicommerceProxy();
    return unicommerce.editItemType(editItemTypeRequest);
  }

    @Override
    public AddPurchaseOrderResponse addPurchaseOrder(AddPurchaseOrderRequest addPurchaseOrderRequest) throws RemoteException {
        if (unicommerce == null) {
            _initUnicommerceProxy();
        }
        return unicommerce.addPurchaseOrder(addPurchaseOrderRequest);
    }
  
  
}