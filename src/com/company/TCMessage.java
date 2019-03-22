package com.company;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

public class TCMessage implements Serializable {
    private static final long serialVersionUID = -8257502186256118705L;
    private ConcurrentSkipListSet<Integer> mprSelectorTable;

    public TCMessage() {
        mprSelectorTable = new ConcurrentSkipListSet<Integer>();
    }

    public TCMessage(ConcurrentSkipListSet<Integer> mprSelectorTable) {
        this.mprSelectorTable = mprSelectorTable;
    }

    public void setMPRs(ConcurrentSkipListSet<Integer> mprSelectorTable) {
        this.mprSelectorTable = mprSelectorTable;
    }

    public ConcurrentSkipListSet<Integer> getMprSelectorTable() {
        return mprSelectorTable;
    }
}
