package org.example.service.impl;

import org.example.common.annotation.CacheRequest;
import org.example.domain.entity.DeviceEntity;
import org.example.domain.mapper.DeviceMapper;
import org.example.dto.device.InQueryDeviceDto;
import org.example.dto.device.OutDeviceDto;
import org.example.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    @CacheRequest(key = "keyWord")
//    @Cacheable(value = "device" , unless = "#result == null")  //unless="#result == null"表示返回结果为空时不缓存
    public List<OutDeviceDto> getAll(InQueryDeviceDto dto) {
        List<DeviceEntity> deviceEntities = deviceMapper.selectList(null);
        List<OutDeviceDto> outDeviceDtos = new ArrayList<>(deviceEntities.size());
        for (DeviceEntity deviceEntity : deviceEntities) {
            OutDeviceDto outDeviceDto = new OutDeviceDto();
            BeanUtils.copyProperties(deviceEntity, outDeviceDto);
            outDeviceDtos.add(outDeviceDto);
        }
        return outDeviceDtos;
    }


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyField{
        String description();
        int length();
    }


}
