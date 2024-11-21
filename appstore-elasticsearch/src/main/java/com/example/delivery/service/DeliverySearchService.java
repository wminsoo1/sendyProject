package com.example.delivery.service;

import com.example.delivery.document.DeliveryDocument;
import com.example.delivery.repository.DeliveryDocumentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliverySearchService {

    private final DeliveryDocumentRepository deliveryDocumentRepository;

    public void saveDelivery(DeliveryDocument delivery) {
        deliveryDocumentRepository.save(delivery);
    }

    public List<DeliveryDocument> searchByCategory(String deliveryCategory) {
        return deliveryDocumentRepository.findByDeliveryCategory(deliveryCategory);
    }

}
