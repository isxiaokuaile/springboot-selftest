package org.example.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.DeviceEntity;

@Mapper
public interface DeviceMapper extends BaseMapper<DeviceEntity> {
}
