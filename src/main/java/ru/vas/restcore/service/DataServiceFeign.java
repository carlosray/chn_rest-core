package ru.vas.restcore.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Set;

@FeignClient("data-service")
public interface DataServiceFeign {
    @PostMapping("/api/blocked/search/ip/status")
    Map<String, Boolean> checkStatus(Set<String> ips);

}
