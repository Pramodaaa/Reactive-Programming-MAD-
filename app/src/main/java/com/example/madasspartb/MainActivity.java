package com.example.madasspartb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madasspartb.fragments.SingleColumnFragment;
import com.example.madasspartb.fragments.TwoColumnFragment;
import com.example.madasspartb.utility.GlobalClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Button loadImageBtn,changeViewBtn,uploadBtn;
    ProgressBar progressBar;
    EditText searchText;
    GlobalClass sharedData;
    SingleColumnFragment singleColumnFragment = new SingleColumnFragment();
    TwoColumnFragment twoColumnFragment = new TwoColumnFragment();
    int currentFragment = 1;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadImageBtn = findViewById(R.id.loadImageBtn);
        changeViewBtn = findViewById(R.id.changeViewBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        progressBar = findViewById(R.id.progressBarId);
        searchText = findViewById(R.id.inputSearch);

        sharedData = GlobalClass.getInstance();
        sharedData.setToUploadList(Collections.emptyList());
        sharedData.setImageList(Collections.emptyList());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        progressBar.setVisibility(View.INVISIBLE);

        loadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchImage();
            }
        });

        changeViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDisplayView();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sharedData.getToUploadList().isEmpty()) {
                    uploadImage();
                } else {
                    Toast.makeText(MainActivity.this, "Please search and select first", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void uploadImage() {
        List<Bitmap> uploadList = sharedData.getToUploadList();

        for(Bitmap image : uploadList) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray(); // converting image to byte to upload

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }



    }

    public void changeDisplayView() {
        if (currentFragment == 1) {
            currentFragment = 2;
            getSupportFragmentManager().beginTransaction().replace(R.id.imageDisplayFragment,twoColumnFragment).commit();
        } else {
            currentFragment = 1;
            getSupportFragmentManager().beginTransaction().replace(R.id.imageDisplayFragment,singleColumnFragment).commit();
        }
    }

   public void searchImage() {
       Toast.makeText(MainActivity.this, "Searching starts", Toast.LENGTH_SHORT).show();
       progressBar.setVisibility(View.VISIBLE);
       SearchTask searchTask = new SearchTask(MainActivity.this);
       searchTask.setSearchkey(searchText.getText().toString());

       Single<String> searchObservable = Single.fromCallable(searchTask);
       searchObservable = searchObservable.subscribeOn(Schedulers.io());
       searchObservable = searchObservable.observeOn(AndroidSchedulers.mainThread());
       searchObservable.subscribe(new SingleObserver<String>() {
           @Override
           public void onSubscribe(@NonNull Disposable d) {

           }

           @Override
           public void onSuccess(@NonNull String s) {
               Toast.makeText(MainActivity.this, "Searching Ends", Toast.LENGTH_SHORT).show();
               progressBar.setVisibility(View.INVISIBLE);
               loadImage(s);
           }

           @Override
           public void onError(@NonNull Throwable e) {
               Toast.makeText(MainActivity.this, "Searching Error", Toast.LENGTH_SHORT).show();
               progressBar.setVisibility(View.INVISIBLE);
           }
       });
    }

    public void loadImage(String response){
        ImageRetrievalTask imageRetrievalTask = new ImageRetrievalTask(MainActivity.this);
        imageRetrievalTask.setData(response);

        Toast.makeText(MainActivity.this, "Image loading starts", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Observable<ArrayList<Bitmap>> searchObservable = Observable.fromCallable(imageRetrievalTask);
        searchObservable = searchObservable.subscribeOn(Schedulers.io());
        searchObservable = searchObservable.observeOn(AndroidSchedulers.mainThread());
        searchObservable.subscribe(new Observer<ArrayList<Bitmap>>() {


            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList<Bitmap> bitmaps) {
                List<Bitmap> newImageList = new ArrayList<>();
                for (Bitmap image: bitmaps) {
                    newImageList.add(image);

                }
                if (!newImageList.isEmpty()) {
                    sharedData.setImageList(newImageList);
                }

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Image loading Ends", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.imageDisplayFragment,singleColumnFragment).commit();

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(
                        MainActivity.this,
                "Image loading error, search again",
                        Toast.LENGTH_SHORT
                ).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });


    }
}