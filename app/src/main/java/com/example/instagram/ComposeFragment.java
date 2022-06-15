package com.example.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.databinding.FragmentComposeBinding;
import com.example.instagram.models.Post;
import com.example.instagram.utils.BitmapScaler;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {

    private EditText descriptionTF;
    private Button submitButton;
    private ImageView postImage;
    private Button captureImageButton;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    private static final String TAG = "MainActivity";
    private ParseUser currentUser;
    private ActivityResultLauncher<Uri> pictureLauncher;
    private FragmentComposeBinding binding;

    public ComposeFragment() {
        // Required empty public constructor
    }


    public static ComposeFragment newInstance(String param1, String param2) {
        return new ComposeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComposeBinding.inflate(LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionTF = binding.descriptionTextField;
        submitButton = binding.submitButton;
        postImage = binding.postImageView;
        captureImageButton = binding.captureImageButton;
        currentUser = ParseUser.getCurrentUser();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (descriptionTF.getText().length() == 0){
                    // check that description is filled in
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    savePost(descriptionTF.getText().toString());
                    descriptionTF.setText("");
                    postImage.setImageResource(0);
                }
            }
        });

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        pictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                System.out.println("result: "+result);
                // if a picture was taken
                if (result){
                    Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // resize bitmap
                    Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 400);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    // Compress the image further
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                    // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                    File resizedFile = getPhotoFileUri(photoFileName + "_resized");
                    try {
                        resizedFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(resizedFile);
                        // Write the bytes of the bitmap to file
                        fos.write(bytes.toByteArray());
                        fos.close();


                        // set the orientation data of the resized image so we can rotate it properly
                        ExifInterface exifResizedImage = new ExifInterface(resizedFile.getPath());
                        ExifInterface exifOriginal = new ExifInterface(photoFile.getPath());
                        exifResizedImage.setAttribute(ExifInterface.TAG_ORIENTATION, exifOriginal.getAttribute(ExifInterface.TAG_ORIENTATION));
                        exifResizedImage.saveAttributes();

                        Bitmap rotatedBM = rotateBitmapOrientation(resizedFile.getPath());
                        postImage.setImageBitmap(rotatedBM);
                    } catch (IOException e) {
                        Log.e(TAG, "error writing resized image to filesystem : "+e.getMessage(), e);
                    }

                } else {
                    // Result was a failure
                    Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePost(String description){
        Post post = new Post();

        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "error saving post", e);
                    Toast.makeText(getContext(), "Error saving post: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void launchCamera(){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.faruq.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            pictureLauncher.launch(fileProvider);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        System.out.println("orientation - faruq: "+orientation);
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }
}