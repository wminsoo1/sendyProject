package com.example.deleteddelivery.repository;

import com.example.deleteddelivery.entity.DeletedDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedDeliveryRepository extends JpaRepository<DeletedDelivery, Long> {

}
