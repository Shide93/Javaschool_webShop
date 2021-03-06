package com.tsystems.javaschool.webshop.controllers.webservice;

import com.tsystems.javaschool.webshop.controllers.utils.ControllerUtils;
import com.tsystems.javaschool.webshop.dao.entities.dto.StatisticsDTO;
import com.tsystems.javaschool.webshop.services.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Shide on 23.03.2016.
 */
@RestController
@RequestMapping(value = "/api")
public class StatisticsRestController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public final StatisticsDTO getRestReport(@RequestParam(required = false,
                                                defaultValue = "01-01-1970")
                                             @DateTimeFormat
                                                     (pattern = "dd-MM-yyyy")
                                             final Date  dateFrom,
                                             @RequestParam
                                             final Integer topProductsCount,
                                             @RequestParam
                                             final Integer topUsersCount,
                                             @RequestParam
                                             final String accessToken) {
        if (!ControllerUtils.isTokenExists(accessToken, servletContext)) {
            throw new IllegalArgumentException("Wrong token value");
        }
        return statisticsService.getShopReport(dateFrom,
                    topProductsCount,
                    topUsersCount);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public final String handleBadRequest(final HttpServletRequest req,
                                           final Exception e) {
        return "Error: " + e.getMessage();
    }
}
