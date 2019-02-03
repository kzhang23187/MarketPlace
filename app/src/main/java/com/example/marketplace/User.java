package com.example.marketplace;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class User {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User() {
    }
    /**
     * CREATES User and ADDS to database.
     *
     * @param name     user name
     * @param email    user email
     * @param phone    user phone
     * @param location user location
     */
    public static void createUser(String id, String name, String email, String phone) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        db.collection("users").document(id).set(user);
    }
    /**
     * Gets a User. Most likely used only for retrieving a single user data (the current user).
     *
     * @param userId the string of the userID.
     */
    public static void getUser(String userId) {

        DocumentReference docRef = db.collection("users").document(userId);

        //GETS USER INFO
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                Log.d(TAG, (String) ds.get("name"));
            }
        });
    }


}
