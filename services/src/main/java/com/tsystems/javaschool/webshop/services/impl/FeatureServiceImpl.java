package com.tsystems.javaschool.webshop.services.impl;

import com.tsystems.javaschool.webshop.dao.api.FeatureDAO;
import com.tsystems.javaschool.webshop.dao.entities.FeatureEntity;
import com.tsystems.javaschool.webshop.dao.entities.ProductFeatureEntity;
import com.tsystems.javaschool.webshop.dao.impl.FeatureDAOImpl;
import com.tsystems.javaschool.webshop.services.api.FeatureService;
import com.tsystems.javaschool.webshop.services.util.ServiceHelper;
import com.tsystems.javaschool.webshop.services.util.ServiceHelperImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * The type Feature service.
 */
public class FeatureServiceImpl implements FeatureService {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(FeatureServiceImpl.class);

    /**
     * The Feature dao.
     */
    private final FeatureDAO featureDAO;
    /**
     * The Service helper.
     */
    private final ServiceHelper serviceHelper;

    /**
     * Instantiates a new Feature service.
     */
    public FeatureServiceImpl() {
        featureDAO = new FeatureDAOImpl();
        serviceHelper = new ServiceHelperImpl(LOGGER);
    }

    /**
     * Instantiates a new Feature service.
     *
     * @param featureDAO    the feature dao
     * @param serviceHelper the service helper
     */
    public FeatureServiceImpl(final FeatureDAO featureDAO,
                              final ServiceHelper serviceHelper) {
        this.featureDAO = featureDAO;
        this.serviceHelper = serviceHelper;
    }

    @Override
    public final void add(final FeatureEntity feature) {
        serviceHelper.executeInTransaction(manager -> {
            featureDAO.create(feature, manager);
        });
    }

    @Override
    public final void update(final FeatureEntity feature) {
        serviceHelper.executeInTransaction(manager -> {
            featureDAO.update(feature, manager);
        });
    }

    @Override
    public final void delete(final Integer featureId) {
        serviceHelper.executeInTransaction(manager -> {
            featureDAO.delete(featureId, manager);
        });
    }
    @Override
    public final FeatureEntity get(final int featureId) {
        return serviceHelper.loadInTransaction(manager ->
                featureDAO.getById(featureId, manager));
    }
    @Override
    public final List<FeatureEntity> getAll() {
        return serviceHelper.loadInTransaction(manager ->
                featureDAO.getAll(manager));
    }

    @Override
    public final List<ProductFeatureEntity> getAllCategoryValues() {
        return serviceHelper.loadInTransaction(manager ->
                featureDAO.getAllValues(manager));
    }
}
