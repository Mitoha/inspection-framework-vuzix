package com.teamproject.inspectionframework.Application_Layer;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Provides functions for handling pictures
 *
 */
public class BitmapUtility {

	/**
	 * Converts a bitmap into a byte array
	 * 
	 * @param bitmap
	 *            The bitmap that should be converted
	 * @return The byte array
	 */
	public byte[] getBytes(Bitmap bitmap) {

		int bytes = bitmap.getByteCount();

		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		bitmap.copyPixelsToBuffer(buffer);
		byte[] array = buffer.array();
		return array;
	}

	/**
	 * Converts a byte array into an image
	 * 
	 * @param image
	 *            The image (as byte array) that should be converted
	 * @return The corresponding bitmap
	 */
	public Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}
}
