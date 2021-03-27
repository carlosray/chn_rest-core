package ru.vas.restcore.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vas.restcore.api.dto.CheckStatusDTO;

import java.util.Set;

@FeignClient("data-service")
public interface DataServiceFeign {
    @PostMapping("/api/blocked/search/ip/status")
    Set<CheckStatusDTO> checkStatus(Set<CheckStatusDTO> ips);

}
