package com.eng.ashm.buschool.data;

import com.eng.ashm.buschool.data.datamodel.DataListObservable;

public class CacheRepository {

    public final DataListObservable requestListObservable = new DataListObservable();
    CacheDataSource cacheDataSource = CacheDataSource.createInstance();
    private static CacheRepository repositoryInstance = null;

    private CacheRepository(){}
    /**
     * create instance from the FirestoreRepository class
     * @return
     */
    public static final CacheRepository getInstance(){
        if (repositoryInstance == null)
            repositoryInstance = new CacheRepository();
        return repositoryInstance;
    }
    public void requestDataCache(){}
    public void requestListCache(){}
    public void updateDataCache(){}
    public void addDataCache(){}
    public void deleteDataCache(){}
    public void searchData(){}

}
