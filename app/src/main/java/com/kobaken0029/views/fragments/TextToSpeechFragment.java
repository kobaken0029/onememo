package com.kobaken0029.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.utils.N2ttsUtil;
import com.kobaken0029.utils.UiUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static butterknife.ButterKnife.findById;

/**
 * TextToSpeechを使ったBaseFragment。
 */
public abstract class TextToSpeechFragment extends BaseFragment {

    /** プリファレンスID。*/
    private static final String SHARED_PREFERENCES_VOICE_SWITCH_ID = "voice_switch";

    /** プリファレンスKEY。*/
    private static final String SHARED_PREFERENCES_VOICE_SWITCH_KEY = "voice_switch_key";

    /** ハンドラのメッセージID */
    private static final int HANDLER_MESSAGE_ID = 1;

    private Handler mHandler;
    protected TextToSpeech mTextToSpeech;

    /**
     * TextToSpeechの初期化リスナー。
     */
    private TextToSpeech.OnInitListener mInitListener = status -> {
        if (status == TextToSpeech.SUCCESS && mTextToSpeech != null) {
            mTextToSpeech.setPitch(1.0f);
            mTextToSpeech.setSpeechRate(1.0f);
            mTextToSpeech.setLanguage(Locale.JAPAN);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextToSpeech = new TextToSpeech(getActivity(), mInitListener);

        View view = findById(getActivity(), R.id.icon_memomiya);
        if (view != null) {
            view.setOnClickListener(v -> {
                TextView messageWindowTextView = findById(getActivity(), R.id.message_window);
                messageWindowTextView.setText("");
                String message = getMessage();
                ttsSpeak(message);
                if (mHandler != null) {
                    mHandler.removeMessages(HANDLER_MESSAGE_ID);
                }
                mHandler = new MyHandler(message, messageWindowTextView);
                mHandler.sendEmptyMessage(HANDLER_MESSAGE_ID);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextToSpeech == null) {
            mTextToSpeech = new TextToSpeech(getActivity(), mInitListener);
        }
    }

    @Override
    public void onPause() {
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
        super.onPause();
    }

    /**
     * N2 TTSで音声読み上げを行います。
     *
     * @param text 読み上げるテキスト
     */
    protected void ttsSpeak(String text) {
        if (N2ttsUtil.packageCheck(N2ttsUtil.N2TTS_PACKAGE_NAME, getActivity().getPackageManager())) {
            if (canPlayVoice(getSharedPreferences())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    String UTTERANCE_ID = "SPEECH";
                    HashMap<String, String> ttsParam = new HashMap<>();
                    ttsParam.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
                    mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, ttsParam);
                }
            }
        } else {
            UiUtil.showToast(getActivity(), getString(R.string.n2tts_not_found_message));
        }
    }

    /**
     * 愛萌宮さんのランダムセリフを取得する。
     */
    private String getMessage() {
        String message;

        // 乱数に応じて、テキストをセット
        switch (new Random().nextInt(7)) {
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
            default:
                message = getResources().getString(R.string.voice7);
                break;
        }

        return message;
    }

    /**
     * プリファレンスを取得する。
     *
     * @return SharedPreferences
     */
    private SharedPreferences getSharedPreferences() {
        return getActivity().getSharedPreferences(SHARED_PREFERENCES_VOICE_SWITCH_ID, Context.MODE_PRIVATE);
    }

    /**
     * 音声を再生するかどうかをプリファレンスから取得する。
     *
     * @param preferences SharedPreferences
     * @return 音声を再生出来る場合true
     */
    private boolean canPlayVoice(SharedPreferences preferences) {
        return preferences.getBoolean(SHARED_PREFERENCES_VOICE_SWITCH_KEY, true);
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
