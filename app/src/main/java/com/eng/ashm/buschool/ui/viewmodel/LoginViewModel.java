package com.eng.ashm.buschool.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.util.Patterns;
import android.widget.Toast;

import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.LoggedInUser;
import com.eng.ashm.buschool.data.datamodel.Parent;

import java.util.Observable;
import java.util.Observer;

public class LoginViewModel extends AndroidViewModel {


    private MainRepository repository = MainRepository.getInstance(getApplication().getApplicationContext());
    public MutableLiveData<IDataModel> loggedInUser = new MutableLiveData<>();
    public MutableLiveData<IDataModel> loggedUserObservable = new MutableLiveData<>();
    public MutableLiveData<Boolean> addLoggedUserObservable = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
    /**
     *
     * @param collection
     * @param phone
     * @param email
     */
    public void requestLoggedUser(String collection, String phone, String email){
        String document = "/" + phone + "-" + email;
        repository.requestData(collection, document, Parent.class);
        repository.mainDataObservable.addObserver(loggedInUserObserver);
    }
    public void addLoggedInUser(IDataModel dataModel){
        repository.addData(dataModel);
        repository.mainAddObservable.addObserver(addLoggedInObserver);
    }
    /**
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        repository.login(username, password);
        repository.mainDataObservable.addObserver(loginObserver);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    /*********** observers *************/
    /**
     * used to observe for the login result and monitor if the user is authenticated
     */
    Observer loginObserver = (o, arg) -> {
      if (arg != null) 
              loggedInUser.setValue((LoggedInUser) arg);
          else
          loggedInUser.setValue(null);
          repository.mainDataObservable.deleteObservers();
  };
    /**
     * add loggedIn observer
     */
    Observer loggedInUserObserver = (o, arg) -> {
        if (arg != null) {
            loggedUserObservable.setValue((IDataModel) arg);
            }
            loggedInUser.setValue(null);
        repository.mainAddObservable.deleteObservers();
        };
    /**
     * observer for adding the authentication username and password
     * of a user to the database
     */
    Observer addLoggedInObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if (arg instanceof Boolean){
                if (((Boolean) arg) == true)
                    addLoggedUserObservable.setValue(true);
                else
                    addLoggedUserObservable.setValue(false);

            }
        }
    };

}