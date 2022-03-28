package com.eng.ashm.buschool.data;

import android.content.Context;
import android.widget.Toast;

import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.LoggedInUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class for getting data from fireStore database.
 * determine the datasource to get the data from
 * provide the result data in observables
 */
public class MainRepository {

    public final DataListObservable<IDataModel> mainRequestListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> mainSearchListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> mainDataObservable = new DataListObservable();
    public final DataListObservable<Boolean> mainAddObservable = new DataListObservable();
    public final DataListObservable<Boolean> mainUpdateObservable = new DataListObservable();
    public final DataListObservable<Boolean> mainDeleteObservable = new DataListObservable();

    //fields
    public DataListObservable<IDataModel> dataListObservable = new DataListObservable<>();

    private static CacheRepository cacheRepository;
    private static FirestroreRepository firestroreRepository = null;
    private static MainRepository repositoryInstance = null;
    private Context context;
    private Object value = null;
    private Class<? extends IDataModel> customClass = null;
    ExecutorService executor = Executors.newCachedThreadPool();



    private MainRepository(Context context){
        this.context = context;
    }
    /**
     * create instance from the MAinRepository class
     * @return
     */
    public static final MainRepository getInstance(Context context){
        if (repositoryInstance == null){
            repositoryInstance = new MainRepository(context);
            firestroreRepository = FirestroreRepository.getInstance();
            cacheRepository = CacheRepository.getInstance(context);
       }
        return repositoryInstance;
    }
    /**
     * Cache/Room database handling -> data requests not
     * all the cache data.
     */
    private boolean addDataCache(IDataModel dataModel){
        return cacheRepository.addDataCache(dataModel);
    }
    private boolean updateDataCache(IDataModel updatedData){
        return cacheRepository.updateDataCache(updatedData);
    }
    private boolean deleteDataCache(IDataModel deletedData){
        return cacheRepository.deleteDataCache(deletedData);
    }
    private List<? extends IDataModel> requestDataListCache(Class<? extends IDataModel> customClass){
        return cacheRepository.requestDataList(customClass);
    }
    private List<? extends IDataModel> searchDataCache(Object searchCondition, Class<? extends IDataModel> customClass){
        return cacheRepository.searchData(searchCondition, customClass);
    }

    /**
     * update the data cache after download data from
     * fireStore.
     * @param updatedList:
     */
    private void updateCache(List< ? extends IDataModel> updatedList){
        if (updatedList.isEmpty() || updatedList == null)
            return;
        List<? extends IDataModel> cacheList = cacheRepository.requestDataList(updatedList
                .get(0).getClass());
        for (IDataModel dataModel: cacheList){
            cacheRepository.deleteDataCache(dataModel);
        }
        for (IDataModel dataModel : updatedList){
            cacheRepository.addDataCache(dataModel);
        }
    }

    // FireStore db handling

