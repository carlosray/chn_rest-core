package ru.vas.restcore.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vas.restcore.api.dto.BlockedResourceDTO;

import java.util.Set;

@FeignClient("data-service")
public interface DataServiceClient {

    @GetMapping("api/blocked/search/ip")
    Set<BlockedResourceDTO> searchIp(@RequestParam String search,
                                                             @RequestParam(required = false, defaultValue = "true") boolean actual);

    @GetMapping("api/blocked/search/domain")
    Set<BlockedResourceDTO> searchDomain(@RequestParam String search,
                                                                @RequestParam(required = false, defaultValue = "true") boolean actual);

    @GetMapping("api/blocked/count")
    Long searchCountBlocked(@RequestParam(required = false, defaultValue = "true") boolean actual);


}
