package com.example.stopover.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStopOver is a Querydsl query type for StopOver
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStopOver extends EntityPathBase<StopOver> {

    private static final long serialVersionUID = 895449722L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStopOver stopOver = new QStopOver("stopOver");

    public final com.example.delivery.model.entity.QDelivery delivery;

    public final com.example.delivery.model.QDeliveryAddress deliveryAddress;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QStopOver(String variable) {
        this(StopOver.class, forVariable(variable), INITS);
    }

    public QStopOver(Path<? extends StopOver> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStopOver(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStopOver(PathMetadata metadata, PathInits inits) {
        this(StopOver.class, metadata, inits);
    }

    public QStopOver(Class<? extends StopOver> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.delivery = inits.isInitialized("delivery") ? new com.example.delivery.model.entity.QDelivery(forProperty("delivery"), inits.get("delivery")) : null;
        this.deliveryAddress = inits.isInitialized("deliveryAddress") ? new com.example.delivery.model.QDeliveryAddress(forProperty("deliveryAddress"), inits.get("deliveryAddress")) : null;
    }

}

