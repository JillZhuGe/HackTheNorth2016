package com.example.nivasini.htn16;

import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import java.util.Locale;
import android.widget.Toast;
import java.util.ArrayList;

import static android.speech.SpeechRecognizer.isRecognitionAvailable;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech t2s;
    private EditText word;
    private Button pronounce;
    private SpeechRecognizer s2t = null;
    private TextView returnedText;
    private Button startRec;
    private Button stopRec;
    private Intent recognizerIntent;
    //private String LOG_TAG = "VoiceRecognitionActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        word = (EditText) findViewById(R.id.word_et);
        pronounce = (Button) findViewById(R.id.pronounce_b);
        startRec = (Button) findViewById(R.id.startrec_b);

        t2s = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2s.setLanguage(Locale.CANADA);
                }
            }
        });

        pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = word.getText().toString();
                t2s.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        returnedText = (TextView) findViewById(R.id.retword_et);

        startRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void test() {
        if (SpeechRecognizer.isRecognitionAvailable(getApplicationContext()) == true) {
            returnedText.setText("true");
        }
        else {
            returnedText.setText("false");
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    returnedText.setText("check");
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    returnedText.setText(result.get(0));
                }
                break;
            }

        }
    }
}
