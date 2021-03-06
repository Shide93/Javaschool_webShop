package com.tsystems.javaschool.webshop.services.api;

import com.tsystems.javaschool.webshop.dao.entities.*;
import com.tsystems.javaschool.webshop.dao.entities.Address;
import com.tsystems.javaschool.webshop.dao.entities.enums.OrderStatus;

import java.util.List;

/**
 * Service provides checkout logic.
 */
public interface OrderService {

    /**
     * Gets payment types.
     *
     * @return the payment types
     */
    List<Payment> getPaymentTypes();

    /**
     * Gets shipping types.
     *
     * @return the shipping types
     */
    List<Shipping> getShippingTypes();

    /**
     * Create order.
     *
     * @param order the order
     * @param cart  the cart
     */
    void createOrder(Order order,
                     Cart cart);

    Order get(int orderId);

    List<Order> getAll();

    void changeStatus(int id, OrderStatus status);

    List<Order> getAllByUser(int userId);
}
