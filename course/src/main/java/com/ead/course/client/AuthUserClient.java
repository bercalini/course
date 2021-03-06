package com.ead.course.client;

import com.ead.course.dto.ResponsePageDTO;
import com.ead.course.dto.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_USERAUTH;

    public Page<UserDTO> findCourseById(UUID courseId, Pageable pageable) {
        List<UserDTO> usersDTOS = null;
        String url = REQUEST_URL_USERAUTH + utilsService.createUrlUsersCourseId(courseId, pageable);
        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};
            ResponseEntity<ResponsePageDTO<UserDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            usersDTOS = result.getBody().getContent();
            log.debug("Response Number of Elements: {}", usersDTOS.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {} ", e);
        }
        log.info("Ending request /courses userId" , courseId);
        return new PageImpl<>(usersDTOS);
    }

    public ResponseEntity<UserDTO> findByUserId(UUID userId) {
        String url = REQUEST_URL_USERAUTH + "/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
    }

}
