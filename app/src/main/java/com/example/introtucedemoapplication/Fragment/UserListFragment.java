package com.example.introtucedemoapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.introtucedemoapplication.ModelClass.User;
import com.example.introtucedemoapplication.R;
import com.example.introtucedemoapplication.Adapter.UserFirestoreAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserListFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserFirestoreAdapter userFirestoreAdapter;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    CollectionReference userCollectionReference=db.collection("Users");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_user_list, container, false);

        recyclerView=root.findViewById(R.id.recyclerView);

        setRecyclerView();

        return root;
    }

    private void setRecyclerView() {
        Query query= userCollectionReference.orderBy("userCreationTimeStamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        userFirestoreAdapter = new UserFirestoreAdapter(getContext(), options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userFirestoreAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        userFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userFirestoreAdapter.stopListening();
    }
}