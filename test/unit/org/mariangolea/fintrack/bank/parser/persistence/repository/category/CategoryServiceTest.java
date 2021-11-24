package org.mariangolea.fintrack.bank.parser.persistence.repository.category;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mariangolea.fintrack.bank.parser.persistence.repository.BaseDataJPATest;
import org.mariangolea.fintrack.bank.parser.persistence.repository.categories.CategoriesRepository;
import org.mariangolea.fintrack.bank.parser.persistence.repository.categories.CategoriesService;
import org.mariangolea.fintrack.bank.parser.persistence.repository.categories.Category;
import org.mariangolea.fintrack.bank.parser.persistence.repository.companies.CompanyIdentifierRepository;
import org.mariangolea.fintrack.bank.parser.persistence.repository.companies.CompanyNameRepository;
import org.mariangolea.fintrack.bank.parser.persistence.repository.companies.CompanyNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
		classes = {
				CategoriesService.class, 
				CompanyNameService.class,
				CategoriesRepository.class,
				CompanyNameRepository.class,
				CompanyIdentifierRepository.class
				})
public class CategoryServiceTest extends BaseDataJPATest{

	@Autowired
	private CategoriesService categoriesService;
	
	@Test
	public void testBasicOperations() {
		Category preexisting = categoriesService.getParent("Aloha");
		assertTrue(true);
	}
}
