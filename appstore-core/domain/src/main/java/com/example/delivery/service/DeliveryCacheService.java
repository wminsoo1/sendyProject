package com.example.delivery.service;

import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryStatus;
import com.example.delivery.model.dto.response.DeliverySummaryResponse;
import com.example.delivery.model.dto.response.DeliverySummaryResponseList;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.repository.DeliveryRepository;
import com.example.stopover.entity.StopOver;
import com.example.stopover.repository.StopOverRepository;
import com.example.member.exception.MemberException;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.delivery.exception.DeliveryErrorCode.DELIVERY_NOT_FOUND;
import static com.example.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class DeliveryCacheService {

    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;
    private final StopOverRepository stopOverRepository;

    //아직 배달 상태를 업데이트 하는 부분이 완성되지 않아(결제 시스템이 아직 없음) 결제가 완료되는 메서드 부분에 @CachePut 해줘야 함
    //역직렬화시 오류 생김 캐싱 데이터는 잘 들어감
    @Cacheable(value = "completedDeliveries", key = "#p0", cacheManager = "cacheManager")
    public DeliverySummaryResponseList getCompletedDeliveriesSummary(Long memberId) {
        validatedMemberExisted(memberId);

        List<Delivery> deliveries = deliveryRepository.findCompletedDeliveriesByMemberId(memberId, DeliveryStatus.DELIVERY_COMPLETED);
        if (deliveries == null) {
            throw new DeliveryException(DELIVERY_NOT_FOUND);
        }

        final List<Long> deliveryIds = deliveries.stream()
                .map(Delivery::getId)
                .toList();

        return DeliverySummaryResponseList.from(createDeliverySummaryResponses(deliveryIds, deliveries));
    }

    private List<DeliverySummaryResponse> createDeliverySummaryResponses(List<Long> deliveryIds, List<Delivery> deliveries) {
        final List<StopOver> stopOverByDeliveryIds = stopOverRepository.findStopOverByDeliveryIds(deliveryIds);
        if (stopOverByDeliveryIds != null && !stopOverByDeliveryIds.isEmpty()) {
            final Map<Long, List<StopOver>> stopOversByDeliveryId = stopOverByDeliveryIds.stream()
                    .collect(Collectors.groupingBy(stopOver -> stopOver.getDelivery().getId()));

            return deliveries.stream()
                    .map(delivery -> {
                        int size = stopOversByDeliveryId.get(delivery.getId()).size();
                        return DeliverySummaryResponse.from(delivery, size);
                    })
                    .toList();
        }

        return deliveries.stream()
                .map(delivery -> DeliverySummaryResponse.from(delivery, 0))
                .toList();
    }

    private void validatedMemberExisted(Long memberId) {
        final boolean isMemberExist = memberRepository.existsById(memberId);
        if (!isMemberExist) {
            throw MemberException.fromErrorCode(MEMBER_NOT_FOUND);
        }
    }
}
