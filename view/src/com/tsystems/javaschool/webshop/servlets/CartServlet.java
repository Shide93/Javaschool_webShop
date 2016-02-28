package com.tsystems.javaschool.webshop.servlets;

import com.tsystems.javaschool.webshop.dao.entities.CartEntity;
import com.tsystems.javaschool.webshop.services.api.CartService;
import com.tsystems.javaschool.webshop.services.exceptions.ServiceException;
import com.tsystems.javaschool.webshop.services.impl.CartServiceImpl;
import com.tsystems.javaschool.webshop.servlets.utils.ServletUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cart servlet that manages all cart manipulations.
 */
public class CartServlet extends HttpServlet {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(CartServlet.class);

    /**
     * The Cart service.
     */
    private CartService cartService;

    /**
     * Instantiates a new Cart servlet.
     */
    public CartServlet() {
        cartService = new CartServiceImpl();
    }

    @Override
    protected final void doGet(final HttpServletRequest req,
                               final HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("cart.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected final void doPost(final HttpServletRequest req,
                                final HttpServletResponse resp)
            throws ServletException, IOException {

        String productIdStr = req.getParameter("product_id");
        String quantityStr = req.getParameter("quantity");
        Integer quantity;
        Integer productId;
        try {
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            LOGGER.warn("invalid data format", e);
            //TODO: error JSON?
            return;
        }
        createCartIfNone(req, resp);
        CartEntity cart = (CartEntity) req.getSession()
                .getAttribute("cart");


            if (req.getParameter("action").equals("add")) {

                CartEntity newCart = cartService.addToCart(productId, quantity, cart.getId());
                req.getSession().setAttribute("cart", newCart);
            } else if (req.getParameter("action").equals("edit")) {

                    CartEntity newCart = cartService.editCartProduct(productId, quantity, cart.getId());
                    req.getSession().setAttribute("cart", newCart);
                    //TODO: error JSON?
            } else if (req.getParameter("action").equals("remove")) {

                    CartEntity newCart = cartService.removeFromCart(productId, cart.getId());
                    req.getSession().setAttribute("cart", newCart);
            }

    }

    /**
     * Checks cart for existing and creates if not exist then adds it to session.
     *
     * @param req  the request object
     * @param resp the response object
     */
    public final void createCartIfNone(final HttpServletRequest req,
                                 final HttpServletResponse resp) {

        //check if cart exists in session or create it
        CartEntity cart = (CartEntity) req.getSession()
                .getAttribute("cart");

        if (cart == null) {

            cart = new CartEntity();
            //TODO: cookie creation function, based on hashing  something

            cartService.add(cart);
            req.getSession().setAttribute("cart", cart);
            //TODO: cookie create function

            resp.addCookie(ServletUtils.createCookie("cartID", "" + cart.getId()));
        }
    }
}