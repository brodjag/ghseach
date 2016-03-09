package com.brodjag.githubapi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class BlankFragment extends Fragment {

    private static final String ARG_FILTER = "filter_id";



    private int mPosition;
    private RecyclerView rv;
    private RVAdapter rvAdapter;



    public BlankFragment() {}


    public static BlankFragment newInstance(int position) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FILTER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_FILTER);
            
        }
    }

   // private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListenerToActivity();
        // Inflate the layout for this fragment
        Log.d("www_","created= "+mPosition);
        View view = inflater.inflate(R.layout.fragment_blank, null, false);
        rv=(RecyclerView) view.findViewById(R.id.rv);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        //todo set default items
        rvAdapter=new RVAdapter();
        rv.setAdapter(rvAdapter);
        rv.setHasFixedSize(false);


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    // if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        //    loading = false;
                            Log.v("mu ", "Last Item Wow !");
                            ((MainActivity)getActivity()).net.loadItems();
                            //Do pagination.. i.e. fetch new data
                       // }
                    }
                }
            }
        });

        return view;
    }




    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
        private List<User> records;

        public void UpdateItems(){
            if(records!=null)records.clear();
            records=new ArrayList<>();
            char f='a',l='z';
            records.clear();
            if(mPosition==0){f='a';l='h';}
            if(mPosition==1){f='i';l='p';}
            if(mPosition==2){f='q';l='z';}

            for (User user : ((MainActivity) getActivity()).userList){
                char first= user.login.toLowerCase().charAt(0);
                if((first>=f)&&(first<=l)){
                       records.add(user);
                }

            }
            //todo sort
            //records.clear();

            notifyDataSetChanged();
          //  for (User user : ((MainActivity) getActivity()).userList) {Log.d("www_a", user.login);}
        }

        public RVAdapter() {
        //    this.records = records;
            UpdateItems();
        }


        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new PersonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personName.setText(records.get(position).login);
            Picasso.with(getContext()).load(records.get(position).avatar_url).into(holder.personPhoto);
            ((MainActivity) getActivity()).net.getUserInfo(records.get(position).login,holder.personName,holder.sub);
        }

        @Override
        public int getItemCount() {
            if(records==null)return 0;
            return records.size();
        }

        public  class PersonViewHolder extends RecyclerView.ViewHolder {

            TextView personName;
            ImageView personPhoto;
            TextView sub;

            PersonViewHolder(View itemView) {
                super(itemView);
                personName = (TextView)itemView.findViewById(R.id.user_name);
                personPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
                sub = (TextView)itemView.findViewById(R.id.user_sub);
            }
        }

    }




     void setListenerToActivity() {
        ((MainActivity) getActivity()).tabCall.put(mPosition, new callFromActivity() {
            @Override
            public void update() {
                //rvAdapter.setItems(((MainActivity) getActivity()).userList);
                rvAdapter.UpdateItems();
                rvAdapter.notifyDataSetChanged();
                Log.d("www_", "update " + mPosition);

            }
        });
    }


     void removeListereToActivity() {
        ((MainActivity) getActivity()).tabCall.remove(mPosition);
    }

    @Override
    public void onDestroy(){
        removeListereToActivity();
        super.onDestroy();
    }
}

interface callFromActivity{
    void update();
}
