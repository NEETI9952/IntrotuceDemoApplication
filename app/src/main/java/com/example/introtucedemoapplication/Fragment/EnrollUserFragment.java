package com.example.introtucedemoapplication.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.introtucedemoapplication.Adapter.SectionPagerAdapter;
import com.example.introtucedemoapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EnrollUserFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    CollectionReference userCollectionReference=db.collection("Users");
    public static StorageReference storageReference;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    TextInputLayout firstName, lastName, dateOfBirth,gender,country,state, hometown, phoneNumber,telephoneNumber;
    ImageView profilePhoto;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar myCalendar;
    Button selectPhotoButton, addUserButton;
    Uri imageUri;
    Dialog dialog;
    ViewGroup root;
    View v;
    ProgressBar progressBar;

    final boolean[] alredyAUserPhone = new boolean[1];
    AutoCompleteTextView countryTextView;
    ArrayAdapter<String> countryArrayAdapter;
    String[] countriesList = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v != null) {
            root = (ViewGroup) v.getParent();
            if (root != null)
                root.removeView(v);
        }
        try {
            root= (ViewGroup) inflater.inflate(R.layout.fragment_enroll_user, container, false);
        } catch (InflateException e) {

        }

        countryTextView=root.findViewById(R.id.countryTextViewEnrolUser);
        firstName=root.findViewById(R.id.firstNameTextViewEnrolUser);
        lastName=root.findViewById(R.id.lastNameTextViewEnrolUser);
        dateOfBirth=root.findViewById(R.id.dateOfBirthTextViewEnrolUser);
        gender=root.findViewById(R.id.genderTextViewEnrolUser);
        country=root.findViewById(R.id.countryTextViewEnrolUserTIL);
        state=root.findViewById(R.id.stateTextViewEnrolUser);
        hometown=root.findViewById(R.id.hometownTextViewEnrolUser);
        phoneNumber=root.findViewById(R.id.phoneNumberTextViewEnrolUser);
        telephoneNumber=root.findViewById(R.id.telephoneNumberTextViewEnrolUser);
        profilePhoto=root.findViewById(R.id.profilePictureEnrolUser);
        selectPhotoButton=root.findViewById(R.id.selectProfilePhotoButtonEnrolUser);
        addUserButton=root.findViewById(R.id.materialButtonAddUser);
        progressBar=root.findViewById(R.id.progressBar);

        selectPhotoButton.setOnClickListener(this);
        addUserButton.setOnClickListener(this);

        countryArrayAdapter=new ArrayAdapter<>(getContext(),R.layout.countrydropdown,countriesList);
        countryTextView.setAdapter(countryArrayAdapter);

        gender.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGenderDialog();
            }
        });

        dateOfBirth.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myCalendar = Calendar.getInstance();
                final int year = myCalendar.get(Calendar.YEAR);
                final int month = myCalendar.get(Calendar.MONTH);
                final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });



        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                monthOfYear=monthOfYear+1;

                String chosendate= dayOfMonth +"-"+monthOfYear +"-"+ year;
                updateLabel(chosendate);
            }
        };

        return root;
    }



    private void updateLabel(String chosendate) {
        dateOfBirth.getEditText().setText(chosendate);
    }

    private void createGenderDialog() {

        dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.gender_alert_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.findViewById(R.id.btnGenderDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup radioGroup=(RadioGroup) dialog.findViewById(R.id.radioGroup);

                int checkId1 = radioGroup.getCheckedRadioButtonId();
                Button selectedGenderRB = dialog.findViewById(checkId1);

                if (selectedGenderRB!= null) {
                    gender.getEditText().setText(selectedGenderRB.getText().toString());
                    dialog.cancel();
                }else {
                    Toast.makeText(getContext(),"Please Select a gender",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations=R.style.Widget_AppCompat_PopupWindow;
        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.selectProfilePhotoButtonEnrolUser){
            selectPhoto();
        }else if(v.getId()==R.id.materialButtonAddUser){
            showProgressBar();
            checkUser();
        }
    }

    public void checkUser(){
        if (validateFirstName() && validateLastName() && validateDob() && validateCountry() && validateState() && validatePhoneNumber() && validateTelephoneNumber() && validateHomeTown() && validateGender()) {
            userCollectionReference.whereEqualTo("phoneNumber", phoneNumber.getEditText().getText().toString().trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            alredyAUserPhone[0] = task.getResult().size() > 0;


                            if (alredyAUserPhone[0]) {

                                hideProgressBar();
                                phoneNumber.setError("Phone Number already registered");
                            } else {
                                phoneNumber.setError(null);
                                phoneNumber.setErrorEnabled(false);
                                final boolean[] alreadyRegisteredTelephone = new boolean[1];
                                userCollectionReference.whereEqualTo("telephoneNumber", telephoneNumber.getEditText().getText().toString().trim())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                alreadyRegisteredTelephone[0] = queryDocumentSnapshots.size() > 0;

                                                if (alreadyRegisteredTelephone[0]) {
                                                    hideProgressBar();
                                                    telephoneNumber.setError("Telephone Number already registered");
                                                } else {
                                                    telephoneNumber.setError(null);
                                                    telephoneNumber.setErrorEnabled(false);

                                                    saveUserDetail();
                                                }
                                            }
                                        });
                            }

                        }
                    });
        }else{
            hideProgressBar();
        }
    }



    public void saveUserDetail() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName.getEditText().getText().toString().trim());
        userMap.put("lastName", lastName.getEditText().getText().toString().trim());
        userMap.put("dateOfBirth", dateOfBirth.getEditText().getText().toString().trim());
        userMap.put("gender", gender.getEditText().getText().toString().trim());
        userMap.put("country", country.getEditText().getText().toString().trim());
        userMap.put("state", state.getEditText().getText().toString().trim());
        userMap.put("homeTown", hometown.getEditText().getText().toString().trim());
        userMap.put("phoneNumber", phoneNumber.getEditText().getText().toString().trim());
        userMap.put("telephoneNumber", telephoneNumber.getEditText().getText().toString().trim());


        if (imageUri != null) {
            userMap.put("imageUrl", imageUri.toString());
        } else {

            userMap.put("imageUrl", "");
        }

        userMap.put("userCreationTimeStamp", Timestamp.now());

        userCollectionReference.document().set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                Toast.makeText(getContext(), "User Successfully Added!", Toast.LENGTH_SHORT).show();
                firstName.getEditText().setText("");
                lastName.getEditText().setText("");
                dateOfBirth.getEditText().setText("");
                gender.getEditText().setText("");
                country.getEditText().setText("");
                state.getEditText().setText("");
                hometown.getEditText().setText("");
                phoneNumber.getEditText().setText("");
                telephoneNumber.getEditText().setText("");
                profilePhoto.setImageResource(R.drawable.ic_baseline_image_24);
                hideProgressBar();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressBar();
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });

    }




    public void selectPhoto() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setIcon(R.drawable.addimage);
        builder.setMessage("Choose image source")
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cameraPermission();
                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryPermissions();
                    }
                 });
        builder.show();

    }

    private void galleryPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);

        } else {
            dispatchToGalleryIntent();

        }

    }

    private void dispatchToGalleryIntent() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
    }

    public void cameraPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

        } else {
            dispatchTakePictureIntent();

        }
