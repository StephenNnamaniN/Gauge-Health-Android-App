package uk.ac.tees.a0278818.gaugehealth.emotionfile;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import uk.ac.tees.a0278818.gaugehealth.R;

public class FacialExpressionRecognition {
    private static Interpreter interpreter;
    private static int INPUT_SIZE;
    private static CascadeClassifier cascadeClassifier;
    static ByteBuffer byteBuffer;

    public FacialExpressionRecognition(AssetManager assetManager, Context context, String modelPath, int inputSize) throws IOException {
        INPUT_SIZE = inputSize;
       byte[] buffer = new byte[4096];

        initializeInterpreter(assetManager, modelPath);
        // Set GPU for interpreter
//        Interpreter.Options options = new Interpreter.Options();
//        gpuDelegate = new GpuDelegate();
//        // Add Gpu to options
//        options.addDelegate(gpuDelegate);
//        // Now set number of threads to options
//        options.setNumThreads(8); // This should be set according to your phone
//        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
//        // If model is loaded print
//        Log.d("facial_expression", "model is loaded");

        // Now let's load the haarcascade classifier
        try {
            // define input stream to read classifier
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            // Create a folder
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            // Now create a file in that folder
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt");
            // Now define output stream to transfer data to file we created
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            // Now let's create buffer to store byte

            int byteRead;
            // read byte in while loop
            // when it read -1 that means no data to read
            while ((byteRead = is.read(buffer)) != -1){
                // writing on mCascade file
                os.write(buffer, 0, byteRead);
            } // close input and output stream
            is.close();
            os.close();
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            // if cascade file is loaded print
            Log.d("facial_expression", "Classifier is loaded");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initializeInterpreter(AssetManager assetManager, String modelPath) throws IOException {
       Interpreter.Options options = new Interpreter.Options();
        GpuDelegate gpuDelegate = new GpuDelegate();
       options.addDelegate(gpuDelegate);
       options.setNumThreads(4);

       interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
       Log.d("facial expression", "Model is loaded");
    }

    public static void recognizePhoto(Mat mat_image){
       // Before predicting our image is not aligned properly
//        Core.flip(mat_image.t(), mat_image, 1);
        Mat grayscaleImage = new Mat();
        Imgproc.cvtColor(mat_image, grayscaleImage, Imgproc.COLOR_RGB2GRAY);
        // Set height and width
        int height = grayscaleImage.height();
        int width = grayscaleImage.width();

        // define minimum height of face in original image
        // below this size no face in original image will show
        int absoluteFaceSize = (int)(height *0.1);
        // now create MatOfRect to store face
        MatOfRect faces = new MatOfRect();
        // Check if cascadeClassifier is loaded or not
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        // now convert it to an array
        Rect[] faceArray = faces.toArray();
        // loop through each face
        for (Rect rect : faceArray) {
            // if you want to draw rectangle around the face
            //                input/output starting point and ending point          color R  G   B   alpha       thickness
            Imgproc.rectangle(mat_image, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255), 4);
            // Now crop face from original frame and grayscaleImage

            Rect roi = new Rect((int) rect.tl().x, (int) rect.tl().y,
                    ((int) rect.br().x) - (int) (rect.tl().x),
                    ((int) rect.br().y) - (int) (rect.tl().y));
            Mat cropped_rgba = new Mat(mat_image, roi);
            // now convert cropped_rgba to bitmap
            Bitmap bitmap = null;
            bitmap = Bitmap.createBitmap(cropped_rgba.cols(), cropped_rgba.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped_rgba, bitmap);
            // resize bitmap to (48, 48)
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 48, 48, false);
            // now convert scaledBitmap to byteBuffer
            byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
            // now create an object to hold output
            float[][] emotion = new float[1][1];
            // now predict with bytebuffer as an input emotion as an output
            interpreter.run(byteBuffer, emotion);
            // if emotion is recognized print value of it

            // define float value of emotion
            float emotion_v = (float) Array.get(Array.get(emotion, 0), 0);
            Log.d("facial_expression", "Output: " + emotion_v);
            // create a function that return text emotion
            String emotion_s = get_emotion_text(emotion_v);
            // Put on prediction string in frame and remove value
            Imgproc.putText(mat_image, emotion_s,
                    new Point((int) rect.tl().x + 5, (int) rect.tl().y + 10),
                    2, 2, new Scalar(255, 0, 0, 255), 1);

        }


    }

    private static String get_emotion_text(float emotionV) {
        // create an empty string
        String val = "";
        // use if statement to determine val
        if (emotionV >= 0 & emotionV < 0.5){
            val = "Surprise";
        } else if (emotionV >= 0.5 & emotionV < 1.5) {
            val = "Fear";
        } else if (emotionV >= 1.5 & emotionV < 2.5) {
            val = "Angry";
        }else if (emotionV >= 2.5 & emotionV < 3.5) {
            val = "Neutral";
        } else if (emotionV >= 3.5 & emotionV < 4.5) {
            val = "Sad";
        } else if (emotionV >= 4.5 & emotionV < 5.5) {
            val = "Disgust";
        } else {
            val = "Happy";
        }
        return val;
    }

    private static ByteBuffer convertBitmapToByteBuffer(Bitmap scaledBitmap) {
        ByteBuffer byteBuffer;
        int size_image = INPUT_SIZE; //48

        byteBuffer = ByteBuffer.allocateDirect(4 * size_image * size_image * 3);
//        byteBuffer=ByteBuffer.allocateDirect(4*1*size_image*size_image*3);
        // 4 is multiplied for float input
        //3 is multiplied for rgb
        byteBuffer.order(ByteOrder.nativeOrder());
         int[] intValues = new int[size_image*size_image];
         scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
         int pixel = 0;
         for (int i = 0; i < size_image; ++i){
             for (int j = 0; j < size_image; ++j){
                 final int val = intValues[pixel++];
                 // now put float value to bytebuffer
                 // scale image to convert image from 0-255 to 0-1
                 byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f/255.f));
                 byteBuffer.putFloat(((val >> 8) & 0xFF)*(1.f/255.f));
                 byteBuffer.putFloat(((val & 0xFF))/255.0f);
             }
         }
        System.out.println("Position before rewind: " + byteBuffer.position());

// Make sure to reset the position to the beginning of the buffer
        byteBuffer.rewind();

        System.out.println("Position after rewind: " + byteBuffer.position());

        return byteBuffer;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();

        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
