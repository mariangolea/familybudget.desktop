package org.mariangolea.fintrack.bank.parser.persistence.repository.companies;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mariangolea.fintrack.bank.parser.persistence.repository.BaseDataJPATest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { CompanyNameService.class, CompanyNameRepository.class,
		CompanyIdentifierRepository.class })
public class CompanyNameServiceTest extends BaseDataJPATest {

	@Autowired
	private CompanyNameService service;
	
	@Autowired
	private CompanyIdentifierRepository idRepo;
	
	
	@Test
	public void testGetAllCompanyIdentifierStrings() {
		Collection<CompanyIdentifier> preexisting = service.getAllCompanyIdentifierStrings();
		assertNotNull(preexisting);
		assertTrue(preexisting.isEmpty());
		
		CompanyIdentifier toAdd = new CompanyIdentifier();
		toAdd.setName("AlohaParent");
		idRepo.save(toAdd);
		
		toAdd = new CompanyIdentifier();
		toAdd.setName("Aloha");
		idRepo.save(toAdd);
		
		preexisting = service.getAllCompanyIdentifierStrings();
		assertNotNull(preexisting);
		assertEquals(2, preexisting.size());
	}
	
	@Test
	public void testGetCompanyIdentifierStrings() {
		Collection<CompanyIdentifier> preexisting = service.getCompanyIdentifierStrings("Aloha");
		assertNotNull(preexisting);
		assertTrue(preexisting.isEmpty());
		
		CompanyIdentifier toAdd = new CompanyIdentifier();
		toAdd.setName("AlohaParent");
		idRepo.save(toAdd);
		
		toAdd = new CompanyIdentifier();
		toAdd.setName("Aloha");
		idRepo.save(toAdd);
		
		preexisting = service.getAllCompanyIdentifierStrings();
		assertNotNull(preexisting);
		assertEquals(2, preexisting.size());
	}

}
