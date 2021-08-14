package org.infracloud.url.shortener.repository;

import java.util.Optional;
import org.infracloud.url.shortener.entities.URLMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLMappingRepository extends JpaRepository<URLMapping, Long> {

	Optional<URLMapping> findByLongUrl(String url);

}
