package org.infracloud.url.shortener.repository;

import org.infracloud.url.shortener.entities.URLMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLMappingRepository extends JpaRepository<URLMapping, Long> {
}