    /**
     *
     * @param requestClass
     */
    private void requestDataListOnline(Class<? extends IDataModel> requestClass){
        firestroreRepository.requestListFirestore(requestClass);
        firestroreRepository.requestListObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                mainRequestListObservable.updateList((List<? extends IDataModel>)arg);
                updateCache((ArrayList<? extends IDataModel>)arg);
            }
        });
    }
    /**
     *
     * @param addedData
     */
    private void addDataOnline(IDataModel addedData){
        firestroreRepository.addDataFirestore(addedData);
        firestroreRepository.addObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg != null)
                mainAddObservable.addElement(true);
                else mainAddObservable.addElement(false);
            }
        });
    }
    /**
     *
     * @param deletedData
     */
    private void deleteDataOnline(IDataModel deletedData){
        firestroreRepository.addDataFirestore(deletedData);
        firestroreRepository.deleteObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg != null)
                    mainDeleteObservable.addElement(true);
                else mainDeleteObservable.addElement(false);
            }
        });

    }
    /**
     *
     * @param updatedData
     */
    private void updateDataOnline(IDataModel updatedData){
        firestroreRepository.addDataFirestore(updatedData);
        firestroreRepository.updateObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg != null)
                    mainUpdateObservable.addElement(true);
                else mainUpdateObservable.addElement(false);
            }
        });
    }


    /**
     * Firestore database handling -> data requests not all the
     * fireStore data
     */

    /**
     * handling data interfaced to viewModel
     *  1 - check cache state
     *      - if empty then download data from fireStore to
     *      cache and to view model
     *      - if cache is filled then download data to update
     *      cache and view model.
     *      - if app is offline then get data from cache if
     *      available, if not the notify the user that he needs to
     *      be online.
     */
    public void requestData(String collection, String document, Class<? extends IDataModel>c){
        firestroreRepository.requestDataFirestore(collection, document, c);
        firestroreRepository.resultDataObservable.addObserver(resultDataObserver);
    }
    public void requestList(Class<? extends IDataModel> c){
        customClass = c;
        firestroreRepository.requestListFirestore(c);
        firestroreRepository.requestListObservable.addObserver(itemListObserver);
    }
    /**
     *
     * @param value
     * @param customClass
     */
    public void searchData(Object value, Class<? extends IDataModel> customClass){
        this.value = value;
        this.customClass = customClass;
        firestroreRepository.requestListFirestore(customClass);
        firestroreRepository.requestListObservable.addObserver(itemSearchObserver);
    }
    /**
     *
     * @param updatedDataModel
     */
    public void updateData(IDataModel updatedDataModel){
        cacheRepository.updateDataCache(updatedDataModel);
        firestroreRepository.updateDataFirestore(updatedDataModel, updatedDataModel.getClass());
        firestroreRepository.updateObservable.addObserver(itemUpdateObserver);
    }
    /**
     *
     * @param dataModel
     */
    public void addData(IDataModel dataModel){
        cacheRepository.addDataCache(dataModel);
        firestroreRepository.addDataFirestore(dataModel);
        firestroreRepository.addObservable.addObserver(itemAddObserver);
    }
    /**
     *
     * @param dataModel
     */
    public void deleteData(IDataModel dataModel){
        cacheRepository.deleteDataCache(dataModel);
        firestroreRepository.deleteDataFirestore(dataModel);
        firestroreRepository.deleteObservable.addObserver(itemDeleteObserver);
    }
    /**
     *
     * @param username
     * @param password
     */
    public void login(String username, String password){
        firestroreRepository.requestDataFirestore(LoggedInUser.COLLECTION, "/"+username + "-" + password, LoggedInUser.class);
        firestroreRepository.resultDataObservable.addObserver(itemRetrievedObserver);
    }
    /**
     *
     * @param username
     * @param password
     */
    public void logout(String username, String password){

    }
    /**
     ********************* Observers *******************************
     */
    /**
     * result data observer
     */
    private Observer resultDataObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if (arg != null){
                mainDataObservable.addElement((IDataModel) arg);
            }
            else
                mainDataObservable.addElement(null);
        }
    };
    /**
     * add observer
     */
    private Observer itemAddObserver = (o, arg) -> {
        if (arg != null)
            mainAddObservable.addElement(true);
        else mainAddObservable.addElement(false);
    };
    /**
     *
     */
    private Observer itemUpdateObserver = (o, arg) -> {
        if (arg != null)
            mainUpdateObservable.addElement(true);
        else mainUpdateObservable.addElement(false);
    };
    /**
     *
     */
    private Observer itemDeleteObserver = (o, arg) -> {
        if (arg != null)
            mainDeleteObservable.addElement(true);
        else mainDeleteObservable.addElement(false);
    };
    /**
     *
     */
    private Observer itemListObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            List<? extends IDataModel> resultList = (List<? extends IDataModel>) arg;
            if (arg != null) {
                mainRequestListObservable.updateList(resultList);
                updateCache(resultList);
            }
            else {
                mainRequestListObservable.updateList(cacheRepository.requestDataList(customClass));
            }
        }
    };
    /**
     *
     */
    private Observer itemSearchObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if (arg != null){
                List<? extends IDataModel> resultList = (List<? extends IDataModel>)arg;
                updateCache(resultList);
                List<? extends IDataModel> searchListResult = cacheRepository.searchData(value, customClass);
                if(searchListResult != null || !searchListResult.isEmpty())
                mainSearchListObservable.updateList(searchListResult);
            }
            else {
                mainSearchListObservable.updateList(null);
            }

        }
    };
    /**
     *
     */
    private Observer itemRetrievedObserver = (o, arg) -> {

        if (arg != null)
            mainDataObservable.addElement((IDataModel) arg);
        else
            mainDataObservable.addElement(null);
    };

}
