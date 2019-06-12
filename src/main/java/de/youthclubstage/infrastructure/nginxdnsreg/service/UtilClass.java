package de.youthclubstage.infrastructure.ngnixdnsreg.service;

import java.util.ArrayList;
import java.util.Collection;

public class UtilClass {
    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }
}
