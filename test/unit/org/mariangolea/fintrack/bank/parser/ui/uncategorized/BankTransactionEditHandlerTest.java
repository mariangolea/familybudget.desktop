package org.mariangolea.fintrack.bank.parser.ui.uncategorized;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.mariangolea.fintrack.bank.parser.UserPreferencesTestFactory;
import org.mariangolea.fintrack.bank.parser.ui.uncategorized.edit.BankTransactionEditHandler;
import org.mariangolea.fintrack.bank.parser.ui.uncategorized.edit.EditResult;
import org.mariangolea.fintrack.bank.parser.ui.uncategorized.edit.UncategorizedTransactionApplyListener;

public class BankTransactionEditHandlerTest {

    @Test
    public void testApplyEditResult() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UncategorizedTransactionApplyListener listener = () -> {
        };
        Extension handler = new Extension(listener, factory);
        EditResult result = new EditResult("a", "b", "c", "d");
        handler.applyResult(result);

        UserPreferencesInterface prefs = factory.getUserPreferencesHandler().getPreferences();
        assertEquals("b", prefs.getCompanyDisplayName("a"));
        assertEquals("c", prefs.getParent("b"));
        assertEquals("d", prefs.getParent("c"));
    }

    private class Extension extends BankTransactionEditHandler {

        public Extension(UncategorizedTransactionApplyListener applyListener, UserPreferencesTestFactory factory) {
            super(applyListener, factory.getUserPreferencesHandler().getPreferences());
        }

        @Override
        protected void applyResult(EditResult result) {
            super.applyResult(result);
        }
    }
}
