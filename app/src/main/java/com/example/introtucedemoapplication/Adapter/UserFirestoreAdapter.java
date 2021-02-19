package com.example.introtucedemoapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.introtucedemoapplication.ModelClass.User;
import com.example.introtucedemoapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class UserFirestoreAdapter extends FirestoreRecyclerAdapter<User,UserFirestoreAdapter.UserVH> {
    Context context;


    public UserFirestoreAdapter(Context context,@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserVH holder, int position, @NonNull User model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAdapterPosition());
        model.setUserUid(documentSnapshot.getId());
        holder.userName.setText(model.getFirstName() + " " + model.getLastName());

        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        Date birthdate = null;
        try {
            birthdate = df.parse(model.getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar dob = Calendar.getInstance();
        dob.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        holder.userDetails.setText(model.getGender() + " | " + String.valueOf(age) + " | " + model.getHomeTown());

        if(model.getImageUrl()!="" ){
            Glide.with(context).load(model.getImageUrl()).into(holder.profileImage);
        }



        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String docUid= String.valueOf(model.getUserUid());
                                FirebaseFirestore db= FirebaseFirestore.getInstance();
                                CollectionReference usersCollectionReference=db.collection("Users");
                                usersCollectionReference.document(docUid).delete();
                                Toast.makeText(context,"User deleted!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_layout, parent, false);
        return new UserVH(v);
    }


    public static class UserVH extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userDetails;
        ImageView deleteButton,profileImage;

        public UserVH(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.textViewUserName);
            userDetails=itemView.findViewById(R.id.textViewUserDetail);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            profileImage=itemView.findViewById(R.id.profilePictureImageView);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
