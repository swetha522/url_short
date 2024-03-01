package com.tele.url_short.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tele.url_short.dto.MyUrl;

public interface UrlRepository extends JpaRepository<MyUrl, Integer> {

    boolean existsByShortUrl(String shortUrl);

    MyUrl findByShortUrl(String url);

}
