package org.example.controller;

import org.example.dto.device.*;
import org.example.service.DeviceService;
import org.example.service.SysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/sys/device")
public class DeviceController {

    @Autowired
    private SysDeviceService sysDeviceService;
    @Autowired
    private DeviceService deviceService;


    @PostMapping("/list")
    public ResponseEntity<List<OutDeviceDto>> list (@RequestBody(required = false) @Valid InQueryDeviceDto dto){
        List<OutDeviceDto> all = deviceService.getAll(dto);
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public Long add(@RequestBody @Valid InAddDeviceDto dto){
        Long add = sysDeviceService.add(dto);
        return add;
       // return ;
    }

    @PutMapping
    public Boolean upd(@RequestBody @Valid InUpdateDeviceDto dto){
        Boolean update = sysDeviceService.update(dto);
        return update;
        // return ;
    }

    @PostMapping("/page")
    public List<OutDeviceDto> page(@RequestBody @Valid InQueryDeviceDto dto){
        List<OutDeviceDto> select = sysDeviceService.page(dto);
        return select;
        // return ;
    }

    @DeleteMapping
    public Boolean delete(@RequestBody @Valid InDeleteDeviceDto dto){
        Boolean delete = sysDeviceService.delete(dto);
        return delete;
        // return ;
    }

}
