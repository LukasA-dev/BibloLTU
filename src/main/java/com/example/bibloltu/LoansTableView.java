package com.example.bibloltu;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import java.time.LocalDate;

public class LoansTableView {
    private final SimpleStringProperty loanID;
    private final SimpleStringProperty loanTitle;
    private final SimpleStringProperty barcode;
    private final ObjectProperty<LocalDate> borrowDate;
    private final ObjectProperty<LocalDate> returnDate;

    public LoansTableView(String loanID, String loanTitle, String barcode, LocalDate borrowDate, LocalDate returnDate) {
        this.loanID = new SimpleStringProperty(loanID);
        this.loanTitle = new SimpleStringProperty(loanTitle);
        this.barcode = new SimpleStringProperty(barcode);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
    }

    public String getLoanID() {
        return loanID.get();
    }

    public SimpleStringProperty loanIDProperty() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID.set(loanID);
    }

    public String getLoanTitle() {
        return loanTitle.get();
    }

    public SimpleStringProperty loanTitleProperty() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle.set(loanTitle);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public LocalDate getBorrowDate() {
        return borrowDate.get();
    }

    public ObjectProperty<LocalDate> borrowDateProperty() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate.set(borrowDate);
    }

    public LocalDate getReturnDate() {
        return returnDate.get();
    }

    public ObjectProperty<LocalDate> returnDateProperty() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate);
    }
}