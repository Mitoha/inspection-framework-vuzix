package com.teamproject.inspectionframework.Application_Layer;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtility {

	public byte[] getBytes(Bitmap bitmap) {

		int bytes = bitmap.getByteCount();

		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		bitmap.copyPixelsToBuffer(buffer);
		byte[] array = buffer.array();
		return array;
	}

	// convert from byte array to bitmap
	// this is needed when data from database is retrieved
	public Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
}
