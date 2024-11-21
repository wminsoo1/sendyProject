package com.example.stopover.repository;

import com.example.delivery.model.entity.Delivery;
import com.example.stopover.entity.StopOver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StopOverRepository extends JpaRepository<StopOver, Long> {

    @Query("select s from StopOver s join fetch s.delivery d where d.id = :deliveryId")
    List<StopOver> findStopOverByDeliveryId(@Param("deliveryId") Long deliveryId);

    @Query("select s from StopOver s join fetch s.delivery d where d.reservationNumber = :reservationNumber")
    List<StopOver> findStopOverByReservationNumber(@Param("reservationNumber") String reservationNumber);

    @Query("select s from StopOver s join fetch s.delivery d where d.id IN :deliveryIds")
    List<StopOver> findStopOverByDeliveryIds(@Param("deliveryIds") List<Long> deliveryIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from StopOver s where s in :stopOvers")
    void deleteAllInBulk(@Param("stopOvers") List<StopOver> stopOvers);

    @Modifying(clearAutomatically = true)
    @Query("delete from StopOver s where s.delivery.id in :deliveriesId")
    void deleteAllByDeliveriesInBulk(@Param("deliveriesId") List<Long> deliveriesId);

}
