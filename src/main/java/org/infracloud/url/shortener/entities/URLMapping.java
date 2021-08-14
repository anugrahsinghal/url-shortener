package org.infracloud.url.shortener.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// indexing this column since we have requirement to return same short URL for already present long url
// thus indexing this column will help us figure out the values
@Table(indexes = @Index(columnList = "long_url"))
public class URLMapping {

	@Id
	@SequenceGenerator(name = "url_seq_gen", sequenceName = "url_id_sequence", initialValue = 100000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_seq_gen")
	private long id;

	@URL
	@Column(nullable = false, name = "long_url")
	private String longUrl;

	@Column(nullable = false)
	private Instant creationTime;

	private Instant expirationTime;

	public URLMapping(String longUrl) {
		this.longUrl = longUrl;
		this.creationTime = Instant.now();
	}

}


