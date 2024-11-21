package com.example.deleteddelivery.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeletedDelivery is a Querydsl query type for DeletedDelivery
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeletedDelivery extends EntityPathBase<DeletedDelivery> {

    private static final long serialVersionUID = 1321987092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeletedDelivery deletedDelivery = new QDeletedDelivery("deletedDelivery");

    public final com.example.global.QBaseEntity _super = new com.example.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

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

    public QDeletedDelivery(String variable) {
        this(DeletedDelivery.class, forVariable(variable), INITS);
    }

    public QDeletedDelivery(Path<? extends DeletedDelivery> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeletedDelivery(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeletedDelivery(PathMetadata metadata, PathInits inits) {
        this(DeletedDelivery.class, metadata, inits);
    }

    public QDeletedDelivery(Class<? extends DeletedDelivery> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.deliveryAddress = inits.isInitialized("deliveryAddress") ? new com.example.delivery.model.QDeliveryAddress(forProperty("deliveryAddress"), inits.get("deliveryAddress")) : null;
        this.deliveryCategory = inits.isInitialized("deliveryCategory") ? new com.example.delivery.model.QDeliveryCategory(forProperty("deliveryCategory")) : null;
        this.vehicle = inits.isInitialized("vehicle") ? new com.example.driver.model.QVehicle(forProperty("vehicle")) : null;
    }

}

