package com.example.buynlarge;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.buynlarge.DB.UserRepository;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> getAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        getAllUsers = userRepository.getAllUsers();
    }

    public void insert(User... users){
        userRepository.insert(users);
    }

    public void update(User... users){
        userRepository.insert(users);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public LiveData<List<User>> getAllUsers(){
        return getAllUsers;
    }
}
