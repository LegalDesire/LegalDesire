package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.BookmarksRecyclerAdapter;
import com.example.user.legaldesire.models.BookmarkDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BookmarkClientFragment extends Fragment {

    RecyclerView bookmarkRecycler;
    RecyclerView.Adapter bookmarksRecyclerAdapter;
    private  View view=null;
    private  LayoutInflater mInflater;
    private ViewGroup mContainer;
    Context mContext;
    public BookmarkClientFragment() {
        // Required empty public constructor
    }
    List<BookmarkDataModel> bookmarkLinks = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContainer = container;
       mInflater = inflater;
         view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        bookmarkRecycler = view.findViewById(R.id.bookmarkRecycler);
        bookmarkRecycler.setHasFixedSize(true);
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            loadData();
        }
    }

    public void loadData(){
        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = fdatabase.getReference().child("Lawyers").child(FirebaseAuth.getInstance()
                .getCurrentUser().getEmail().replace(".",",")).child("bookmarks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookmarkLinks.clear();
                if(dataSnapshot.getChildrenCount()==0)
                {
                    Toast.makeText(getContext(),"You haven't added any bookmarks!",Toast.LENGTH_SHORT).show();
                }else
                {
                    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                        Log.e("coming here",dataSnapshot1.toString());
                        BookmarkDataModel bookmarkDataModel = new BookmarkDataModel(dataSnapshot1.getKey(),dataSnapshot1.child("link").getValue().toString(),
                                dataSnapshot1.child("title").getValue().toString());
                        bookmarkLinks.add(bookmarkDataModel);
                        //bookmarkLinks.add(dataSnapshot1.getValue().toString());
                        bookmarksRecyclerAdapter = new BookmarksRecyclerAdapter(bookmarkLinks,getContext());
                        bookmarkRecycler.setAdapter(bookmarksRecyclerAdapter);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}