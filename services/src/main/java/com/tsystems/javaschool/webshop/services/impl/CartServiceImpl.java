package com.tsystems.javaschool.webshop.services.impl;

import com.tsystems.javaschool.webshop.dao.api.CartDAO;
import com.tsystems.javaschool.webshop.dao.api.ProductDAO;
import com.tsystems.javaschool.webshop.dao.entities.CartEntity;
import com.tsystems.javaschool.webshop.dao.entities.CartProductEntity;
import com.tsystems.javaschool.webshop.dao.entities.ProductEntity;
import com.tsystems.javaschool.webshop.dao.impl.CartDAOImpl;
import com.tsystems.javaschool.webshop.dao.impl.ProductDAOImpl;
import com.tsystems.javaschool.webshop.services.api.CartService;
import com.tsystems.javaschool.webshop.services.exceptions.ServiceException;
import com.tsystems.javaschool.webshop.services.util.ServiceHelper;
import com.tsystems.javaschool.webshop.services.util.ServiceHelperImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.DuplicateMappingException;

import java.util.List;

/**
 * The type Cart service.
 */
public class CartServiceImpl implements CartService {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(CartServiceImpl.class);
    /**
     * The Cart dao.
     */
    private final CartDAO cartDAO;
    /**
     * The Product dao.
     */
    private final ProductDAO productDAO;
    /**
     * The Service helper.
     */
    private final ServiceHelper serviceHelper;

    /**
     * Instantiates a new Cart service.
     */
    public CartServiceImpl() {
        cartDAO = new CartDAOImpl();
        productDAO = new ProductDAOImpl();
        serviceHelper = new ServiceHelperImpl(LOGGER);
    }

    /**
     * Instantiates a new Cart service.
     *
     * @param cartDAO       the cart dao
     * @param productDAO    the product dao
     * @param serviceHelper the service helper
     */
    public CartServiceImpl(final CartDAO cartDAO,
                           final ProductDAO productDAO,
                           final ServiceHelper serviceHelper) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.serviceHelper = serviceHelper;
    }

    @Override
    public final void add(final CartEntity cart) {
        serviceHelper.executeInTransaction(manager -> {

            cartDAO.create(cart, manager);
        });
    }

    @Override
    public final void update(final CartEntity cart) {
        serviceHelper.executeInTransaction(manager -> {

            cartDAO.update(cart, manager);
        });
    }

    @Override
    public final void delete(final Integer cartId) {
        serviceHelper.executeInTransaction(manager -> {

            cartDAO.delete(cartId, manager);
        });
    }

    @Override
    public final CartEntity get(final int cartId) {
        return serviceHelper.load(manager -> {

            return cartDAO.getById(cartId, manager);
        });
    }

    @Override
    public final List<CartEntity> getAll() {
        return serviceHelper.loadInTransaction(manager -> {

            return cartDAO.getAll(manager);
        });
    }

    @Override
    public final CartEntity addToCart(final Integer productId,
                                      final Integer quantity,
                                      final Integer cartId) {
        return serviceHelper.loadInTransaction(manager -> {
            CartEntity cart = cartDAO.getById(cartId, manager);
            ProductEntity product = productDAO.getById(productId, manager);

            CartProductEntity item = new CartProductEntity();

            item.setQuantity(quantity);
            item.setProduct(product);
            item.setCart(cart);
            item.setProductId(productId);
            item.setCartId(cart.getId());
            if (cart.getItems().contains(item)) {
                throw new ServiceException("Product already in cart");
            }
            cart.getItems().add(item);
            updateSummaryCount(cart);
            return cart;
        });
    }

    @Override
    public final CartEntity editCartProduct(final Integer productId,
                                            final Integer quantity,
                                            final Integer cartId) {
        return serviceHelper.loadInTransaction(manager -> {
            CartEntity cart = cartDAO.getById(cartId, manager);

            for (CartProductEntity cartProduct : cart.getItems()) {
                if (productId.equals(cartProduct.getProductId())) {
                    cartProduct.setQuantity(quantity);
                    break;
                }
            }
            updateSummaryCount(cart);
            return cart;
        });
    }

    @Override
    public final CartEntity removeFromCart(final Integer productId,
                                           final Integer cartId) {
        return serviceHelper.loadInTransaction(manager -> {
            cartDAO.removeFromCart(productId, cartId, manager);
            CartEntity cart = cartDAO.getById(cartId, manager);
            updateSummaryCount(cart);
            return cart;

        });
    }

    /**
     * Update summary and count of cart.
     *
     * @param cart the cart
     */
    private void updateSummaryCount(final CartEntity cart) {

        int count = 0;
        int summary = 0;
        for (CartProductEntity item : cart.getItems()) {
            count += item.getQuantity();
            summary += item.getQuantity() * item.getProduct().getPrice();
        }
        cart.setCount(count);
        cart.setSummary(summary);
    }
}

