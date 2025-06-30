package com.cathaybk.tpi_leo_demo.service;

import com.cathaybk.tpi_leo_demo.dto.CoindeskResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoindeskService {

    private final String COINDESK_API_URL = "https://kengp3.github.io/blog/coindesk.json";

    public CoindeskResponse getCoindeskData() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(COINDESK_API_URL, CoindeskResponse.class);
    }
}
