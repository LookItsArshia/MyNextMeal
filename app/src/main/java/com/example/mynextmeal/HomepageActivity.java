package com.example.mynextmeal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import clarifai2.exception.ClarifaiException;
import okhttp3.OkHttpClient;

import org.tensorflow.lite.Interpreter;

public class HomepageActivity extends AppCompatActivity {

    private Button scanBtn;
    private String ACTIVITY_NAME = "HOMEPAGE";
    private static final int CAMERA_REQUEST = 1888;
    public ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final String APIKEY = "d944de6a0e75490f939c289aa1ff1c00";
    private final String MODEL_ID = "bd367be194cf45149e75f01d59f77ba7";
    private Bitmap bitmap;
    final ClarifaiClient client = new ClarifaiBuilder(APIKEY)
            .client(new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()
            )
            .buildSync();
    public Model<Concept> generalModel;
    public PredictRequest<Concept> request;
    public List<ClarifaiOutput<Concept>> result;
    public Bitmap incomingImage;
    public List<String> veggies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        veggies = new ArrayList<String>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        scanBtn = findViewById(R.id.homepage_scan_button);
        imageView = findViewById(R.id.imageView);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Location:","in Scan Ingredients");

                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        try {
            URL url = new URL("https://www.mealgarden.com/media/recipe/2016/05/veggies.jpeg"); //Will be the bytearray of the image just taken
            incomingImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(incomingImage);
        }catch (Exception e){
            e.printStackTrace();
        }
        new getModel().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
//            Bitmap incomingImage = (Bitmap) data.getExtras().get("data");
            bitmap = incomingImage;
//            imageView.setImageBitmap(incomingImage);
            try{
                new getCall().execute(request);

            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(this, "Added image and result", Toast.LENGTH_SHORT).show();
            Log.i("Homepage","Added image and result");

        }
    }

    public void openMyMenu(View view) {
        Log.i("Location:","in openMyMenu");
        Intent menu = new Intent(this.getApplicationContext(), Ingredients.class);
        menu.putStringArrayListExtra("veggie", (ArrayList<String>)veggies);
        startActivity(menu);
    }

    public void spoonacular(View view) throws IOException {
        List<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        SpoonacularAPI api = new SpoonacularAPI();
        AsyncTask<List<String>, Integer, String> res = api.execute(strings);
        Log.i("homepage", String.valueOf(res));
        Log.i("Homepage", "called spoon");
    }

    class getModel extends AsyncTask<ClarifaiResponse<Model<?>>, String, Model<Concept>>{

        @Override
        protected Model<Concept> doInBackground(ClarifaiResponse<Model<?>>... responses) {
            generalModel = client.getModelByID(MODEL_ID).executeSync().get().asConceptModel();
            Log.i(ACTIVITY_NAME, "Loaded GeneralModel");
            return generalModel;
        }

        @Override
        protected void onPostExecute(Model<Concept> conceptModel) {
            super.onPostExecute(conceptModel);
//            generalModel = conceptModel;
        }
    }

    class getCall extends AsyncTask<PredictRequest<Concept>, String, List<ClarifaiOutput<Concept>>>{

        @Override
        protected List<ClarifaiOutput<Concept>> doInBackground(PredictRequest<Concept>... data) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            incomingImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            request = generalModel.predict().withInputs(//ClarifaiInput.forImage());
                    ClarifaiInput.forImage(baos.toByteArray()));
//            result = data[0].executeSync().get();
            result = request.executeSync().get();
            return result;
        }

        @Override
        protected void onPostExecute(List<ClarifaiOutput<Concept>> s) {
            super.onPostExecute(s);
            for(int i = 0; i <= 5; i++){
                veggies.add(result.get(0).data().get(i).name());
                Log.i(ACTIVITY_NAME, "Added: " + result.get(0).data().get(0).name());
            }
        }
    }
}
