package com.kobaken0029.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.N2ttsUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.activities.NavigationDrawerActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

/**
 * TextToSpeechを使ったBaseFragment。
 */
public abstract class TextToSpeechFragment extends BaseFragment
        implements TextToSpeech.OnInitListener {
    protected TextToSpeech tts;

    @OnClick(R.id.memomiya)
    void onClickMemomiya() {
        if (tts.isSpeaking()) {
            tts.stop();
        }

        Memo memo = mMemoHelper.find(((NavigationDrawerActivity) getActivity()).currentMemoId);
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
        tts = new TextToSpeech(getActivity(), this);

        View view = findById(getActivity(), R.id.icon_memomiya);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView messageWindowTextView = findById(getActivity(), R.id.message_window);
                messageWindowTextView.setText("");
                String message = getMessage();
                ttsSpeak(message);
                new MyHandler(message, messageWindowTextView).sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tts == null) {
            tts = new TextToSpeech(getActivity(), this);
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

            tts = tts == null ? new TextToSpeech(getActivity(), this) : tts;
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
        if (N2ttsUtil.packageCheck(N2ttsUtil.N2TTS_PACKAGE_NAME, getActivity().getPackageManager())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                String UTTERANCE_ID = "SPEECH";
                HashMap<String, String> ttsParam = new HashMap<>();
                ttsParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, ttsParam);
            }
        } else {
            UiUtil.showToast(getActivity(), getString(R.string.n2tts_not_found_message));
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

    @Override
    void bindView() {
    }

    static class MyHandler extends Handler {
        private String message;
        private TextView messageWindowTextView;
        private String buff = "「";
        private int i = 0;

        MyHandler(String message, TextView messageWindowTextView) {
            this.message = message;
            this.messageWindowTextView = messageWindowTextView;
        }

        @Override
        public void dispatchMessage(Message msg) {
            char data[] = message.toCharArray();

            if (i < data.length) {
                if (msg.what == 1) {
                    buff += String.valueOf(data[i]);
                    messageWindowTextView.setText(buff);
                    this.sendEmptyMessageDelayed(1, 100);
                    i++;
                } else {
                    super.dispatchMessage(msg);
                }
            } else if (i == data.length) {
                buff += "」";
                messageWindowTextView.setText(buff);
            } else {
                i = 0;
                buff = "「";
            }
        }
    }

}
