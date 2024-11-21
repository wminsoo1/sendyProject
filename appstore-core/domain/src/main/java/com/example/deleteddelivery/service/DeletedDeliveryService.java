package com.example.deleteddelivery.service;

import com.example.deleteddelivery.entity.DeletedDelivery;
import com.example.deleteddelivery.repository.DeletedDeliveryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeletedDeliveryService {

    private final DeletedDeliveryRepository deletedDeliveryRepository;

    @Transactional
    public void saveDeletedDeliveries(List<DeletedDelivery> deletedDeliveries) {
        if (CollectionUtils.isEmpty(deletedDeliveries)) {
            return;
        }

        deletedDeliveryRepository.saveAll(deletedDeliveries);
    }

}
