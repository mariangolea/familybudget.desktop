package org.mariangolea.fintrack.bank.parser.ui.categorized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.mariangolea.fintrack.bank.parser.UserPreferencesTestFactory;
import org.mariangolea.fintrack.bank.parser.ui.categorized.table.TableViewData;
import org.mariangolea.fintrack.bank.transaction.api.BankTransactionGroupInterface;
import org.mariangolea.fintrack.bank.transaction.csv.BankTransactionDefaultGroup;

import javafx.collections.FXCollections;

public class TableViewDataTest {
    
    @Test
    public void testConstructor(){
        Map<YearSlot, Collection<BankTransactionGroupInterface>> map = FXCollections.observableHashMap();
        Collection<BankTransactionGroupInterface> groups = Arrays.asList(new BankTransactionDefaultGroup("aloha"));
        map.put(new YearSlot(2018), groups);
        UserPreferencesInterface prefs = new UserPreferencesTestFactory().getUserPreferencesHandler().getPreferences();
        TableViewData data = new TableViewData("aloha", true, map, prefs);
        assertNotNull(data);
        assertEquals("aloha", data.getTopMostCategoryString());
        assertEquals(BigDecimal.ZERO.toString(), data.getAmountString(0));
        assertEquals(1, data.getAmountStrings().get().size());
        
        data = new TableViewData("aloha", false, map, prefs);
        assertEquals("aloha", data.getSubCategoryString());
    }
}
