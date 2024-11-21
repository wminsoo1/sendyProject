package com.example.delivery.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPickUpLocation is a Querydsl query type for PickUpLocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPickUpLocation extends BeanPath<PickUpLocation> {

    private static final long serialVersionUID = 1921519999L;

    public static final QPickUpLocation pickUpLocation = new QPickUpLocation("pickUpLocation");

    public final StringPath pickUpAddress = createString("pickUpAddress");

    public final NumberPath<Double> pickUpLatitude = createNumber("pickUpLatitude", Double.class);

    public final NumberPath<Double> pickUpLongitude = createNumber("pickUpLongitude", Double.class);

    public QPickUpLocation(String variable) {
        super(PickUpLocation.class, forVariable(variable));
    }

    public QPickUpLocation(Path<? extends PickUpLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPickUpLocation(PathMetadata metadata) {
        super(PickUpLocation.class, metadata);
    }

}

