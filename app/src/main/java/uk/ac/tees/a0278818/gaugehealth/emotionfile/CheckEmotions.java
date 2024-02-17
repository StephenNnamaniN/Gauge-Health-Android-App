package uk.ac.tees.a0278818.gaugehealth.emotionfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

import uk.ac.tees.a0278818.gaugehealth.R;

public class CheckEmotions extends AppCompatActivity {
    private Button selectImage, captureImage, predict;
    private ImageView imageV;

    int SELECT_PICTURE = 200;
    int TAKE_PICTURE = 100;

    Bitmap bitmap = null;
    private FacialExpressionRecognition facialExpressionRecognition;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_emotions);

        selectImage = findViewById(R.id.select_image_btn);
        captureImage = findViewById(R.id.capture_image_btn);
        predict = findViewById(R.id.predict_btn);
        imageV = findViewById(R.id.image_view);

        // TODO: load model and classifier
        try {
            int inputSize = 48;
            facialExpressionRecognition = new FacialExpressionRecognition(getAssets(), CheckEmotions.this,
                    "model300.tflite", inputSize);
        } catch (IOException e){
            e.printStackTrace();
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_chooser();
            }
        });

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(intent, "Take Picture" ), TAKE_PICTURE);
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mat selected_image = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(bitmap, selected_image);
//                Core.rotate(selected_image, selected_image, 0);

                FacialExpressionRecognition.recognizePhoto(selected_image);
                Bitmap bitmap1 = null;
                bitmap1 = Bitmap.createBitmap(selected_image.cols(), selected_image.rows(), Bitmap.Config.ARGB_8888);

                Core.rotate(selected_image, selected_image, -1);
                Utils.matToBitmap(selected_image, bitmap1);
                imageV.setImageBitmap(bitmap1);
            }
        });
    }

    private void image_chooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    Log.d("StoragePActivity", "Output Uri: " + selectedImageUri);
                    //convert uri to bitmap
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageV.setImageBitmap(bitmap);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == TAKE_PICTURE){
                bitmap = (Bitmap) data.getExtras().get("data");
                imageV.setImageBitmap(bitmap);
            }
        }
    }
}