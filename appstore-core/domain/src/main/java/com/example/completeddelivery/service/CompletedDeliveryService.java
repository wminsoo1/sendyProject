package com.example.completeddelivery.service;

import com.example.completeddelivery.entity.CompletedDelivery;
import com.example.completeddelivery.repository.CompletedDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompletedDeliveryService {

    private final CompletedDeliveryRepository completedDeliveryRepository;

    @Transactional //외부에 공개할 api가 아닌 내부에서 수행되는 로직이므로 매개변수는 엔티티 클래스를 이용하였다.
    public CompletedDelivery saveCompletedDelivery(CompletedDelivery completedDelivery) {
        return completedDeliveryRepository.save(completedDelivery);
    }

}
