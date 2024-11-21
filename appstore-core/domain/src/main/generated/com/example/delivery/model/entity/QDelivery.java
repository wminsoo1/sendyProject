package com.example.delivery.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDelivery is a Querydsl query type for Delivery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDelivery extends EntityPathBase<Delivery> {

    private static final long serialVersionUID = -1249213295L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDelivery delivery = new QDelivery("delivery");

    public final com.example.global.QBaseEntity _super = new com.example.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath deleted = createString("deleted");

    public final com.example.delivery.model.QDeliveryAddress deliveryAddress;

    public final com.example.delivery.model.QDeliveryCategory deliveryCategory;

    public final DateTimePath<java.time.LocalDateTime> deliveryDate = createDateTime("deliveryDate", java.time.LocalDateTime.class);

    public final NumberPath<java.math.BigDecimal> deliveryFee = createNumber("deliveryFee", java.math.BigDecimal.class);

    public final StringPath deliveryOptions = createString("deliveryOptions");

    public final EnumPath<com.example.delivery.model.DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", com.example.delivery.model.DeliveryStatus.class);

    public final NumberPath<Long> driverId = createNumber("driverId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idempotencyKey = createString("idempotencyKey");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath reservationNumber = createString("reservationNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.driver.model.QVehicle vehicle;

    public QDelivery(String variable) {
        this(Delivery.class, forVariable(variable), INITS);
    }

    public QDelivery(Path<? extends Delivery> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDelivery(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDelivery(PathMetadata metadata, PathInits inits) {
        this(Delivery.class, metadata, inits);
    }

    public QDelivery(Class<? extends Delivery> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.deliveryAddress = inits.isInitialized("deliveryAddress") ? new com.example.delivery.model.QDeliveryAddress(forProperty("deliveryAddress"), inits.get("deliveryAddress")) : null;
        this.deliveryCategory = inits.isInitialized("deliveryCategory") ? new com.example.delivery.model.QDeliveryCategory(forProperty("deliveryCategory")) : null;
        this.vehicle = inits.isInitialized("vehicle") ? new com.example.driver.model.QVehicle(forProperty("vehicle")) : null;
    }

}

