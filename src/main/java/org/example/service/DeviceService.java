package org.example.service;

import org.example.dto.device.InQueryDeviceDto;
import org.example.dto.device.OutDeviceDto;

import java.util.List;

public interface DeviceService {
    List<OutDeviceDto> getAll(InQueryDeviceDto dto);
}
