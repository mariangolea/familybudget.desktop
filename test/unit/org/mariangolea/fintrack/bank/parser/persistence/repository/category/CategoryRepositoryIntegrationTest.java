package org.mariangolea.fintrack.bank.parser.persistence.repository.category;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mariangolea.fintrack.bank.parser.persistence.repository.BaseDataJPATest;
import org.mariangolea.fintrack.bank.parser.persistence.repository.categories.CategoriesRepository;
import org.mariangolea.fintrack.bank.parser.persistence.repository.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CategoriesRepository.class)
public class CategoryRepositoryIntegrationTest extends BaseDataJPATest{

	@Autowired
	private CategoriesRepository categoriesRepository;
	
	@Test
	public void testBasicOperations() {
		List<Category> preexisting = categoriesRepository.findAll();
		Category toAdd = new Category();
		toAdd.setName("Aloha");
		categoriesRepository.save(toAdd);
		
		preexisting = categoriesRepository.findAll();
		assertEquals(1, preexisting.size());
		
		Category search = new Category();
		search.setName("Aloha");
		Category found = categoriesRepository.findOne(Example.of(search)).get();
		assertNotNull(found);
		assertNotNull(found.getId());
		assertEquals("Aloha", found.getName());
	}
}
