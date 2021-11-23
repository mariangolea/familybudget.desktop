package org.mariangolea.fintrack.bank.parser.persistence.repository.companies;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class CompanyNameService {

    @Autowired
    private CompanyNameRepository companyNamesRepo;
    
    @Autowired
    private CompanyIdentifierRepository companyIdentifiersRepo;

    public Collection<CompanyIdentifier> getAllCompanyIdentifierStrings() {
        return companyIdentifiersRepo.findAll();
    }

    public Collection<CompanyIdentifier> getCompanyIdentifierStrings(final String companyDisplayName){
        CompanyName sample = new CompanyName();
        sample.setName(companyDisplayName);
        return companyNamesRepo.findOne(Example.of(sample)).get().getIdentifiers();
    }

    public Collection<CompanyIdentifier> getMatchingIdentifierStrings(final String transactionDescription){
        List<CompanyIdentifier> identifiers = companyIdentifiersRepo.findAll();
        Collection<CompanyIdentifier> matching = Collections.emptyList();
        identifiers.forEach(identifier -> {
            if (identifier.getName().toLowerCase().matches(transactionDescription)){
                matching.add(identifier);
            }
        });
        
        return matching;
    }
    
    public String getCompanyDisplayName(final String companyIdentifier){
        CompanyIdentifier sample = new CompanyIdentifier();
        sample.setName(companyIdentifier);
        return companyIdentifiersRepo.findOne(Example.of(sample)).get().getCompanyName().getName();
    }

    public Collection<CompanyName> getCompanyDisplayNames(){
        return companyNamesRepo.findAll();
    }

    public void deleteCompanyName(final CompanyName company){
        companyNamesRepo.delete(company);
    }

    public void editCompanyName(final String existingName, final String newName){
        CompanyName sample = new CompanyName();
        sample.setName(existingName);
        CompanyName company = companyNamesRepo.findOne(Example.of(sample)).get();
        company.setName(newName);
        companyNamesRepo.save(company);
    }

    public void editCompanyIdentifier(final String existingIdentifier, final String newIdentifier){
        CompanyIdentifier sample = new CompanyIdentifier();
        sample.setName(existingIdentifier);
        CompanyIdentifier target = companyIdentifiersRepo.findOne(Example.of(sample)).get();
        target.setName(newIdentifier);
        companyIdentifiersRepo.save(target);
    }

    public void resetCompanyIdentifierStrings(final String displayName, final Collection<CompanyIdentifier> newIdentifiers){
        CompanyName sample = new CompanyName();
        sample.setName(displayName);
        CompanyName company = companyNamesRepo.findOne(Example.of(sample)).get();
        company.getIdentifiers().clear();
        company.getIdentifiers().addAll(newIdentifiers);
        companyNamesRepo.save(company);
    }


    public boolean hasCompanyDisplayName(final String companyDisplayName){
        CompanyName sample = new CompanyName();
        sample.setName(companyDisplayName);
        CompanyName company = companyNamesRepo.findOne(Example.of(sample)).get();
        return company != null;
    }
}
