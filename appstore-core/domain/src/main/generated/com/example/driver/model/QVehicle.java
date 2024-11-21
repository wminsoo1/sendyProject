package com.example.driver.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVehicle is a Querydsl query type for Vehicle
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QVehicle extends BeanPath<Vehicle> {

    private static final long serialVersionUID = -971439790L;

    public static final QVehicle vehicle = new QVehicle("vehicle");

    public final EnumPath<VehicleType> vehicleType = createEnum("vehicleType", VehicleType.class);

    public final EnumPath<VehicleWeight> vehicleWeight = createEnum("vehicleWeight", VehicleWeight.class);

    public QVehicle(String variable) {
        super(Vehicle.class, forVariable(variable));
    }

    public QVehicle(Path<? extends Vehicle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVehicle(PathMetadata metadata) {
        super(Vehicle.class, metadata);
    }

}

