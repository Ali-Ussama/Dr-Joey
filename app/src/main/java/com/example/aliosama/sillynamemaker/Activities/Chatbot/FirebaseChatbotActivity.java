package com.example.aliosama.sillynamemaker.Activities.Chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.aliosama.sillynamemaker.Model.POJO.ChatMessage;
import com.example.aliosama.sillynamemaker.Model.POJO.chat_rec;
import com.example.aliosama.sillynamemaker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

//Text to speech
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import android.widget.Toast;

import java.util.Locale;

public class FirebaseChatbotActivity extends AppCompatActivity implements AIListener,OnInitListener{

    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage,chat_rec> adapter;
    Boolean flagFab = true;
    private AIService aiService;

//    Text-to-speach
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firabase_chatbot);

//        Text-to-speech
        try {
            Intent checkTTSIntent = new Intent();
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

            Init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init(){
        try {
            recyclerView = findViewById(R.id.recyclerView);
            editText = findViewById(R.id.editText);
            addBtn = findViewById(R.id.addBtn);

            recyclerView.setHasFixedSize(true);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            ref = FirebaseDatabase.getInstance().getReference();
            ref.keepSynced(true);

            final AIConfiguration config = new AIConfiguration(getResources().getString(R.string.DIALOGFLOW_CLIENT_ACCESS_TOKEN),
                    AIConfiguration.SupportedLanguages.English,
                    AIConfiguration.RecognitionEngine.System);

            aiService = AIService.getService(this, config);
            aiService.setListener(this);

            final AIDataService aiDataService = new AIDataService(config);

            final AIRequest aiRequest = new AIRequest();


            addBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View view) {

                    String message = editText.getText().toString().trim();

                    if (!message.equals("")) {

                        ChatMessage chatMessage = new ChatMessage(message, "user");
                        ref.child("chat").push().setValue(chatMessage);

                        aiRequest.setQuery(message);
                        new AsyncTask<AIRequest,Void,AIResponse>(){

                            @Override
                            protected AIResponse doInBackground(AIRequest... aiRequests) {
                                final AIRequest request = aiRequests[0];
                                try {
                                    final AIResponse response = aiDataService.request(aiRequest);
                                    return response;
                                } catch (AIServiceException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(AIResponse response) {
                                if (response != null) {

                                    Result result = response.getResult();
                                    String reply = result.getFulfillment().getSpeech();
                                    ChatMessage chatMessage = new ChatMessage(reply, "bot");
                                    ref.child("chat").push().setValue(chatMessage);
                                }
                            }
                        }.execute(aiRequest);
                    }
                    else {
                        aiService.startListening();
                    }

                    editText.setText("");

                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ImageView fab_img = findViewById(R.id.fab_img);
                    Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                    Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                    if (s.toString().trim().length()!=0 && flagFab){
                        ImageViewAnimatedChange(FirebaseChatbotActivity.this,fab_img,img);
                        flagFab=false;

                    }
                    else if (s.toString().trim().length()==0){
                        ImageViewAnimatedChange(FirebaseChatbotActivity.this,fab_img,img1);
                        flagFab=true;

                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(ChatMessage.class,R.layout.msglist,chat_rec.class,ref.child("chat")) {
                @Override
                protected void populateViewHolder(chat_rec viewHolder, ChatMessage model, int position) {
                    try {
                        if (model.getMsgUser().equals("user")) {
                            viewHolder.rightText.setText(model.getMsgText());

                            viewHolder.rightText.setVisibility(View.VISIBLE);
                            viewHolder.leftText.setVisibility(View.GONE);
                        }
                        else {
                            viewHolder.leftText.setText(model.getMsgText());

                            viewHolder.rightText.setVisibility(View.GONE);
                            viewHolder.leftText.setVisibility(View.VISIBLE);
                            speak(model.getMsgText());

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    int msgCount = adapter.getItemCount();
                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1 ||
                            (positionStart >= (msgCount - 1) &&
                                    lastVisiblePosition == (positionStart - 1))) {
                        recyclerView.scrollToPosition(positionStart);
                    }
                }
            });

            recyclerView.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {


        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child("chat").push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child("chat").push().setValue(chatMessage);


    }

    @Override
    public void onError(AIError error) {
        Log.i("onError",error.getMessage());
        Log.i("onError",error.toString());

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onInit(int initStatus) {
        try {
            //check for successful instantiation
            if (initStatus == TextToSpeech.SUCCESS) {
                if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                    myTTS.setLanguage(Locale.US);
            }
            else if (initStatus == TextToSpeech.ERROR) {
                Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == MY_DATA_CHECK_CODE) {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    myTTS = new TextToSpeech(this, this);
                }
                else {
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void speak(String message){
        try {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }
}
