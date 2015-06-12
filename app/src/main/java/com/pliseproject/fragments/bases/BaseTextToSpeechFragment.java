package com.pliseproject.fragments.bases;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.pliseproject.R;
import com.pliseproject.activities.bases.BaseNavigationDrawerActivity;
import com.pliseproject.utils.UiUtil;
import com.pliseproject.utils.packageUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import butterknife.OnClick;

/**
 * TextToSpeechを使ったBaseFragment。
 */
public class BaseTextToSpeechFragment extends BaseNavigationDrawerFragment
        implements TextToSpeech.OnInitListener {
    protected TextToSpeech tts;


    @OnClick(R.id.memomiya)
    void onClickMemomiya() {
        if (tts.isSpeaking()) {
            tts.stop();
        }

        ttsSpeak(memo != null
                ? ((memo.getSubject() != null
                ? memo.getSubject() + "。"
                : "") +
                (memo.getMemo() != null
                        ? memo.getMemo()
                        : ""))
                : "");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getDrawerMemomiyaImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getMessageWindowTextView().setText("");
                String message = getMessage();
                ttsSpeak(message);
                new MyHandler(message, activity).sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tts == null) {
            tts = new TextToSpeech(activity, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            float pitch = 1.0f;
            float rate = 1.0f;
            Locale locale = Locale.JAPAN;

            tts.setPitch(pitch);
            tts.setSpeechRate(rate);
            tts.setLanguage(locale);
        }
    }

    /**
     * N2 TTSで音声読み上げを行います。
     *
     * @param text 読み上げるテキスト
     */
    private void ttsSpeak(String text) {
        if (packageUtil.packageCheck(packageUtil.N2TTS_PACKAGE_NAME, activity.getPackageManager())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                String UTTERANCE_ID = "SPEECH";
                HashMap<String, String> ttsParam = new HashMap<>();
                ttsParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, ttsParam);
            }
        } else {
            UiUtil.showToast(activity, packageUtil.N2TTS_PACKAGE_NOT_FOUND_MESSAGE);
        }
    }

    /**
     * 愛萌宮さんのランダムセリフを取得する。
     */
    private String getMessage() {
        String message = "";

        // 乱数を生成
        int ran = new Random().nextInt(6);

        // 乱数に応じて、テキストをセット
        switch (ran) {
            case 0:
                message = getResources().getString(R.string.voice1);
                break;
            case 1:
                message = getResources().getString(R.string.voice2);
                break;
            case 2:
                message = getResources().getString(R.string.voice3);
                break;
            case 3:
                message = getResources().getString(R.string.voice4);
                break;
            case 4:
                message = getResources().getString(R.string.voice5);
                break;
            case 5:
                message = getResources().getString(R.string.voice6);
                break;
        }

        return message;
    }

    static class MyHandler extends Handler {
        private String message;
        private BaseNavigationDrawerActivity activity;
        private String buff = "「";
        private int i = 0;

        MyHandler(String message, BaseNavigationDrawerActivity activity) {
            this.message = message;
            this.activity = activity;
        }

        @Override
        public void dispatchMessage(Message msg) {
            char data[] = message.toCharArray();

            if (i < data.length) {
                if (msg.what == 1) {
                    buff += String.valueOf(data[i]);
                    activity.getMessageWindowTextView().setText(buff);
                    this.sendEmptyMessageDelayed(1, 100);
                    i++;
                } else {
                    super.dispatchMessage(msg);
                }
            } else if (i == data.length) {
                buff += "」";
                activity.getMessageWindowTextView().setText(buff);
            } else {
                i = 0;
                buff = "「";
            }
        }
    }

}
