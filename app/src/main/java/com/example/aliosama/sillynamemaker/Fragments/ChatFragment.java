package com.example.aliosama.sillynamemaker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aliosama.sillynamemaker.Activities.Chatbot.ChatActivity;
import com.example.aliosama.sillynamemaker.Activities.Chatbot.FirebaseChatbotActivity;
import com.example.aliosama.sillynamemaker.R;

public class ChatFragment extends Fragment implements View.OnClickListener{


    private Button ChatWithJoey;
    private Button ChatWithAnonymous;

    public ChatFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ChatWithJoey = view.findViewById(R.id.Joey_Chat_Button);
        ChatWithAnonymous = view.findViewById(R.id.Anonymous_Chat_Button);

        ChatWithAnonymous.setOnClickListener(this);
        ChatWithJoey.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.Joey_Chat_Button){
            Intent intent = new Intent(view.getContext(), FirebaseChatbotActivity.class);
            view.getContext().startActivity(intent);
        }else if (id == R.id.Anonymous_Chat_Button){
            Intent intent = new Intent(view.getContext(), FirebaseChatbotActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
