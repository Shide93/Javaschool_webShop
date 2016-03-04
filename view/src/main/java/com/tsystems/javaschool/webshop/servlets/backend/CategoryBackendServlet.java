package com.tsystems.javaschool.webshop.servlets.backend;

import com.tsystems.javaschool.webshop.dao.entities.CategoryEntity;
import com.tsystems.javaschool.webshop.services.api.CategoryService;
import com.tsystems.javaschool.webshop.services.api.ValidationService;
import com.tsystems.javaschool.webshop.services.impl.CategoryServiceImpl;
import com.tsystems.javaschool.webshop.services.impl.ValidationServiceImpl;
import com.tsystems.javaschool.webshop.servlets.SaveProfileServlet;
import com.tsystems.javaschool.webshop.servlets.utils.ServletUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Shide on 28.02.2016.
 */
public class CategoryBackendServlet extends HttpServlet {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(SaveProfileServlet.class);
    /**
     * The Category service.
     */
    private final CategoryService categoryService;
    /**
     * The Validation service.
     */
    private final ValidationService validationService;

    /**
     * Instantiates a new Category backend servlet.
     */
    public CategoryBackendServlet() {
        categoryService = new CategoryServiceImpl();
        validationService = new ValidationServiceImpl();
    }
    @Override
    protected final void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        String categoryIdStr = req.getParameter("categoryId");

        if (categoryIdStr == null) {        //when enter page without params
            List<CategoryEntity> categories =
                    (List<CategoryEntity>) getServletContext()
                            .getAttribute("categoryList");
            if (categories != null && categories.size() > 0) {
                Integer categoryId = categories.get(0).getId();
                req.setAttribute("selectedCategory",
                        categoryService.get(categoryId));
            }

        } else {
            Integer categoryId = Integer.parseInt(categoryIdStr);
            if (categoryId > 0) {                       //when choose category in sidebar
                req.setAttribute("selectedCategory",
                        categoryService.get(categoryId));
            }
        }
        req.getRequestDispatcher("/backend/categories.jsp")
                .forward(req, resp);
    }

    @Override
    protected final void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        if (action.equals("add")) {
            CategoryEntity category = new CategoryEntity();
            category.setName(name);
            category.setDescription(description);
            categoryService.add(category);
            resp.sendRedirect(resp.encodeRedirectURL(
                    "/backend/categories?categoryId="
                            + category.getId()));
        } else if (action.equals("save")) {
            Integer id = Integer.parseInt(idStr);
            CategoryEntity category = new CategoryEntity();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);
            categoryService.update(category);
            resp.sendRedirect(resp.encodeRedirectURL(
                    "/backend/categories?categoryId="
                            + category.getId()));
        } else if (action.equals("remove")) {
            Integer id = Integer.parseInt(idStr);
            try {
                categoryService.delete(id);
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
                req.setAttribute("cantRemove",
                        "Can't remove category: Category has products");
                req.getRequestDispatcher("/backend/cantRemove.jsp")
                        .forward(req, resp);
                return;
            }
            resp.sendRedirect(resp.encodeRedirectURL("/backend/categories"));
        }
        //refresh category list in sidebar
        ServletUtils.setCategoryListToContext(this.getServletContext());
    }
}
