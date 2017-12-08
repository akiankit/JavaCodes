package com.bluestone.app.uniware.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.uniware.OrderItemStatusDetail;
import com.bluestone.app.uniware.OrderItemStatusDetail.ShippingDetails;


public class CsvParseUtil {

    private static final Logger log = LoggerFactory.getLogger(CsvParseUtil.class);

    public List<OrderItemStatusDetail> execute(String url) throws Exception {
        log.info("CsvParseUtil.execute(): CSV file url={}", url);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpclient = wrapClient(httpclient);
        org.apache.http.HttpResponse response = httpclient.execute(httpGet);

/*      System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
*/
        try {
            org.apache.http.HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                File file = writeCsvResponseToFile(entity);
                List<OrderItemStatusDetail> itemStatusDetails = parseCSVFile(file);
                return itemStatusDetails;
            } else {
                log.warn("Response from http request failed. Response Code {} ", statusCode);
                throw new Exception("Response from http request failed. Response Code " + statusCode);
            }
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.abort();
                }

                if (httpclient != null) {
                    httpclient.getConnectionManager().shutdown();
                }
            } catch (Exception exception) {
                log.debug("Error: CsvParseUtil.execute(): {}", exception.getLocalizedMessage(), exception);

            }
        }
    }

    public List<OrderItemStatusDetail> parseCSVFile(File file) throws Exception {
        log.debug("CsvParseUtil.parseCSVFile(): File={}", file.getAbsolutePath());
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        CSVReader csvReader = null;
        try {
            List<OrderItemStatusDetail> orderItemStatusDetailList = new ArrayList<OrderItemStatusDetail>();
            fileReader = new FileReader(file);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            csvReader = new CSVReader(bufferedReader);
            Map<Integer, SaleOrderExportJobColumn> columnsHeaders = parseHeaders(csvReader.readNext());
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                OrderItemStatusDetail orderItemStatusDetails = parseBody(nextLine, columnsHeaders);
                orderItemStatusDetailList.add(orderItemStatusDetails);
            }
            return orderItemStatusDetailList;
        } catch (Exception e) {
            log.error("Error: CsvParseUtil.execute(): {}", e.getLocalizedMessage(), e);
            throw e;
        } finally {
            closeStream(fileReader);
            closeStream(bufferedReader);
            closeStream(csvReader);
        }
    }


    private Map<Integer, SaleOrderExportJobColumn> parseHeaders(String[] headerLine) {
        log.debug("CsvParseUtil.parseHeaders()");
        Map<Integer, SaleOrderExportJobColumn> columnMap = new HashMap<Integer, SaleOrderExportJobColumn>();
        for (int j = 0; j < headerLine.length; j++) {
            String convertedColumnName = getConvertedColumnName(headerLine[j]);
            log.debug("Column Header Name = " + convertedColumnName.toUpperCase());
            SaleOrderExportJobColumn columnHeader = SaleOrderExportJobColumn.valueOf(convertedColumnName.toUpperCase());
            columnMap.put(j, columnHeader);
        }
        return columnMap;
    }

    private OrderItemStatusDetail parseBody(String[] nextLine, Map<Integer, SaleOrderExportJobColumn> columnsHeaders) {
        log.debug("CsvParseUtil.parseBody()");
        OrderItemStatusDetail eachOrderItemStatusDetail = new OrderItemStatusDetail();
        ShippingDetails shippingDetails = eachOrderItemStatusDetail.createShippingDetails();
        for (int j = 0; j < nextLine.length; j++) {
            String content = nextLine[j];
            //log.debug("CsvParseUtil.parseBody(): Content={}", content);
            SaleOrderExportJobColumn saleOrderExportJobColumn = columnsHeaders.get(j);
            log.debug("CsvParseUtil.parseBody(): {}={}", saleOrderExportJobColumn.name(), content);
            if (saleOrderExportJobColumn != null) {
                switch (saleOrderExportJobColumn) {

                    case SALE_ORDER_ITEM_CODE:
                        eachOrderItemStatusDetail.setOrderItemCode(content);
                        break;

                    case SHIPPING_METHOD:
                        shippingDetails.setShippingMethod(content);
                        break;

                    case PACKET_NUMBER:
                        eachOrderItemStatusDetail.setPacketNumber(content);

                    case CODE:
                        eachOrderItemStatusDetail.setOrderId(content);

                    case ON_HOLD:
                        eachOrderItemStatusDetail.setOnHold(Boolean.parseBoolean(content));
                        break;

                    case SALE_ORDER_STATUS:
                        eachOrderItemStatusDetail.setOrderStatusCode(content);
                        break;

                    case SALE_ORDER_ITEM_STATUS:
                        eachOrderItemStatusDetail.setOrderItemStatusCode(content);
                        break;

                    case SHIPPING_PROVIDER:
                        shippingDetails.setShippingProvider(content);
                        break;

                    case SHIPPING_PACKAGE_CODE:
                        eachOrderItemStatusDetail.setShippingPackageCode(content);
                        break;

                    case SHIPPING_PACKAGE_CREATION_DATE:
                        Calendar shippingPackageCreatedDate = convertToDate(content);
                        if (shippingPackageCreatedDate != null) {
                            shippingDetails.setCreatedOn(shippingPackageCreatedDate);
                        }
                        break;

                    case SHIPPING_PACKAGE_STATUS_CODE:
                        shippingDetails.setStatusCode(content);
                        break;

                    case DELIVERY_TIME:
                        Calendar shippingPackageDeliveryDate = convertToDate(content);
                        if (shippingPackageDeliveryDate != null) {
                            shippingDetails.setDeliveredOn(shippingPackageDeliveryDate);
                        }
                        break;

                    case TRACKING_NUMBER:
                        shippingDetails.setTrackingNumber(content);
                        break;

                    case DISPATCH_DATE:
                        Calendar shippingPackageDispatchDate = convertToDate(content);
                        if (shippingPackageDispatchDate != null) {
                            shippingDetails.setDispatchedOn(shippingPackageDispatchDate);
                        }
                        break;
                }
            } else {
                log.debug("*************** column name is null" + saleOrderExportJobColumn + " column no:" + j);
            }
        }
        return eachOrderItemStatusDetail;
    }

    private File writeCsvResponseToFile(org.apache.http.HttpEntity entity) throws Exception {
        log.debug("CsvParseUtil.writeCsvResponseToFile()");
        String tmpDir = System.getProperty("java.io.tmpdir");
        File file = new File(tmpDir, "exportedOrderItemsFromUniware.csv");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            entity.writeTo(fos);
            return file;
        } catch (Exception e) {
            log.debug("Error: CsvParseUtil.writeCsvResponseToFile(): {}", e.getLocalizedMessage(), e);
            throw e;
        } finally {
            closeStream(fos);
        }
    }


    private void closeStream(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                if (log.isErrorEnabled()) {
                    log.error("Error while closing io stream", e);
                }
            }
        }
    }

    public String getConvertedColumnName(String name) {
        log.debug("CsvParseUtil.getConvertedColumnName(): {}", name);
        name = name.replace(" ", "_");
        name = name.replace("%", "PERCENT");
        name = name.replace(":", "_");
        name = name.replace("/", "_");
        return name;
    }

    public Calendar convertToDate(String uniwareStringDate) {
        log.debug("CsvParseUtil.convertToDate(): String Date={}", uniwareStringDate);
        if (StringUtils.isBlank(uniwareStringDate)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            Date date = sdf.parse(uniwareStringDate);
            calendar.setTime(date);
            calendar.getTime();// forcing a recompute  see the java docs.
        } catch (Exception e) {
            log.error("Error: CsvParseUtil.convertToDate():{}", e.getLocalizedMessage(), e);
            return null;
        }
        return calendar;
    }

    public HttpClient wrapClient(HttpClient base) {
        log.debug("CsvParseUtil.wrapClient()");
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(sslContext);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception exception) {
            log.debug("Error: CsvParseUtil.wrapClient(): {} ", exception.getLocalizedMessage(), exception);
            return null;
        }
    }

}
