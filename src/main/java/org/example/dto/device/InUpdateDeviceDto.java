package org.example.dto.device;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InUpdateDeviceDto {
    @NotNull
    private Long deviceId;
    private String deviceName;
    private String deviceSite;    //位置
    private String deviceType;    //类型
    private Long onlineVerificationCycle;    //在线校验周期
    private String dataCollectionMethod;     //数据采集方式
    private Long userId;
}
