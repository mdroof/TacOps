package edu.svsu.tacops.tacops;

import android.app.Activity;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * Created by Topgun on 4/15/2017.
 */

public class SoundManager implements MediaPlayer.OnPreparedListener{
    // Create a storage reference from our app
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    // Create a reference with an initial file path and name
    //StorageReference pathReference = storageRef.child("images/stars.jpg");

    // Create a reference from an HTTPS URL
    // Note that in the URL, characters are URL escaped!
    //StorageReference httpsReference = storageRef.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");

    private static SoundManager instance = null;
    private Activity context;
    private MediaPlayer musicPlayer;
    private MediaPlayer voiceOverPlayer;

    public static SoundManager getInstance(Activity activity){
        if(instance == null){
            instance = new SoundManager(activity);
        }
        return instance;
    }

    private SoundManager(Activity context){
        this.context = context;

        musicPlayer = new MediaPlayer();
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        voiceOverPlayer = new MediaPlayer();
        voiceOverPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        fetchAudioUrlFromFirebase("draw");
    }

    public void parseAlertToSound(String obj, String event){
        switch(event){
            //case: "Scored a point!"
        }
    }

    private void fetchAudioUrlFromFirebase(String sound) {
        //String[] androidStrings = context.getResources();

        String sound_path = getStringResourceByName(sound);
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl(sound_path);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    // Download url of file
                    final String url = uri.toString();
                    musicPlayer.setDataSource(url);
                    // wait for media player to get prepare
                    musicPlayer.setOnPreparedListener(SoundManager.this);
                    musicPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })  .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", e.getMessage());
                    }
                });
    }

    private String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", context.getPackageName());
                //.getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
