package com.example.driver.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDriver is a Querydsl query type for Driver
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDriver extends EntityPathBase<Driver> {

    private static final long serialVersionUID = 1560100473L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDriver driver = new QDriver("driver");

    public final com.example.global.QBaseEntity _super = new com.example.global.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath driverName = createString("driverName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath password = createString("password");

    public final EnumPath<com.example.global.Roles> roles = createEnum("roles", com.example.global.Roles.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.driver.model.QVehicle vehicle;

    public QDriver(String variable) {
        this(Driver.class, forVariable(variable), INITS);
    }

    public QDriver(Path<? extends Driver> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDriver(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDriver(PathMetadata metadata, PathInits inits) {
        this(Driver.class, metadata, inits);
    }

    public QDriver(Class<? extends Driver> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vehicle = inits.isInitialized("vehicle") ? new com.example.driver.model.QVehicle(forProperty("vehicle")) : null;
    }

}

