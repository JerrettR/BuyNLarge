package com.example.buynlarge.DB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.buynlarge.User;
import java.util.List;

public class UserRepository {
    private UserDAO userDAO;
    private LiveData<List<User>> getAllUsers;

    public UserRepository(Application application){
        AppDataBase database = AppDataBase.getInstance(application);
        userDAO = database.getUserDAO();
        getAllUsers = userDAO.getAllUsers();
    }

    public void insert(User... users){
        new InsertUserAsyncTask(userDAO).execute(users);
    }

    public void update(User... users){
        new UpdateUserAsyncTask(userDAO).execute(users);
    }

    public void delete(User user){
        new DeleteUserAsyncTask(userDAO).execute(user);
    }

    public LiveData<List<User>> getAllUsers(){
        return getAllUsers;
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private InsertUserAsyncTask(UserDAO userDAO){
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users){
            userDAO.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private UpdateUserAsyncTask(UserDAO userDAO){
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users){
            userDAO.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private DeleteUserAsyncTask(UserDAO userDAO){
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users){
            userDAO.delete(users[0]);
            return null;
        }
    }
}
