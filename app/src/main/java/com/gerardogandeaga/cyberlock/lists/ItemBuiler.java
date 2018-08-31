package com.gerardogandeaga.cyberlock.lists;

import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-24
 *
 * this interface ensures methods to create items for FastItemAdapters
 * used in a recycler view
 */
public interface ItemBuiler<Item, Obj> {

    /**
     * @param obj objects needed for the creation of an item
     * @return adapter item
     */
    Item buildItem(Obj obj);

    /**
     * @param objList list of objects that items need
     * @return list of adapter items
     */
    List<Item> buildItems(List<Obj> objList);
}
