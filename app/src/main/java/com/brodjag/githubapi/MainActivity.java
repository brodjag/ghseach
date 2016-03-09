package com.brodjag.githubapi;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
networkSet net;
    tabSet tab;
    String q="";

    public List<User> userList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      net=new networkSet(this);

        tab=   new tabSet(this);

    }


    //camunication with tabs fragment
    public HashMap<Integer,callFromActivity> tabCall =new HashMap<>();

public void onLoad(List<User> users, boolean succseful){

   if(succseful) {
       for(User user:users){
           userList.add(user);
       }
       updateTabs();

   }
}

void updateTabs(){
    // userList=users;
    for (int i=0; i<=3; i++){
        callFromActivity call=tabCall.get(i);
        if(call!=null){
            Log.d("www_","call "+i);
            call.update();
        }
    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    query = URLEncoder.encode(query, "UTF-8");
                    Log.d("qqq",query);
                } catch (UnsupportedEncodingException e) {e.printStackTrace();}
                //activity.searchSubmit(query);
               // ( MainActivity.fragment).makeSeachRequast(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userList.clear(); updateTabs();
                q=newText;
                if(q==""){return false;}
                net.loadItems();
                return false;
            }
        });

        return true;
    }



}
