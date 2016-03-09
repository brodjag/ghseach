package com.brodjag.githubapi;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by brodjag on 04.03.16.
 */
public class networkSet {
   public static String LOGIN="brodjag";
    public static  String PASSWORD="***";


    MainActivity activity;
   public networkSet(MainActivity mainActivity){
       activity=mainActivity;

           /*
        UserService userService = ServiceGenerator.createService(UserService.class);
        Call<List<User>> call= userService.getUsers(2);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                for(User user:response.body()){
                    Log.d("www",user.login);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        */

   }


boolean loading=true;
public void loadItems(){
if(!loading)return;
    loading=false;

    int id=1;
    if(activity.userList.size()>0) id=activity.userList.size()/100+1;
    if(id>10){return;} //api can give 1k result max
    Log.d("page_id",""+id);
    UserService userService = ServiceGenerator.createService(UserService.class);
    Call<seachUserRes> call= userService.getUsers(activity.q, id, 100, LOGIN, PASSWORD);

    final String cQ=activity.q;
    call.enqueue(new Callback<seachUserRes>() {
        @Override
        public void onResponse(Call<seachUserRes> call, Response<seachUserRes> response) {
            loading=true;
            if (!cQ.equals(activity.q)){loadItems(); return;} //not actual answer
            Log.d("www", "body " + response.body());
            if(response.body()==null){Toast.makeText(activity," no items",Toast.LENGTH_LONG).show(); return;}
            if(response.body().items==null){Toast.makeText(activity," no items",Toast.LENGTH_LONG).show(); return;}
            activity.onLoad(response.body().items,true);


           // activity.net.onLoaded(response);
        }

        @Override
        public void onFailure(Call<seachUserRes> call, Throwable t) {
            loading=true;
            activity.onLoad(null,true);
            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();

        }
    });

}

public void getUserInfo(String login, final TextView name, final TextView sub){
        UserInfoService userInfoService=ServiceGenerator.createService(UserInfoService.class);
        Call<UserInfo> call= userInfoService.getUserInfo(login,LOGIN,PASSWORD);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo user=response.body();
                if(user==null){
                    Log.d("www", "name_info=null");
                    return;
                }
                name.setText(user.name);
                sub.setText(user.followers+"/"+user.following);


            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }



    public interface UserInfoService {
        @GET("users/{user_name}") //?since={num}
        Call<UserInfo> getUserInfo(@Path("user_name") String userName,@Query("client_id")String client_id,@Query("client_secret")String client_secret); //@Path("user") String user
    }

    public interface UserService {
        ///?client_id=brodjag&client_secret=coolcool11
        @GET("search/users") //?since={num}
        Call<seachUserRes> getUsers(@Query("q") String q, @Query("page") int since, @Query("per_page") int per_page,  @Query("client_id")String client_id,@Query("client_secret")String client_secret); //@Path("user") String user
    }

}

 class User{
    String login;
    String id;
    String avatar_url;
}




class seachUserRes{
    List<User> items;
}

class UserInfo{
    String login;
    String id;
    String avatar_url;
    String name;
    String followers;
    String following;
}
