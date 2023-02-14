package org.example.dto.device;

import lombok.Data;

import java.io.Serializable;

@Data
public class OutDeviceDto implements Serializable {
    private Long deviceId;
    private String deviceName;
    private String deviceSite;    //位置
    private String deviceType;    //类485
    private Long onlineVerificationCycle;    //在线校验周期
    private String dataCollectionMethod;     //数据采集方式
    private Long userId;
}
