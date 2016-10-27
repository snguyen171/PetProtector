package edu.orangecoastcollege.cs273.snguyen171.petprotector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;
    private static final int REQUEST_CODE = 100;

    // This member variable stores the URI to whatever image has been selected
    // Default: none.png (R.drawable.non)
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        // Hook up the petImageView to the layout:
        petImageView = (ImageView) findViewById(R.id.petImageView);

        // Constructs a full URI to any Android resource (id, drawable, color, layout, etc)
        //imageURI = getUriToResource(this, R.drawable.none);

        // Set the imageRUI of the ImageView in code
        petImageView.setImageURI(imageURI);
    }

    public void selectPetImage(View view){

        // List of all the permissions we need to request the user
        ArrayList<String> permList = new ArrayList<>();

        // Star by seeing if we have permission to camera
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.CAMERA);

        int readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readExternalPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        int writeExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalPermission != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int requestCode = 100;
        if (permList.size() > 0 )
        {
            // Convert the ArrayList into an Array of Strings
            String[] perms = new String[permList.size()];

            // Request permissions from the user:
            ActivityCompat.requestPermissions(this, permList.toArray(perms), REQUEST_CODE);
        }

        // If we have all 3 permissions, open image gallery
        if (cameraPermission == PackageManager.PERMISSION_GRANTED &&
                readExternalPermission == PackageManager.PERMISSION_GRANTED &&
                writeExternalPermission == PackageManager.PERMISSION_GRANTED)
        {
            // Use an Intent to launch gallery and take pictures
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE);
        }
        else
            Toast.makeText(this,
                    "Pet Protector requires camera and external storage permission",
                    Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Code to handle when the user closes the image gallery (by selecting an image
        // or pressing the back button)

        // The Intent data is the URI selected from image gallery
        // Decide if the user selected an image:
        if (data != null && requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Set the imageURI to the data:
            imageURI = data.getData();
            petImageView.setImageURI(imageURI);
        }
    }
}
