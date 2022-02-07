package com.eng.ashm.buschool.data.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class DataListObservable<T> extends Observable {

    List<T> observables;

    public DataListObservable(){
        observables = new ArrayList<>();
    }

    /**
     * add the element to the end of the list
     * @param t element to be added
     */
    public void addElement(T t){
        observables.add(t);
        setChanged();
        notifyObservers(observables);
    }

    /**
     *
     * @return
     */
    public List<T> getList(){
        return observables;
    }
    /**
     * return
     * @param index of the requested element
     * @return the element specified by the index from the observable list
     */
    public T getElement(int index){
        return observables.get(index);
    }

    /**
     *
     * @return the last element from the observable list
     */
    public T getLastElement(){
        return observables.get(observables.size() - 1);
    }
}
