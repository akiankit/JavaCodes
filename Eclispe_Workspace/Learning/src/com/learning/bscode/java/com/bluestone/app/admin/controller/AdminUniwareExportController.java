package com.bluestone.app.admin.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.admin.service.SaleOrderExportJobService;
import com.bluestone.app.order.service.UniwareSynchroniser;
import com.bluestone.app.uniware.OrderItemStatusDetail;
import com.bluestone.app.uniware.util.CsvParseUtil;

@Controller
@RequestMapping("/admin/uniware*")
public class AdminUniwareExportController {

    @Autowired
    SaleOrderExportJobService saleOrderExportJobService;

    @Autowired
    UniwareSynchroniser uniwareSynchroniser;


    private static final Logger log = LoggerFactory.getLogger(AdminUniwareExportController.class);

    @RequestMapping(value = "/one", method = RequestMethod.GET)
    public void executeOneTimeAllImport() {
        log.info("AdminUniwareExportController.executeOneTimeAllImport()");
        String dir = "/tmp";
        File file = new File(dir, "orderItemStatusDetails.csv");
        log.debug("AdminUniwareExportController.executeOneTimeAllImport() from Reading file={}", file.getAbsolutePath());

        List<OrderItemStatusDetail> orderItemStatusDetails = new LinkedList<OrderItemStatusDetail>();
        CsvParseUtil csvParseUtil = new CsvParseUtil();
        //String pathname = "D:\\tmp\\export\\with-vivek\\481_ef8e2820c5cc0d4f9488bf683bae295e.csv";

        try {
            orderItemStatusDetails = csvParseUtil.parseCSVFile(file);
        } catch (Exception exception) {
            log.debug("Error: AdminUniwareExportController.executeOneTimeImport into OrderItemDB(): ");
            //throw new RuntimeException("Fix this", exception);
        } finally {
            try {
                boolean deletionSuccess = FileUtils.deleteQuietly(file);
                log.info("AdminUniwareExportController.executeOneTimeAllImport(): File {} deleted={}.", file.getAbsolutePath(), deletionSuccess);
            } catch (Exception exception) {
                log.debug("Error: AdminUniwareExportController.executeOneTimeAllImport(): Failed to delete the file.", exception);

            }
        }

        List<OrderItemStatusDetail> listOf500 = new LinkedList<OrderItemStatusDetail>();
        for (int i = 0; i < orderItemStatusDetails.size(); i++) {
            log.info("i={}", i);
            OrderItemStatusDetail orderItemStatusDetail = orderItemStatusDetails.get(i);
            listOf500.add(orderItemStatusDetail);
            if ((i % 500) < 499) {
                continue;
            }
            uniwareSynchroniser.updateDatabase(listOf500);
            listOf500.clear();
        }

        uniwareSynchroniser.updateDatabase(listOf500);
    }


    @RequestMapping(value = "/exportww", method = RequestMethod.GET)
    public
    @ResponseBody
    String execute(ModelMap modelMap) {
        long waitTimeMinute = 2;
        int sinceNoOfDays = 2;
        String cvsResultFile;
        try {
            cvsResultFile = saleOrderExportJobService.execute(SaleOrderExportJobService.Frequency.ONETIME, waitTimeMinute, sinceNoOfDays);
        } catch (RemoteException e) {
            log.debug("Error: AdminUniwareExportController.execute():{} ", e.getLocalizedMessage(), e);
            throw new RuntimeException("Fix this", e);
        }

        List<OrderItemStatusDetail> orderItemStatusDetails = new LinkedList<OrderItemStatusDetail>();
        if (cvsResultFile != null) {
            CsvParseUtil csvParseUtil = new CsvParseUtil();
            try {
                orderItemStatusDetails = csvParseUtil.execute(cvsResultFile);
            } catch (Exception exception) {
                log.debug("Error: AdminUniwareExportController.execute(): ");
                throw new RuntimeException("Fix this", exception);
            }
        }

        uniwareSynchroniser.updateDatabase(orderItemStatusDetails);

        return null;
    }

}
