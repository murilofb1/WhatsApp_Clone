package com.murilofb.wppclone.home.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.murilofb.wppclone.R;

import java.util.Observable;
import java.util.Observer;

public class MessagesTab extends Fragment implements Observer {

    public MessagesTab() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        return view;
    }


    @Override
    public void update(Observable o, Object arg) {

    }


}

