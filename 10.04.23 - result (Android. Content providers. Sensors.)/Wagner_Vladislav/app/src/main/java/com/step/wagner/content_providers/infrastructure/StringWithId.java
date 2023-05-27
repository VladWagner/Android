package com.step.wagner.content_providers.infrastructure;

import androidx.annotation.NonNull;

public class StringWithId {

    private String snp;

    private int id;

    public StringWithId(String snp, int id) {
        this.snp = snp;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return snp;
    }

    public String getSnp() {
        return snp;
    }

    public void setSnp(String snp) {
        this.snp = snp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
