package com.example.driver.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SignInDriverRequest {
    private String driverName;
    private String password;

    public SignInDriverRequest(String driverName, String password) {
        this.driverName = driverName;
        this.password = password;
    }
}
