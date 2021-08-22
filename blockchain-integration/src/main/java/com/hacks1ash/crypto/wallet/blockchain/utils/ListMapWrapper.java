package com.hacks1ash.crypto.wallet.blockchain.utils;

import java.util.AbstractList;
import java.util.List;
import java.util.Map;

public abstract class ListMapWrapper<X> extends AbstractList<X> {

    public final List<Map<String, ?>> list;
    
    public ListMapWrapper(List<Map<String, ?>> list) {
        this.list = list;
    }

    protected abstract X wrap(Map<String, ?> m);

    @Override
    public X get(int index) {
        return wrap(list.get(index));
    }

    @Override
    public int size() {
        return list.size();
    }

}