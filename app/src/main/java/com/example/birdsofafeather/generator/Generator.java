package com.example.birdsofafeather.generator;

import android.content.Context;

public interface Generator {
    String generateCSV(Context context, long myID, long targetID);
}
