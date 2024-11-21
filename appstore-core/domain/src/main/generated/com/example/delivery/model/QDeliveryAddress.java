package com.example.delivery.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryAddress is a Querydsl query type for DeliveryAddress
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDeliveryAddress extends BeanPath<DeliveryAddress> {

    private static final long serialVersionUID = 887520274L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryAddress deliveryAddress = new QDeliveryAddress("deliveryAddress");

    public final QDropLocation dropLocation;

    public final QPickUpLocation pickupLocation;

    public QDeliveryAddress(String variable) {
        this(DeliveryAddress.class, forVariable(variable), INITS);
    }

    public QDeliveryAddress(Path<? extends DeliveryAddress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryAddress(PathMetadata metadata, PathInits inits) {
        this(DeliveryAddress.class, metadata, inits);
    }

    public QDeliveryAddress(Class<? extends DeliveryAddress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dropLocation = inits.isInitialized("dropLocation") ? new QDropLocation(forProperty("dropLocation")) : null;
        this.pickupLocation = inits.isInitialized("pickupLocation") ? new QPickUpLocation(forProperty("pickupLocation")) : null;
    }

}

