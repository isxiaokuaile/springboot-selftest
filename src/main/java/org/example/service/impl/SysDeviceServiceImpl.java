package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.domain.entity.DeviceEntity;
import org.example.domain.mapper.DeviceMapper;
import org.example.dto.device.*;
import org.example.service.SysDeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeviceServiceImpl implements SysDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

//
//    @Override
//    @Cacheable(value = "device" ,key = "'all'", unless = "#result == null")  //unless="#result == null"表示返回结果为空时不缓存
//    public List<OutDeviceDto> getAll(InQueryDeviceDto dto) {
//        List<DeviceEntity> deviceEntities = deviceMapper.selectList(null);
//        List<OutDeviceDto> outDeviceDtos = new ArrayList<>(deviceEntities.size());
//        for (DeviceEntity deviceEntity : deviceEntities) {
//            OutDeviceDto outDeviceDto = new OutDeviceDto();
//            BeanUtils.copyProperties(deviceEntity, outDeviceDto);
//            outDeviceDtos.add(outDeviceDto);
//        }
//
//        return outDeviceDtos;
//    }

    @Override
    public Long add(InAddDeviceDto dto) {
        DeviceEntity entity = new DeviceEntity();
        BeanUtils.copyProperties(dto, entity);
        deviceMapper.insert(entity);
        if (entity.getDeviceId() != null) {
            return entity.getDeviceId();
        }
        return null;
    }

    @Override
    public Boolean update(InUpdateDeviceDto dto) {
        DeviceEntity entity = deviceMapper.selectById(dto.getDeviceId());
        BeanUtils.copyProperties(dto, entity);
        return deviceMapper.updateById(entity) > 0;
    }

    @Override
    public List<OutDeviceDto> page(InQueryDeviceDto dto) {
        LambdaQueryWrapper<DeviceEntity> wrapper = new LambdaQueryWrapper<>();
        List<DeviceEntity> deviceEntities;
        if (dto.getDeviceName() != null) {
            wrapper.eq(DeviceEntity::getDeviceName, dto.getDeviceName());
             deviceEntities = deviceMapper.selectList(wrapper);
        }else {
            deviceEntities = deviceMapper.selectList(null);
        }
            List<OutDeviceDto> outDeviceDtos = new ArrayList<>(deviceEntities.size());
            for (DeviceEntity deviceEntity : deviceEntities) {
                OutDeviceDto outDeviceDto = new OutDeviceDto();
                BeanUtils.copyProperties(deviceEntity, outDeviceDto);
                outDeviceDtos.add(outDeviceDto);
            }
            return outDeviceDtos;
    }

    @Override
    @CacheEvict(value = "device",allEntries = true)
    @Cacheable
    public Boolean delete(InDeleteDeviceDto dto) {
        DeviceEntity entity = deviceMapper.selectById(dto.getDeviceId());
        return deviceMapper.deleteById(entity) > 0;
    }
}
