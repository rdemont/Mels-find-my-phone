package ch.rmbi.melsfindmyphone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import ch.rmbi.melsfindmyphone.utils.ErrorUtils;

public class DirectReplyReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    public static final String TEXT_REPLAY_ID = "TEXT_REPLAY_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String replyText = remoteInput.getString(TEXT_REPLAY_ID);
            ErrorUtils.instance(context).error(TAG,replyText);
        }
    }

}
