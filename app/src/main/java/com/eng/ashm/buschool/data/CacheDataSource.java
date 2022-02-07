package com.eng.ashm.buschool.data;

public class CacheDataSource {

    private static CacheDataSource cacheDataSource = null;

    public static CacheDataSource createInstance(){
        if (cacheDataSource == null)
            cacheDataSource = new CacheDataSource();
        return cacheDataSource;
    }
    /**
     *
     */
    public void addData(){}
    public void requestData(){}
    public void requestDataList(){}
    public void updateData(){}
    public void deleteData(){}
    public void searchData(){}

}
