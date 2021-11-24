package org.mariangolea.fintrack.bank.parser.ui.uncategorized.edit;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.mariangolea.fintrack.bank.parser.persistence.repository.transactions.BankTransaction;
import org.mariangolea.fintrack.bank.parser.ui.EditDialog;

public class BankTransactionEditHandler {

    private final UserPreferencesInterface userPrefs;
    private EditDialog editPopup;
    private BankTransactionEditPane editPane;
    private final UncategorizedTransactionApplyListener applyListener;

    public BankTransactionEditHandler(final UncategorizedTransactionApplyListener applyListener, final UserPreferencesInterface userPrefs) {
        this.applyListener = Objects.requireNonNull(applyListener);
        this.userPrefs = Objects.requireNonNull(userPrefs);
    }

    public void editTransaction(final BankTransaction transaction) {
        if (editPopup == null) {
            editPane = new BankTransactionEditPane(userPrefs);
            editPopup = new EditDialog<>("Edit company name and apply to similar transactions", editPane, BankTransactionEditPane::getEditResult, BankTransactionEditPane::isValid);
        }
        editPane.setBankTransaction(transaction);
        Optional<EditResult> result = editPopup.showAndWait();
        result.ifPresent(userData -> {
            applyResult(userData);
        });
    }

    protected void applyResult(final EditResult result) {
        userPrefs.resetCompanyIdentifierStrings(result.companyDisplayName, Arrays.asList(result.companyIdentifierString));
        userPrefs.appendDefinition(result.categoryName, Arrays.asList(result.companyDisplayName));
        if (result.parentCategory != null && !result.parentCategory.isEmpty()) {
            userPrefs.appendDefinition(result.parentCategory, Arrays.asList(result.categoryName));
        }
        applyListener.transactionEditApplied();
    }
}
