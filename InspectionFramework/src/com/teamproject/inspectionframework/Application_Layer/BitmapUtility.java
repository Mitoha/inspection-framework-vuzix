package com.teamproject.inspectionframework.Application_Layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BitmapUtility {

    // convert from bitmap to byte array
    // this is needed before storing it to the database
	public byte[] getBytes(Bitmap bitmap) {

        int bytes = bitmap.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();
        return array;
    }
	
	//TODO: method for converting audiofile to be able to save it in the DB
}
