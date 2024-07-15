package com.progmeleon.musicplayer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//////////////////////////////////////////////

public class MainActivity extends AppCompatActivity implements OnItemClickListener{
    ArrayList<AudioModel> audioModelList = new ArrayList<>();
    RecyclerView recyclerView;
    AudioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioAdapter = new AudioAdapter(audioModelList, MainActivity.this);
        recyclerView.setAdapter(audioAdapter);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //        To get all audios files from android device.
                       fetchSongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void fetchSongs(){
        ContentResolver contentResolver = getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
        Cursor cursor = contentResolver.query(audioUri, projection, null, null, null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                audioModelList.add(new AudioModel(title, path));
            }
            cursor.close();
        }
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, PlaySong.class);
        String titleCurrent = audioModelList.get(position).getTitle();
//        Log.d("myTag", titleCurrent);
//        intent.putExtra("songList", (Parcelable) audioModelList);
        Bundle bundle = new Bundle();
        bundle.putSerializable("songList", audioModelList);
        intent.putExtras(bundle);
        intent.putExtra("currentSong", titleCurrent);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}

//package com.progmeleon.musicplayer2;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//    List<AudioModel> audioModelList = new ArrayList<>();
//    RecyclerView recyclerView;
//    AudioAdapter audioAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        audioAdapter = new AudioAdapter(audioModelList);
//        recyclerView.setAdapter(audioAdapter);
//
////        To get all audios files from android device.
//        fetchAudioFiles();
//    }
//
//    private void fetchAudioFiles() {
//        ContentResolver contentResolver = getContentResolver();
//        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
//        Cursor cursor = contentResolver.query(audioUri, projection, null, null, null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()){
//                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
//                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                audioModelList.add(new AudioModel(title, path));
//            }
//            cursor.close();
//        }
//
//        // Notify the adapter that the data set has changed
//        audioAdapter.notifyDataSetChanged();
//    }
//}
