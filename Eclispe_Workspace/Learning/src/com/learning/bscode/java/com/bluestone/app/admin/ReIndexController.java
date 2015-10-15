package com.bluestone.app.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.admin.service.ReindexService;

@Controller
@RequestMapping("/admin/reindex*")
public class ReIndexController {
    
    private static final Logger log = LoggerFactory.getLogger(ReIndexController.class);
    
    @Autowired
    private ReindexService reindexService;
    
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
	@RequiresPermissions("admin:reindex")
    public @ResponseBody String reindex(HttpServletRequest httpServletRequest) {
        log.info("Reindex job for all initiated");
        reindexService.reindex();
        log.info("Reindex job for all done");
        return "done";
    }
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
	@RequiresPermissions("search:edit")
    public @ResponseBody String reindexProducts(HttpServletRequest httpServletRequest) {
        log.info("Reindex job for products initiated");
        reindexService.reindexProducts();
        return "done";
    }
    
}
