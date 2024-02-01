package org.kaustav.majorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    public Button clear;

    private TextView userDetailsTextView;
    private TextView userDetails2TextView;
    private FirebaseUser user;
    private EditText inputText;  // Add EditText for user input
    private Button summarizeButton;


    private ImageButton clipboard;

    private EditText summaryText;
    private String flaskURL = "http://192.168.122.218:5000/text-summarization"; // Update with your Flask server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        clear = findViewById(R.id.clearInput);
        userDetailsTextView = findViewById(R.id.user_details);
        userDetails2TextView = findViewById(R.id.user_details2);
        user = auth.getCurrentUser();
        inputText = findViewById(R.id.input);  // Initialize EditText
        summarizeButton = findViewById(R.id.submit);  // Initialize Summarize Button
        summaryText = findViewById(R.id.output);  // Initialize TextView for Summary
        clipboard=findViewById(R.id.clipboard);

        clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cp=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("EditText",summaryText.getText().toString());
                cp.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this,"Copied to Clipboard",Toast.LENGTH_SHORT).show();


            }
        });



        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
            finish();
        } else {
            userDetailsTextView.setText(user.getEmail());
        }






        summarizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTextValue = inputText.getText().toString();
                if (inputTextValue.isEmpty()) {
                    inputText.setError("Input cannot be empty");
                } else {
                    sendSummarizationRequest(inputTextValue);
                }
            }
        });
    }


    public void openSettingsActivity(View v){

        Intent i=new Intent(this,settingsActivity.class);
        startActivity(i);


    }
    public void openHistoryActivity(View v){

        Intent i=new Intent(this,historyActivity.class);
        startActivity(i);


    }






    public void clearInputText(View v){

        if(clear.isPressed()){

            inputText.setText("");


        }



    }


    void sendSummarizationRequest(String inputText) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("inputtext_", inputText)
                .build();

        Request request = new Request.Builder()
                .url(flaskURL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        summaryText.setText(responseData);
                    }
                });
            }
        });
    }
}
