package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("device")
public class DeviceEntity {
    @TableId(type = IdType.INPUT)
    private Long deviceId;
    private String deviceName;
    private String deviceSite;    //位置
    private String deviceType;    //类型
    private Long onlineVerificationCycle;    //在线校验周期
    private String dataCollectionMethod;     //数据采集方式
    private Long userId;
}
