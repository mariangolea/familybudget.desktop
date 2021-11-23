package org.mariangolea.fintrack.bank.parser.persistence.repository.categories;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import org.mariangolea.fintrack.bank.parser.persistence.repository.companies.CompanyIdentifier;
import org.mariangolea.fintrack.bank.parser.persistence.repository.companies.CompanyNameService;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepo;

    @Autowired
    private CompanyNameService companyNamesService;

    public static final String UNCATEGORIZED = "Uncategorized";

    public Collection<Category> getUserDefinedCategoryNames() {
        return categoriesRepo.findAll();
    }

    public Collection<Category> getTopMostCategories() {
        Category sample = new Category();
        sample.setParent(null);
        return categoriesRepo.findAll(Example.of(sample));
    }

    public Collection<Category> getSubCategories(final String categoryName) {
        Long categoryID = findCategory(categoryName).getId();
        Category sample = new Category();
        sample.setParent(categoryID);
        return categoriesRepo.findAll(Example.of(sample));
    }

    public Category getMatchingCategory(String companyDescriptionString) {
        Collection<CompanyIdentifier> identifiers = companyNamesService.getAllCompanyIdentifierStrings();
        for (CompanyIdentifier identifier : identifiers) {
            if (companyDescriptionString.toLowerCase().contains(identifier.getName().toLowerCase())) {
                Category category = findCategory(identifier.getCompanyName().getName());
                if (category != null){
                    return category;
                }
                break;
            }
        }
        
        return null;
    }

    public void appendDefinition(final String categoryName, final Collection<String> subCategories){
        
    }

    public Collection<String> removeSubCategory(final String parentCategory, final String category){
        return null;
    }

    public Category getParent(final String categoryName) {
        Long parentId = findCategory(categoryName).getParent();
        return categoriesRepo.findById(parentId.intValue()).get();
    }

    private Category findCategory(final String categoryName) {
        Category sample = new Category();
        sample.setName(categoryName);
        return categoriesRepo.findOne(Example.of(sample)).get();
    }

    private Category findCategory(final Long parentID) {
        Category sample = new Category();
        sample.setParent(parentID);
        return categoriesRepo.findOne(Example.of(sample)).get();
    }

    private Category findParent(final String categoryName) {
        Category category = findCategory(categoryName);
        return findCategory(category.getParent());
    }
}
