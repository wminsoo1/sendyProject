package com.example.delivery.repository;

import com.example.delivery.document.DeliveryDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DeliveryDocumentRepository extends ElasticsearchRepository<DeliveryDocument,Long> {

    List<DeliveryDocument> findByDeliveryCategory(String deliveryCategory);
}
