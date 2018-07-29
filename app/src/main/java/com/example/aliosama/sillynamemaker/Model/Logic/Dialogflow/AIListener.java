package com.example.aliosama.sillynamemaker.Model.Logic.Dialogflow;

import ai.api.model.AIError;
import ai.api.model.AIResponse;

/**
 * Created by aliosama on 2/7/2018.
 */

public interface AIListener {
    void onResult(AIResponse result); // here process response
    void onError(AIError error); // here process error
    void onAudioLevel(float level); // callback for sound level visualization
    void onListeningStarted(); // indicate start listening here
    void onListeningCanceled(); // indicate stop listening here
    void onListeningFinished(); // indicate stop listening here
}
