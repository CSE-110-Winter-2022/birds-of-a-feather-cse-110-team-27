package com.example.birdsofafeather.parsers;

import android.app.Service;
import android.content.Context;

public interface Parser {

    void parse(Context context, String message, Service service);
}
