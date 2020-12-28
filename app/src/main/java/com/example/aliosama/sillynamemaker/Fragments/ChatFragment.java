package com.example.aliosama.sillynamemaker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.aliosama.sillynamemaker.Activities.Chatbot.ChatActivity;
import com.example.aliosama.sillynamemaker.Activities.Chatbot.FirebaseChatbotActivity;
import com.example.aliosama.sillynamemaker.R;
import com.example.aliosama.sillynamemaker.databinding.FragmentChatBinding;
import com.google.android.material.button.MaterialButton;

public class ChatFragment extends Fragment {

    public ChatFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentChatBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);

        binding.AnonymousChatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FirebaseChatbotActivity.class);
            getActivity().startActivity(intent);
        });
        binding.JoeyChatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FirebaseChatbotActivity.class);
            getActivity().startActivity(intent);
        });

        return binding.getRoot();
    }

}
