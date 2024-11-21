package com.example.delivery.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDropLocation is a Querydsl query type for DropLocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDropLocation extends BeanPath<DropLocation> {

    private static final long serialVersionUID = -1591500206L;

    public static final QDropLocation dropLocation = new QDropLocation("dropLocation");

    public final StringPath dropAddress = createString("dropAddress");

    public final NumberPath<Double> dropLatitude = createNumber("dropLatitude", Double.class);

    public final NumberPath<Double> dropLongitude = createNumber("dropLongitude", Double.class);

    public QDropLocation(String variable) {
        super(DropLocation.class, forVariable(variable));
    }

    public QDropLocation(Path<? extends DropLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDropLocation(PathMetadata metadata) {
        super(DropLocation.class, metadata);
    }

}

