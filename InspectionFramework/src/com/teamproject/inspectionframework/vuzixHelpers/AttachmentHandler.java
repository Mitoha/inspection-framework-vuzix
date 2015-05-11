package com.teamproject.inspectionframework.vuzixHelpers;

import com.teamproject.inspectionframework.MyApplication;
import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.R.id;
import com.teamproject.inspectionframework.R.layout;
import com.teamproject.inspectionframework.Application_Layer.BitmapUtility;
import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AttachmentHandler {

    ImageView IMG;
    int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private MySQLiteHelper datasource;
    Bitmap imageBitmap;
    BitmapUtility bitmapUtility;
    HttpCustomClient client;
    Task task;
    int Clicked = 0;
    int NoPic=0;
    String mCurrentPhotoPath;
    private MyApplication myApp;


    public AttachmentHandler (Context ctx) {
    	myApp = (MyApplication) ctx;
        datasource = new MySQLiteHelper(ctx);
        bitmapUtility = new BitmapUtility();
        client = new HttpCustomClient();

        Assignment assignment = myApp.getAssignment();
        task = myApp.getTask();
    }

    public int takePicture(String type) {
    	int result = 0;
    	
    	return result;
    }

//        try{
//        	//TODO: Implement for foto and audio 
//            byte[] B = datasource.getAttachmentPhotoByTaskId(task.getId());
//            Bitmap bitmap = BitmapFactory.decodeByteArray(B, 0, B.length);
//            IMG.setImageBitmap(bitmap);
//
//        } catch (Exception e) {
//        e.printStackTrace();
//            NoPic=1;
//
//    }
//
//
//        if(assignment.getState()!=2) {
//            IMG.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    dispatchTakePictureIntent();
//
//                }
//            });
//        }
//
//
//        Save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Updating the task in the database
//
//
//
//                //Creating a new assignment and store it to the database
//                if (Clicked == 1&&Problem_Desc.getText().length()!=0) {
//
//                    //The imagebitmap is transferred to byte[] before storing it to the database
//
//                    IMG.buildDrawingCache();
//                    Bitmap bmap = IMG.getDrawingCache();
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    bmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
//                    byte[] array = bos.toByteArray();
//                    System.out.println(array);
//                    Attachment attachment = new Attachment();
//                    attachment.setAssignmentId(assignmentId);
//                    attachment.setFile_type("Photo");
//                    attachment.setBinaryObject(array);
//                    attachment.setTaskId(taskId);
//                    if(NoPic==1) {
//                        datasource.createAttachment(attachment);
//                        Toast.makeText(getApplicationContext(), "Attachment created ",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        datasource.deleteAttachment(taskId);
//                        datasource.createAttachment(attachment);
//
//                        Toast.makeText(getApplicationContext(), "Attachment updated ",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    task.setErrorDescription(Problem_Desc.getText().toString());
//                    task.setState(2);
//                    datasource.updateTask(task);
//                }
//                else if(Clicked == 1&&Problem_Desc.getText().length()==0){
//                    Toast.makeText(getApplicationContext(), "Problem description is empty, please fill it before saving",
//                            Toast.LENGTH_LONG).show();
//                }
//                else if(Clicked == 0&&Problem_Desc.getText().length()!=0){
//                    task.setErrorDescription(Problem_Desc.getText().toString());
//                    task.setState(2);
//                    datasource.updateTask(task);
//                }
//                else if(Problem_Desc.getText().length()==0){
//                    Toast.makeText(getApplicationContext(), "Problem description is empty, please fill it before saving",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Toast.makeText(getApplicationContext(), "File not created",
//                        Toast.LENGTH_LONG).show();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//
//            setPic();
//
//            Clicked=1;
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//
//
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = IMG.getWidth();
//        int targetH = IMG.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        IMG.setImageBitmap(bitmap);
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

}
