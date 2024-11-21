package com.example.completeddelivery.repository;

import com.example.completeddelivery.entity.CompletedDelivery;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedDeliveryRepository extends JpaRepository<CompletedDelivery, Long> {

}
