package com.example.student.cooper_assign2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Chris Frye on 10/31/2017.
 */

public class ImageUtils {

    static final int IMAGE_HEIGHT = 300;
    static final int IMAGE_WIDTH = 275;

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //convert uri to bitmap
    public static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }

    //function to resize the image to the appropriate size for spinners
    //To change the height or width that the image is resized to,
    //change constants IMAGE_WIDTH, IMAGE_HEIGHT above
    //Takes bitmap as parameter
    //Returns: bitmap
    //*DOES NOT CHANGE THE ORIGINAL BITMAP
    public static Bitmap resizeImage(Bitmap image)
    {
        //get image width and height
        int width = image.getWidth();
        int height = image.getHeight();
        //get scaled width and height
        float scaleWidth = IMAGE_WIDTH / (float)width;
        float scaleHeight = IMAGE_HEIGHT / (float)height;
        // create a matrix for the resize
        Matrix matrix = new Matrix();
        // do the resizing
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the scaled bitmap and return it
        Bitmap resizedBitmap = Bitmap.createBitmap(
                image, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //overloaded resizeImage function
    //allows a specified width and height to be resized to
    public static Bitmap resizeImage(Bitmap image, int scaleWidth, int scaleHeight)
    {
        //get image width and height
        int width = image.getWidth();
        int height = image.getHeight();
        //get scaled width and height
        float scaledWidth = scaleWidth / (float)width;
        float scaledHeight = scaleHeight / (float)height;
        // create a matrix for the resize
        Matrix matrix = new Matrix();
        // do the resizing
        matrix.postScale(scaledWidth, scaledHeight);
        // recreate the scaled bitmap and return it
        Bitmap resizedBitmap = Bitmap.createBitmap(
                image, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
