package org.mariangolea.fintrack.bank.parser.persistence.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@DataJpaTest
@EnableAutoConfiguration
@EnableJpaRepositories("org.mariangolea.fintrack.bank.parser.persistence.repository.*")
@EntityScan("org.mariangolea.fintrack.bank.*")
public class BaseDataJPATest {

}
