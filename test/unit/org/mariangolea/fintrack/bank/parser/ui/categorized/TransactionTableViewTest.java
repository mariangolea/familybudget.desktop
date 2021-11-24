package org.mariangolea.fintrack.bank.parser.ui.categorized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mariangolea.fintrack.bank.parser.UserPreferencesTestFactory;
import org.mariangolea.fintrack.bank.parser.Utilities;
import org.mariangolea.fintrack.bank.parser.ui.FXUITest;
import org.mariangolea.fintrack.bank.parser.ui.categorized.table.TransactionTableView;
import org.mariangolea.fintrack.bank.parser.ui.categorized.table.TransactionTableView.AmountStringPropertyValueFactory;
import org.mariangolea.fintrack.bank.transaction.api.BankTransaction;
import org.mariangolea.fintrack.bank.transaction.api.BankTransactionGroupInterface;

import javafx.beans.property.SimpleObjectProperty;

public class TransactionTableViewTest extends FXUITest{

    @Test
    public void testColumns() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        BankTransaction transaction = Utilities.createTransaction(Utilities.createDate(1, 2017), BigDecimal.ONE, BigDecimal.ZERO, "company1 identifier");
        UserPreferencesInterface prefs = factory.getUserPreferencesHandler().getPreferences();
        prefs.setTransactionGroupingTimeframe(UserPreferencesInterface.Timeframe.MONTH);
        prefs.resetCompanyIdentifierStrings("Company", Arrays.asList("company1"));
        prefs.appendDefinition("CompanyGroup", Arrays.asList("Company"));

        TransactionTableView view = new TransactionTableView(prefs);
        TransactionsCategorizedSlotter slotter = new TransactionsCategorizedSlotter(Arrays.asList(transaction), prefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> model = slotter.getUnmodifiableSlottedCategorized();
        view.resetView(model);

        assertEquals(3, view.getColumns().size());
        
        Extension cellFactory = new Extension(1);
        SimpleObjectProperty<List<String>> props = new SimpleObjectProperty<>(Arrays.asList("","CompanyGroup"));
        cellFactory.updateItem(props, true);
        assertNull(cellFactory.getText());
        
        cellFactory.updateItem(props, false);
        assertEquals("CompanyGroup", cellFactory.getText());
    }
    
    private class Extension extends AmountStringPropertyValueFactory{
        
        public Extension(int column) {
            super(column);
        }

        @Override
        protected void updateItem(SimpleObjectProperty<List<String>> item, boolean empty) {
            super.updateItem(item, empty); 
        }
    }
}
