/**
 * UnicommerceSoap11Stub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class UnicommerceSoap11Stub extends org.apache.axis.client.Stub implements com.unicommerce.uniware.services.Unicommerce {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[16];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CancelSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CancelSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CancelSaleOrderRequest"), com.unicommerce.uniware.services.CancelSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CancelSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExportJobStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GetExportJobStatusRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetExportJobStatusRequest"), com.unicommerce.uniware.services.GetExportJobStatusRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetExportJobStatusResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.GetExportJobStatusResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GetExportJobStatusResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SearchSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SearchSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderRequest"), com.unicommerce.uniware.services.SearchSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.SearchSaleOrderResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SearchSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateItemType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateItemTypeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemTypeRequest"), com.unicommerce.uniware.services.ItemTypeRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateItemTypeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UnholdSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnholdSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">UnholdSaleOrderRequest"), com.unicommerce.uniware.services.UnholdSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnholdSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UnholdSaleOrderItems");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnholdSaleOrderItemsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">UnholdSaleOrderItemsRequest"), com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnholdSaleOrderItemsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateExportJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateExportJobRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateExportJobRequest"), com.unicommerce.uniware.services.CreateExportJobRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateExportJobResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.CreateExportJobResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateExportJobResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GetSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetSaleOrderRequest"), com.unicommerce.uniware.services.GetSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetSaleOrderResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.GetSaleOrderResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GetSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("HoldSaleOrderItems");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "HoldSaleOrderItemsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">HoldSaleOrderItemsRequest"), com.unicommerce.uniware.services.HoldSaleOrderItemsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "HoldSaleOrderItemsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EditSaleOrderAddress");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "EditSaleOrderAddressRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">EditSaleOrderAddressRequest"), com.unicommerce.uniware.services.EditSaleOrderAddressRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "EditSaleOrderAddressResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateSaleOrderRequest"), com.unicommerce.uniware.services.CreateSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateOrEditItemType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateOrEditItemTypeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemTypeRequest"), com.unicommerce.uniware.services.ItemTypeRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateOrEditItemTypeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateVendorItemType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateVendorItemTypeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateVendorItemTypeRequest"), com.unicommerce.uniware.services.CreateVendorItemTypeRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreateVendorItemTypeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddPurchaseOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddPurchaseOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">AddPurchaseOrderRequest"), com.unicommerce.uniware.services.AddPurchaseOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">AddPurchaseOrderResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.AddPurchaseOrderResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddPurchaseOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("HoldSaleOrder");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "HoldSaleOrderRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">HoldSaleOrderRequest"), com.unicommerce.uniware.services.HoldSaleOrderRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "HoldSaleOrderResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EditItemType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "EditItemTypeRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemTypeRequest"), com.unicommerce.uniware.services.ItemTypeRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
        oper.setReturnClass(com.unicommerce.uniware.services.ServiceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "EditItemTypeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

    }

    public UnicommerceSoap11Stub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public UnicommerceSoap11Stub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public UnicommerceSoap11Stub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem>ShippingMethodCode");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItemShippingMethodCode.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CancelSaleOrderRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CreateExportJobRequest>ExportFilters>ExportFilter>DateRange");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterDateRange.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CreateExportJobRequest>ExportFilters>ExportFilter>SelectedValues");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterSelectedValues.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>ShippingPackages>ShippingPackage");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>HoldSaleOrderItemsRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>UnholdSaleOrderItemsRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>CancelSaleOrderRequest>SaleOrder>SaleOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CancelSaleOrderRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>CreateExportJobRequest>ExportFilters>ExportFilter");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>GetSaleOrderResponse>SaleOrder>Addresses");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Address[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>GetSaleOrderResponse>SaleOrder>SaleOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>GetSaleOrderResponse>SaleOrder>ShippingPackages");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>ShippingPackages>ShippingPackage");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingPackage");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>HoldSaleOrderItemsRequest>SaleOrder>SaleOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>HoldSaleOrderItemsRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>SearchSaleOrderResponse>SaleOrders>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>UnholdSaleOrderItemsRequest>SaleOrder>SaleOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>UnholdSaleOrderItemsRequest>SaleOrder>SaleOrderItems>SaleOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>AddPurchaseOrderRequest>PurchaseOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.PurchaseOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>AddPurchaseOrderResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>AddPurchaseOrderResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>CancelSaleOrderRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>CreateExportJobRequest>ExportColumns");
        cachedSerQNames.add(qName);
        cls = java.lang.String[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportColumn");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>CreateExportJobRequest>ExportFilters");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>CreateExportJobRequest>ExportFilters>ExportFilter");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportFilter");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>CreateExportJobResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>CreateExportJobResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetExportJobStatusResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetExportJobStatusResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetSaleOrderRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetSaleOrderResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetSaleOrderResponse>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetSaleOrderResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>HoldSaleOrderItemsRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderItemsRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>HoldSaleOrderRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>SearchSaleOrderRequest>SearchOptions");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SearchSaleOrderRequestSearchOptions.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>SearchSaleOrderResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>SearchSaleOrderResponse>SaleOrders");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>SearchSaleOrderResponse>SaleOrders>SaleOrder");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrder");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>SearchSaleOrderResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>UnholdSaleOrderItemsRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>UnholdSaleOrderRequest>SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderRequestSaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">AddPurchaseOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.AddPurchaseOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">AddPurchaseOrderResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.AddPurchaseOrderResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CancelSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CancelSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateExportJobRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateExportJobResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateExportJobResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateVendorItemTypeRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CreateVendorItemTypeRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CustomFields>CustomField");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CustomFieldsCustomField.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">EditSaleOrderAddressRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.EditSaleOrderAddressRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetExportJobStatusRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetExportJobStatusRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetExportJobStatusResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetExportJobStatusResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">GetSaleOrderResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.GetSaleOrderResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">HoldSaleOrderItemsRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderItemsRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">HoldSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.HoldSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ItemTypeRequest>ShipTogether");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.ItemTypeRequestShipTogether.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(enumsf);
        cachedDeserFactories.add(enumdf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ItemTypeRequest>Tags");
        cachedSerQNames.add(qName);
        cls = java.lang.Object[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Tag");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SaleOrder>Addresses");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Address[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SaleOrder>SaleOrderItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrderItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SaleOrder>ShippingProviders");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.ShippingProvider[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SaleOrderAddress>Addresses");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Address[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SaleOrderAddress>SaleOrderAddressItems");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrderAddressItem[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SearchSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SearchSaleOrderResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ServiceResponse>Errors");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ServiceResponse>Warnings");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">UnholdSaleOrderItemsRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">UnholdSaleOrderRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.UnholdSaleOrderRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Address.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.AddressRef.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomFields");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.CustomFieldsCustomField[].class;
        cachedSerClasses.add(cls);
        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CustomFields>CustomField");
        qName2 = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomField");
        cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
        cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Error.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemTypeRequest");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.ItemTypeRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.PurchaseOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrder");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrder.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddress");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrderAddress.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrderAddressItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.SaleOrderItem.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.ServiceResponse.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.ShippingProvider.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

        qName = new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning");
        cachedSerQNames.add(qName);
        cls = com.unicommerce.uniware.services.Warning.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                    cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                    cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                    cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse cancelSaleOrder(com.unicommerce.uniware.services.CancelSaleOrderRequest cancelSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CancelSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {cancelSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.GetExportJobStatusResponse getExportJobStatus(com.unicommerce.uniware.services.GetExportJobStatusRequest getExportJobStatusRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetExportJobStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getExportJobStatusRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.GetExportJobStatusResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.GetExportJobStatusResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.GetExportJobStatusResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.SearchSaleOrderResponse searchSaleOrder(com.unicommerce.uniware.services.SearchSaleOrderRequest searchSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SearchSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {searchSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.SearchSaleOrderResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.SearchSaleOrderResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.SearchSaleOrderResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse createItemType(com.unicommerce.uniware.services.ItemTypeRequest createItemTypeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateItemType"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createItemTypeRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrder(com.unicommerce.uniware.services.UnholdSaleOrderRequest unholdSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "UnholdSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {unholdSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse unholdSaleOrderItems(com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest unholdSaleOrderItemsRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "UnholdSaleOrderItems"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {unholdSaleOrderItemsRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.CreateExportJobResponse createExportJob(com.unicommerce.uniware.services.CreateExportJobRequest createExportJobRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateExportJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createExportJobRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.CreateExportJobResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.CreateExportJobResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.CreateExportJobResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.GetSaleOrderResponse getSaleOrder(com.unicommerce.uniware.services.GetSaleOrderRequest getSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.GetSaleOrderResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.GetSaleOrderResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.GetSaleOrderResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse holdSaleOrderItems(com.unicommerce.uniware.services.HoldSaleOrderItemsRequest holdSaleOrderItemsRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "HoldSaleOrderItems"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {holdSaleOrderItemsRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse editSaleOrderAddress(com.unicommerce.uniware.services.EditSaleOrderAddressRequest editSaleOrderAddressRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "EditSaleOrderAddress"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {editSaleOrderAddressRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse createSaleOrder(com.unicommerce.uniware.services.CreateSaleOrderRequest createSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse createOrEditItemType(com.unicommerce.uniware.services.ItemTypeRequest createOrEditItemTypeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateOrEditItemType"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createOrEditItemTypeRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse createVendorItemType(com.unicommerce.uniware.services.CreateVendorItemTypeRequest createVendorItemTypeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateVendorItemType"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {createVendorItemTypeRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.AddPurchaseOrderResponse addPurchaseOrder(com.unicommerce.uniware.services.AddPurchaseOrderRequest addPurchaseOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "AddPurchaseOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {addPurchaseOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.AddPurchaseOrderResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.AddPurchaseOrderResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.AddPurchaseOrderResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse holdSaleOrder(com.unicommerce.uniware.services.HoldSaleOrderRequest holdSaleOrderRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "HoldSaleOrder"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {holdSaleOrderRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    public com.unicommerce.uniware.services.ServiceResponse editItemType(com.unicommerce.uniware.services.ItemTypeRequest editItemTypeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "EditItemType"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {editItemTypeRequest});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException)_resp;
            }
            else {
                extractAttachments(_call);
                try {
                    return (com.unicommerce.uniware.services.ServiceResponse) _resp;
                } catch (java.lang.Exception _exception) {
                    return (com.unicommerce.uniware.services.ServiceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.unicommerce.uniware.services.ServiceResponse.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