//
    }
    private void dispatchTakePictureIntent () {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), "Please Wait!", Toast.LENGTH_SHORT).show();
        if(requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                profilePhoto.setImageBitmap(bitmap);
                profilePhoto.setTag("photo added");
                showProgressBar();
                handleUpload(bitmap);

            }
        }
        if (requestCode == GALLERY_REQUEST_CODE&&data!=null&&data.getData()!=null) {
            if (resultCode == Activity.RESULT_OK) {

                Uri uri=data.getData();
                try {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    profilePhoto.setImageBitmap(bitmap);
                    showProgressBar();
                    handleUpload(bitmap);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Photo not uploaded", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                    e.printStackTrace();
                }

            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        final String timeStamp = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date());
        storageReference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(timeStamp +"ProfilePicture" + ".jpeg");

        storageReference.putBytes(byteArrayOutputStream.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(storageReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBar();
                        Toast.makeText(getContext(), "Photo not uploaded", Toast.LENGTH_SHORT).show();
                        Log.e("testimageupload", e.getMessage(),e.getCause() );
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageUri=uri;
                        Toast.makeText(getContext(), "Photo uploaded", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private boolean validateFirstName(){
        String val= firstName.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            firstName.setError("Field cannot be empty");


            return false;
        }else{
            firstName.setError(null);
            firstName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLastName(){
        String val= lastName.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            lastName.setError("Field cannot be empty");

            return false;
        }else{
            lastName.setError(null);
            lastName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateGender(){
        String val= gender.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            gender.setError("Field cannot be empty");

            return false;
        }else{
            gender.setError(null);
            gender.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateDob(){
        String val=dateOfBirth.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            dateOfBirth.setError("Field cannot be empty");
            return false;
        }else{
            dateOfBirth.setError(null);
            dateOfBirth.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCountry(){
        String val=country.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            country.setError("Field cannot be empty");
            return false;
        }else{
            country.setError(null);
            country.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateHomeTown() {
        String val=hometown.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            hometown.setError("Field cannot be empty");
            return false;
        }else{
            hometown.setError(null);
            hometown.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateState(){
        String val=state.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            state.setError("Field cannot be empty");
            return false;
        }else{
            state.setError(null);
            state.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePhoneNumber(){
        String val=phoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            phoneNumber.setError("Phone Number cannot be empty");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }

    public boolean validateTelephoneNumber(){
        String val=telephoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            telephoneNumber.setError("Telephone Number cannot be empty");
            return false;
        } else{
                telephoneNumber.setError(null);
                telephoneNumber.setErrorEnabled(false);
                return true;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakePictureIntent();
                } else {

                }
                break;
            case GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchToGalleryIntent();
                } else {


                }
                break;

        }

    }
    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}