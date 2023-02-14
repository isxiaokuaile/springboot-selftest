package org.example.service;

import org.example.dto.device.*;

import java.util.List;

public interface SysDeviceService {
    Long add(InAddDeviceDto dto);
    Boolean update(InUpdateDeviceDto dto);
    List<OutDeviceDto> page(InQueryDeviceDto dto);
    Boolean delete(InDeleteDeviceDto dto);
//    List<OutDeviceDto> getAll(InQueryDeviceDto dto);

}
