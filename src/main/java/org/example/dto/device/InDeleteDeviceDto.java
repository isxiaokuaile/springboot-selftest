package org.example.dto.device;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InDeleteDeviceDto {
    @NotNull
    private Long deviceId;
}
