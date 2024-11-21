package com.example.driver.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDriverAddress is a Querydsl query type for DriverAddress
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDriverAddress extends BeanPath<DriverAddress> {

    private static final long serialVersionUID = 1519148114L;

    public static final QDriverAddress driverAddress = new QDriverAddress("driverAddress");

    public final StringPath street = createString("street");

    public QDriverAddress(String variable) {
        super(DriverAddress.class, forVariable(variable));
    }

    public QDriverAddress(Path<? extends DriverAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDriverAddress(PathMetadata metadata) {
        super(DriverAddress.class, metadata);
    }

}

