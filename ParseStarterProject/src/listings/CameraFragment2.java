package listings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import trial.MainActivity;
import trial.ProfileFrag;

public class CameraFragment2 extends android.support.v4.app.Fragment {

    public static final String TAG = "CameraFragment";

    private Camera camera;
    private SurfaceView surfaceView;
    private ParseFile photoFile;
    private ImageButton photoButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);

        ((MainActivity) getActivity()).goBackToPrevious(true);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity) getActivity()).setActionBarTitle("New Profile Picture");


        photoButton = (ImageButton) v.findViewById(R.id.camera_photo_button);

        if (camera == null) {
            try {
                camera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.e(TAG, "No camera with exception: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(getActivity(), "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (camera == null)
                    return;
                camera.takePicture(new Camera.ShutterCallback() {

                    @Override
                    public void onShutter() {
                        // nothing to do
                    }

                }, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        saveScaledPhoto(data);
                    }

                });

            }
        });

        surfaceView = (SurfaceView) v.findViewById(R.id.camera_surface_view);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new Callback() {

            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (camera != null) {
                        camera.setDisplayOrientation(90);
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error setting up preview", e);
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                // nothing to do here
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                // nothing here
            }

        });

        return v;
    }

    /*
     * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
     * they are saved. Since we never need a full-size image in our app, we'll
     * save a scaled one right away.
     */
    private void saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap listingImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap listingImageScaled = Bitmap.createScaledBitmap(listingImage, 300, 300, false);
//        Bitmap listingImageScaled = Bitmap.createScaledBitmap(listingImage, 300, 550
//                * listingImage.getHeight() / listingImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedScaledListingImage = Bitmap.createBitmap(listingImageScaled, 0,
                0, listingImageScaled.getWidth(), listingImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledListingImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("prof.jpg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    addPhotoToListingAndReturn(photoFile);
                }
            }
        });
    }

    private void addPhotoToListingAndReturn(ParseFile photoFile) {
        ParseUser.getCurrentUser().put("prof",photoFile);
        ParseUser.getCurrentUser().saveInBackground();
        android.support.v4.app.Fragment ProfileFrag = new ProfileFrag();
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,ProfileFrag);
        transaction.commit();
//		FragmentManager fm = getActivity().getFragmentManager();
//		fm.popBackStack("NewMealFragment",
//				FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (camera == null) {
            try {
                camera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.i(TAG, "No camera: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(getActivity(), "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPause() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        super.onPause();
    }

}
