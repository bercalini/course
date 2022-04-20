package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Override
    public String createUrlUsersCourseId(UUID id, Pageable pageable) {
        String url = "/users?courseId=" + id + "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        return url;
    }
}
