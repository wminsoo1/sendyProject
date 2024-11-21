package com.example.delivery.model.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliverySummaryResponseList {

    private List<DeliverySummaryResponse> deliverySummaryResponses;

    private DeliverySummaryResponseList(List<DeliverySummaryResponse> deliverySummaryResponses) {
        this.deliverySummaryResponses = deliverySummaryResponses;
    }

    public static DeliverySummaryResponseList from(List<DeliverySummaryResponse> deliverySummaryResponses) {
        return new DeliverySummaryResponseList(deliverySummaryResponses);
    }
}
