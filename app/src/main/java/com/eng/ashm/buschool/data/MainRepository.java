package com.eng.ashm.buschool.data;

import androidx.lifecycle.MutableLiveData;

import com.eng.ashm.buschool.data.datamodel.DataListObservable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class for getting data from fireStore database
 */
public class MainRepository {

    // constants
    public static final int EMPTY = 10;
    public static final int AVAILABLE = 20;
    public static final int DATA_NOT_AVAILABLE = 30;
    public static final int DATA_AVAILABLE = 30;

    //fields
    private FirestoreDataSource firestoreDataSource = FirestoreDataSource.createInstance();
    private DataListObservable<Result<IFirestoreDataModel>> dataListObservable = firestoreDataSource.list;
    public final MutableLiveData<IFirestoreDataModel> resultLiveData = new MutableLiveData<>();

    private static CacheRepository cacheRepository;
    private static FirestroreRepository firestroreRepository;
    private static MainRepository repositoryInstance = null;
    ExecutorService executor = Executors.newCachedThreadPool();



    private MainRepository(){}
    /**
     * create instance from the MAinRepository class
     * @return
     */
    public static final MainRepository getInstance(){
        if (repositoryInstance == null)
            repositoryInstance = new MainRepository();
        firestroreRepository = FirestroreRepository.getInstance();
        cacheRepository = CacheRepository.getInstance();
        return repositoryInstance;
    }
    /**
     * add observers to the observable dataListObservable object to get notification
     * when FirestoreDataSource retrieve the data and add it to the observable.
     * it check if the retrieved result state: if succeed the add the data to
     * the liveData or
     */
    private void observeDataList(){
        dataListObservable.addObserver(((o, arg) -> {
            List<Result<IFirestoreDataModel>> list = (List<Result<IFirestoreDataModel>>) arg;
            for (Result<IFirestoreDataModel> result: list) {
                if(result.STATE == Result.SUCCEED){
                    resultLiveData.setValue(result.getResult());
                }
                else if(result.STATE == Result.ERROR)
                    resultLiveData.setValue(null);
            }
        }));
    }
    /**
     * check  if the data cached in the app room is exist ir not, or check
     * if the app room is empty or not
     * @return the state of the room
     */
    private int checkCacheData(){
        return 0;
    }
    /**
     * Cache/Room database handling -> data requests not
     * all the cache data.
     */

    /**
     * update the data cache after download data from
     * fireStore.
     */
    private void updateCache(){}

    /**
     * fill the cache with data loaded from fireStore
     */
    private void fillCache(){}
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
    public void requestData(){
    }
    public void requestList(Class<? extends IFirestoreDataModel> c){
    }
    public void searchData(String field, Object value){
    }
    public void updateData(IFirestoreDataModel updatedDataModel){
    }
    public void addData(IFirestoreDataModel dataModel){

    }
    public void deleteData(IFirestoreDataModel dataModel){
    }
}
