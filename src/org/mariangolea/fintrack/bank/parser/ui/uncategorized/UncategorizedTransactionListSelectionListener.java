package org.mariangolea.fintrack.bank.parser.ui.uncategorized;

import java.math.BigDecimal;

import org.mariangolea.fintrack.bank.parser.persistence.repository.transactions.BankTransaction;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UncategorizedTransactionListSelectionListener implements ListChangeListener {

    private final ListView<BankTransaction> list;
    private final Label amountArea;

    public UncategorizedTransactionListSelectionListener(ListView<BankTransaction> list, Label amountArea) {
        this.list = list;
        this.amountArea = amountArea;
    }

    @Override
    public void onChanged(Change c) {
        ObservableList<BankTransaction> selectedItems = list.getSelectionModel().getSelectedItems();
        BigDecimal amount = BigDecimal.ZERO;
        if (selectedItems == null || selectedItems.size() < 1) {
            selectedItems = list.getItems();
        }
        for (BankTransaction transaction : selectedItems) {
            amount = amount.add(transaction.getCreditAmount()).subtract(transaction.getDebitAmount());
        }
        updateLabel(selectedItems.size(), amount);
    }

    private void updateLabel(int transactions, BigDecimal currencyTotal) {
        amountArea.setText(transactions + " transactions, " + currencyTotal.toString() + " amount.");
    }

}
