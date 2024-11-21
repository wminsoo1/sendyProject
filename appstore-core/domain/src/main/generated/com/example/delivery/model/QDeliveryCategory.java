package com.example.delivery.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryCategory is a Querydsl query type for DeliveryCategory
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDeliveryCategory extends BeanPath<DeliveryCategory> {

    private static final long serialVersionUID = -1282416480L;

    public static final QDeliveryCategory deliveryCategory = new QDeliveryCategory("deliveryCategory");

    public final EnumPath<PersonalDeliveryCategory> deliveryPersonalCategory = createEnum("deliveryPersonalCategory", PersonalDeliveryCategory.class);

    public final EnumPath<DeliveryType> deliveryType = createEnum("deliveryType", DeliveryType.class);

    public QDeliveryCategory(String variable) {
        super(DeliveryCategory.class, forVariable(variable));
    }

    public QDeliveryCategory(Path<? extends DeliveryCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryCategory(PathMetadata metadata) {
        super(DeliveryCategory.class, metadata);
    }

}

