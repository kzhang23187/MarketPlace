package com.example.marketplace;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Product {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    private static ArrayList<Map<String, Object>> products;

    /**
     * Adds the product image to storage by the File reference of the android camera.
     * @param image the File object of the product image.
     */
    public static void addProductImage(File image) {
        StorageReference storageRef = storage.getReference();
        // Uploads file to images/ folder in database
        Uri file = Uri.fromFile(image);
        StorageReference imageRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    /**
     * Creates a new product.
     * @param name the name of the product.
     * @param condition the condition of the product.
     * @param description the description of the product.
     * @param type the type of the product.
     * @param price the price of the product.
     * @param image_url the download url of the image of the product.
     */
    public static void createProduct(int year, int month, String contact, String name, String condition, String description, String type, int price, String image_url) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("condition", condition);
        product.put("description", description);
        product.put("type", type);
        product.put("price", price);
        product.put("image", image_url);
        product.put("contact", contact);
        product.put("year", year);
        product.put("month", month);
        db.collection("products")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * gets the products from the database and calls addproduct to add it to list of products.
     */
    public static ArrayList<Map<String, Object>> getProductsByStringField(String field, String fieldValue) {
        products.clear();
        db.collection("products").whereEqualTo(field, fieldValue)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addProduct(document.getData());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return getProducts();
    }
    public static ArrayList<Map<String, Object>> getProductsByMaxPrice(int maxPrice) {
        products.clear();
        db.collection("products").whereLessThan("price", maxPrice)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addProduct(document.getData());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return getProducts();
    }
    public static ArrayList<Map<String, Object>> getProductsByMaxYear(int maxYear) {
        products.clear();
        db.collection("products").whereEqualTo("year", maxYear)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addProduct(document.getData());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return getProducts();
    }

    /**
     * Adds a product to the arraylist of products.
     * @param productMap the map of products.
     */
    public static void addProduct(Map<String, Object> productMap) {
        products.add(productMap);
    }

    public static ArrayList<Map<String, Object>> getProducts() {
        return products;
    }
}
