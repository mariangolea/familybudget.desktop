package org.mariangolea.fintrack.bank.parser.ui.categorized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.mariangolea.fintrack.bank.parser.UserPreferencesTestFactory;
import org.mariangolea.fintrack.bank.parser.ui.FXUITest;
import org.mariangolea.fintrack.bank.parser.ui.preferences.companynames.EditCompanyNamesPane;
import org.mariangolea.fintrack.bank.parser.ui.preferences.companynames.single.EditCompanyNamePane;

import javafx.collections.ObservableList;

public class CompanyNamesEditerTest extends FXUITest {

    @Test
    public void testEditPanes() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        EditCompanyNamePane single = new EditCompanyNamePane("aloha", Arrays.asList("one"));
        assertNotNull(single.getResult());
        EditCompanyNamesPane multi = new EditCompanyNamesPane(new UserPreferencesTestFactory().getUserPreferencesHandler().getPreferences());
        assertNotNull(multi.getResult());
    }

    @Test
    public void testEventHandlers() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Extension single = new Extension("aloha", Arrays.asList("one"));
        assertNotNull(single.getResult());
        single.searchFieldTextChanged("o");
        assertTrue(single.getAddBtnEnabledState());
        single.searchFieldTextChanged("z");
        assertFalse(single.getAddBtnEnabledState());

        single.setSearchFieldText("b");
        single.addButtonClicked();
        assertNull(single.getSearchFieldText());
        assertTrue(single.isValid());
    }

    @Test
    public void testCellRenderer() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Extension single = new Extension("aloha", Arrays.asList("one"));
        Extension.CompanyIdentifierListCellExtension renderer = single.createRendererInstance();
        renderer.updateItem("aloha", true);
        assertTrue(renderer.getLabelText().isEmpty());

        renderer.updateItem("aloha", false);
        assertEquals("aloha", renderer.getLabelText());
    }

    private class Extension extends EditCompanyNamePane {

        Extension(final String companyName, final Collection<String> companyIdentifiers) {
            super(companyName, companyIdentifiers);
        }

        private CompanyIdentifierListCellExtension createRendererInstance() {
            return new CompanyIdentifierListCellExtension(filtered);
        }

        @Override
        protected void addButtonClicked() {
            super.addButtonClicked();
        }

        @Override
        protected void searchFieldTextChanged(String filter) {
            super.searchFieldTextChanged(filter);
        }

        private boolean getAddBtnEnabledState() {
            return add.disableProperty().get();
        }

        private String getSearchFieldText() {
            return searchField.getText();
        }

        private void setSearchFieldText(final String text) {
            searchField.setText(text);
        }

        private class CompanyIdentifierListCellExtension extends CompanyIdentifierListCell {

            public CompanyIdentifierListCellExtension(ObservableList<String> identifierStrings) {
                super(identifierStrings);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
            }

            private String getLabelText() {
                return label.getText();
            }
        }
    }
}
