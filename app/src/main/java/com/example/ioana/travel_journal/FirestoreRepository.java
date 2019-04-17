package com.example.ioana.travel_journal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Picture;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class FirestoreRepository {
    private static final String TRIPS_COLLECTION = "Trips";
    private static final String DESTINATION = "destination";
    private static final String ID = "id";
    private static final String ENDDATE = "endDate";
    private static final String PRICE = "price";
    private static final String RATING = "rating";
    private static final String STARTDATE = "startDate";
    private static final String TRIPTYPE = "tripType";
    private static final String PICTURE = "picture";
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext;
    private TextView mTextViewTasks;

    public FirestoreRepository(Context context) {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mContext = context;
    }

    public void addTrip(Trip item) {
        Map<String, Object> theItem = new HashMap<>();
        theItem.put(DESTINATION, item.getMDestination());
        theItem.put(ID,item.getMDocumentId());
        theItem.put(ENDDATE,item.getMEndDate());
        theItem.put(PICTURE,item.getMPicture());
        theItem.put(PRICE,item.getMPrice());
        theItem.put(RATING,item.getMRating());
        theItem.put(STARTDATE,item.getMStartDate());
        theItem.put(TRIPTYPE,item.getMTripType());
        //..... put pt toate valorile

        mFirebaseFirestore.collection(TRIPS_COLLECTION)
                .add(theItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        displayMessage(
                                "DocumentSnapshot added with ID: " + documentReference.getId());
                        readTrips();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayMessage("Error adding document");
                    }
                });
    }

    private void displayMessage(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    private void getTrips() {
        mFirebaseFirestore.collection(TRIPS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getId() + " => " + document.getData() + "\n");
                            }
                        } else {
                            displayMessage("Error getting documents.");
                        }
                    }
                });
    }

    private void readTrips() {
        mFirebaseFirestore.collection(TRIPS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mTextViewTasks.setText("");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mTextViewTasks
                                        .append(document.getId() + " => " + document.getData() + "\n");
                            }
                        } else {
                            displayMessage("Error getting documents.");
                        }
                    }
                });
    }
}
