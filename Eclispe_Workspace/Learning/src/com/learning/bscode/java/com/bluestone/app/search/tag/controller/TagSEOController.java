package com.bluestone.app.search.tag.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagSEO;
import com.bluestone.app.search.tag.service.TagSEOService;
import com.bluestone.app.search.tag.service.TagService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/admin/search/tag/tagseo*")
public class TagSEOController {

	private static final String TAGSEO_LIST_URL = "/admin/search/tag/tagseo/list";

	private static final String TAGSEO = "tagseo";

	private static final String TAGSEO_LIST_VIEW = "tagseoList";

	private static final String TAGSEO_DETAILS_VIEW = "tagseoDetails";	
	
    private static final Logger log = LoggerFactory.getLogger(TagSEOController.class);

    @Autowired
    private TagSEOService tagseoService;
    
    @Autowired
    private TagService tagService;
    
    public void handleErrors(ModelAndView modelAndView, String ... errorMessages) {
    	List<String> errorList = Arrays.asList(errorMessages);
    	handleErrors(modelAndView, errorList);
    }    
    
    public void handleErrors(ModelAndView modelAndView, List errorsList) {
        log.error("Error for view {} -  {}" , modelAndView.getViewName(), errorsList);
        modelAndView.addObject("errors", errorsList);
    }
    
