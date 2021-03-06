package com.tsystems.javaschool.webshop.services.impl;

import com.tsystems.javaschool.webshop.dao.api.OrderDAO;
import com.tsystems.javaschool.webshop.dao.api.ProductDAO;
import com.tsystems.javaschool.webshop.dao.api.UsersDAO;
import com.tsystems.javaschool.webshop.dao.entities.Product;
import com.tsystems.javaschool.webshop.dao.entities.User;
import com.tsystems.javaschool.webshop.dao.entities.dto.ProductDTO;
import com.tsystems.javaschool.webshop.dao.entities.dto.StatisticsDTO;
import com.tsystems.javaschool.webshop.dao.entities.dto.UserDTO;
import com.tsystems.javaschool.webshop.dao.entities.enums.OrderStatus;
import com.tsystems.javaschool.webshop.services.api.AccountService;
import com.tsystems.javaschool.webshop.services.api.StatisticsService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.AssertTrue;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by Shide on 03.03.2016.
 */
@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    /**
     * The Order dao.
     */
    @Autowired
    private OrderDAO orderDAO;
    /**
     * The Product dao.
     */
    @Autowired
    private ProductDAO productDAO;
    /**
     * The Users dao.
     */
    @Autowired
    private UsersDAO usersDAO;

    @Override
    public final int newOrders() {
        return orderDAO.getOrderCountByStatus(OrderStatus.NEW, new Date(0));
    }

    @Override
    public final long totalSales() {
        return orderDAO.totalSales();
    }

    @Override
    public final long periodSales(final Date dateFrom) {
        return orderDAO.periodSales(dateFrom);
    }

    @Override
    public final List<Product> topProducts(final int count) {
        return productDAO.topProducts(count, new Date(0));
    }

    @Override
    public final List<User> topCustomers(final int count) {
        return orderDAO.topCustomers(count, new Date(0));
    }

    @Override
    public final StatisticsDTO getShopReport(final Date dateFrom,
                                       final Integer topProductsCount,
                                       final Integer topUsersCount) {
        StatisticsDTO report = new StatisticsDTO();

        report.setTotalSales(orderDAO.totalSales());

        report.setPeriodSales(orderDAO.periodSales(dateFrom));

        report.setTotalOrders(orderDAO.totalOrders());

        for (OrderStatus status : OrderStatus.values()) {
            report.getOrdersPerStatus().put(status,
                    orderDAO.getOrderCountByStatus(status, dateFrom));
        }

        for (User user : orderDAO.topCustomers(topUsersCount, dateFrom)) {
            report.getTopUsers().add(new UserDTO(user));
        }

        for (Product product
                : productDAO.topProducts(topProductsCount, dateFrom)) {
            ProductDTO productDTO = new ProductDTO(product);
            productDTO.setTotalSales(productDAO
                    .getProductSales(product.getId(), dateFrom));
            report.getTopProducts().add(productDTO);
        }
        return report;
    }

    @Override
    public final String generateAccessToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Sets order dao.
     *
     * @param dao the dao
     */
    public final void setOrderDAO(final OrderDAO dao) {
        this.orderDAO = dao;
    }

    /**
     * Sets product dao.
     *
     * @param dao the dao
     */
    public final void setProductDAO(final ProductDAO dao) {
        this.productDAO = dao;
    }

    /**
     * Sets users dao.
     *
     * @param dao the dao
     */
    public final void setUsersDAO(final UsersDAO dao) {
        this.usersDAO = dao;
    }

    /**
     * Gets bla.
     *
     * @return the bla
     */
    public String getBla() {
        return "bla";
    }
}
