package com.tsystems.javaschool.webshop.dao.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Shide on 22.02.2016.
 */
@Entity
@Table(name = "product_feature", schema = "web_shop")
@IdClass(ProductFeatureEntityPK.class)
public class ProductFeatureEntity {
    /**
     * The Product id.
     */
    private int productId;
    /**
     * The Feature id.
     */
    private int featureId;
    /**
     * The Value.
     */
    private String value;
    /**
     * The Product.
     */
    private ProductEntity product;
    /**
     * The Feature.
     */
    private FeatureEntity feature;

    /**
     * Gets product id.
     *
     * @return the product id
     */
    @Id
    @Column(name = "product_id")
    public final int getProductId() {
        return productId;
    }

    /**
     * Sets product id.
     *
     * @param productId the product id
     */
    public final void setProductId(final int productId) {
        this.productId = productId;
    }

    /**
     * Gets feature id.
     *
     * @return the feature id
     */
    @Id
    @Column(name = "feature_id")
    public final int getFeatureId() {
        return featureId;
    }

    /**
     * Sets feature id.
     *
     * @param featureId the feature id
     */
    public final void setFeatureId(final int featureId) {
        this.featureId = featureId;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    @Basic
    @Column(name = "value")
    public final String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    @ManyToOne
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    public final ProductEntity getProduct() {
        return product;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public final void setProduct(final ProductEntity product) {
        this.product = product;
    }

    /**
     * Gets feature.
     *
     * @return the feature
     */
    @ManyToOne
    @JoinColumn(name = "feature_id", updatable = false, insertable = false)
    public final FeatureEntity getFeature() {
        return feature;
    }

    /**
     * Sets feature.
     *
     * @param feature the feature
     */
    public final void setFeature(final FeatureEntity feature) {
        this.feature = feature;
    }

    @Override
    public final String toString() {
        return "ProductFeatureEntity{" +
                "productId=" + productId +
                ", featureId=" + featureId +
                ", value='" + value + '\'' +
                '}';
    }
}
