package com.example.bbdaiya.capstoneproject.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by bbdaiya on 09-Mar-17.
 */

public class NewsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsViewsFactory(this);
    }
}
