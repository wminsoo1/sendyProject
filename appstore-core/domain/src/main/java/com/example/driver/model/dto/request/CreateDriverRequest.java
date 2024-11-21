package com.example.driver.model.dto.request;

import com.example.driver.model.Vehicle;
import com.example.driver.model.entity.Driver;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateDriverRequest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String driverName;

    @Pattern(regexp = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,32}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~32자 입니다.")
    private String password;

    @Embedded
    @NotNull(message = "차량 정보는 필수 입력 항목입니다.")
    private Vehicle vehicle;

    private MultipartFile multipartFile;

    public CreateDriverRequest(String driverName, String password, Vehicle vehicle,
        MultipartFile multipartFile) {
        this.driverName = driverName;
        this.password = password;
        this.vehicle = vehicle;
        this.multipartFile = multipartFile;
    }

    public Driver toDriver() {
        return Driver.builder()
                .driverName(driverName)
                .password(password)
                .vehicle(vehicle)
                .build();
    }

    public static CreateDriverRequest mapToCreateDriverRequest(String driverData, MultipartFile multipartFile) throws IOException {
        CreateDriverRequest createDriverRequest = OBJECT_MAPPER.readValue(driverData, CreateDriverRequest.class);
        createDriverRequest.multipartFile = multipartFile;

        return createDriverRequest;
    }

}
