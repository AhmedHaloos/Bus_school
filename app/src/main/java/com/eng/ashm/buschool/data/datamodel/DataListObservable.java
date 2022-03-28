package com.eng.ashm.buschool.data.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class DataListObservable<T> extends Observable {

    private List<T> observables;

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
        notifyObservers(t);
    }
    /**
     *
     * @param list
     */
    public void updateList(List< ? extends T>list){
        if (!observables.isEmpty())
            observables.clear();
        observables.addAll(list);
        setChanged();
        notifyObservers(observables);

    }

    public boolean isListEmpty(){
        if (observables.size() <= 0)
            return true;
        return false;
    }
    /**
     *
     */
    public void clearList(){
        if (observables != null)
            observables.clear();
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