    @InitBinder
	public void initBinder(WebDataBinder binder) {
		log.trace("BaseController.initBinder()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Set.class, "tags",new CustomCollectionEditor(Set.class){

            @Override
            protected Object convertElement(Object element) {
                Tag tag = null;

                if (element != null) {
                    Long id = Long.valueOf(element.toString());
                    tag = tagService.findAny(Tag.class, id);
                }
                return tag;
            }
        });
	}
    

    @RequestMapping(value = "/add*", method = RequestMethod.GET)
    @RequiresPermissions("tagseo:add")
    public ModelAndView showTagSEOCreateForm() {
        log.debug("TagSEOController.showTagSEOCreateForm()");
        ModelAndView modelAndView = new ModelAndView(TAGSEO_DETAILS_VIEW);
        modelAndView.addObject("tagsList", tagService.getAllTags());
        fillModelForView(modelAndView, new TagSEO(), false);
        return modelAndView;
    }
    
    @RequestMapping(value = "/edit/{tagseoId}", method = RequestMethod.GET)
    @RequiresPermissions("tagseo:edit")
    public ModelAndView showTagSEOUpdateForm(@PathVariable("tagseoId") Long tagseoId) {
        log.debug("TagSEOController.showTagSEOUpdateForm(): TagSEO Id = {}", tagseoId);
        TagSEO tagseo = tagseoService.find(TagSEO.class,tagseoId,true);
        ModelAndView modelAndView = new ModelAndView(TAGSEO_DETAILS_VIEW);
        modelAndView.addObject("tagsList", tagService.getAllTags());
        fillModelForView(modelAndView, tagseo, true);
        return modelAndView;
    }
    
    private void fillModelForView(ModelAndView modelAndView, TagSEO tagseo, boolean isEditView) {
        modelAndView.addObject(TAGSEO, tagseo);
                                                                        }

    @RequestMapping(value = "/create*", method = RequestMethod.POST)
    @RequiresPermissions("tagseo:add")
    public ModelAndView createTagSEO(@ModelAttribute("tagseo") @Valid TagSEO tagseo,BindingResult bindingResult,HttpServletResponse httpServletResponse) {
        log.debug("TagSEOController.createTagSEO()");
        ModelAndView modelAndView = new ModelAndView(TAGSEO_DETAILS_VIEW);
        fillModelForView(modelAndView, tagseo, false);
		if (bindingResult.hasErrors()) {
			handleErrors(modelAndView, bindingResult.getAllErrors());
		} else {
			try {
				tagseoService.create(tagseo);
				httpServletResponse.sendRedirect(Util.getContextPath() + TAGSEO_LIST_URL);
			} catch (Exception e) {
				String errorMessage = "Some problem occurred while creating tagseo";
				log.error("{} : Reason {} ", errorMessage, e.getMessage(), e);
				handleErrors(modelAndView, errorMessage);
			}
		}
		return modelAndView;
    }
    
    @RequestMapping(value = "/update*", method = RequestMethod.POST)
    @RequiresPermissions("tagseo:edit")
    public ModelAndView updateTagSEO(@ModelAttribute("tagseo") @Valid TagSEO tagseo,BindingResult bindingResult,HttpServletResponse httpServletResponse) {
        log.debug("TagSEOController.editTagSEO()");
        ModelAndView modelAndView = new ModelAndView(TAGSEO_DETAILS_VIEW);
		fillModelForView(modelAndView, tagseo, true);
		if (bindingResult.hasErrors()) {
			handleErrors(modelAndView, bindingResult.getAllErrors());
		} else {
			try {
				tagseoService.update(tagseo);
				httpServletResponse.sendRedirect(Util.getContextPath() + TAGSEO_LIST_URL);
			} catch (Exception e) {
				String errorMessage = "Some problem occurred while updating tagseo";
				log.error("{} : Reason {} ", errorMessage, e.getMessage(), e);
				handleErrors(modelAndView, errorMessage);
			}
		}
		return modelAndView;
    }
    
    @RequestMapping(value = "/list*", method = RequestMethod.GET)
    @RequiresPermissions("tagseo:view")
    public ModelAndView listTagSEOs(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria) {
        log.debug("TagSEOController.listTagSEOs()");
        ModelAndView modelAndView = new ModelAndView(TAGSEO_LIST_VIEW);
        try {
            Map<String, Object> tagseoDetails = tagseoService.getTagSEOPaginatedList(filterCriteria, true);            
            modelAndView.addAllObjects(tagseoDetails);            
        } catch (Exception e) {
            log.error("Error during TagSEOController.listTagSEOs(): Reason: {}", e.toString(), e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/disabledlist*", method = RequestMethod.GET)
    @RequiresPermissions("tagseo:view")
    public ModelAndView disabledTagSEOListTagSEOs(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria) {
        log.debug("TagSEOController.disabledListTagSEOs()");
        ModelAndView modelAndView = new ModelAndView(TAGSEO_LIST_VIEW);
        try {
            Map<String, Object> tagseoDetails = tagseoService.getTagSEOPaginatedList(filterCriteria, false);
            modelAndView.addAllObjects(tagseoDetails);
        } catch (Exception e) {
            log.error("Error during TagSEOController.disabledlistTagSEOs(): Reason: {}", e.toString(), e);
        }
        modelAndView.addObject("isDisabledList", true);
		return modelAndView;
    }

    @RequestMapping(value = "/disable/{tagseoId}", method = RequestMethod.POST)
    @RequiresPermissions("tagseo:delete")
    public @ResponseBody String disableTagSEO(@PathVariable("tagseoId") Long tagseoId) {
        log.debug("TagSEOController.disableTagSEO(): TagSEO Id = {}", tagseoId);
        boolean hasError = false;
        String message = "TagSEO disabled successfully.";
        try {
            TagSEO tagseo = tagseoService.find(TagSEO.class,tagseoId,true);
            tagseoService.markEntityAsDisabled(tagseo);
        } catch (Exception e) {
            hasError = true;
            message = "An error occurred while disabling tagseo.";
            log.error("{} : Reason {} ", message, e.getMessage(), e);
        }
        Map<String, Object> response = new HashMap<String, Object>();
        response.put(Constants.HAS_ERROR, hasError);
        response.put(Constants.MESSAGE, message);
        response.put("redirectUrl", Util.getContextPath() + TAGSEO_LIST_URL);
        return new Gson().toJson(response);
    }
    
    @RequestMapping(value = "/enable/{tagseoId}", method = RequestMethod.POST)
    public @ResponseBody String enableTagSEO(@PathVariable(value="tagseoId") Long tagseoId) {
        log.debug("TagSEOController.enableTagSEO() for tagseo id {}",tagseoId);
        boolean hasError = false;
        String message = "TagSEO enabled successfully.";
        try {
            TagSEO tagseo = tagseoService.find(TagSEO.class,tagseoId,false);
            tagseoService.markEntityAsEnabled(tagseo);
        } catch (Exception e) {
            hasError = true;
            message = "An error occurred while enabling tagseo.";
            log.error("{} : Reason {} ", message, e.getMessage(), e);
        }
        Map<String, Object> response = new HashMap<String, Object>();
        response.put(Constants.HAS_ERROR, hasError);
        response.put(Constants.MESSAGE, message);
        response.put("redirectUrl", Util.getContextPath() + TAGSEO_LIST_URL);
        return new Gson().toJson(response);
    }
    
}
