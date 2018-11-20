package com.zqzy.rangebar_test;


import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lygit.rangebar.IRangeBarFormatter;
import com.lygit.rangebar.RangeBar;



public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private TextView mEtLeftIndexValue;
    private TextView mEtRightIndexValue;
    private Button mBtConfirm;
    private Button mBtPlay;
    private RangeBar mRangeBar;
    private MediaPlayer mediaPlayer;
    private int calcTimes;//用于rangbar划分刻度用的计算倍数，如果没有这个划分刻度过多造成界面卡顿甚至rangbar不可用
    private RoundCornerDialog roundCornerDialog;
    private LinearLayout mLLresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtLeftIndexValue = (TextView) findViewById(R.id.leftIndexValue);
        mEtRightIndexValue = (TextView) findViewById(R.id.rightIndexValue);
        mBtPlay = (Button) findViewById(R.id.bt_play);
        mBtConfirm = (Button) findViewById(R.id.bt_confirm);
        mLLresult = (LinearLayout) findViewById(R.id.ll_result);

        mBtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playItem();
            }
        });


        mBtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTag();
            }
        });
    }


    //打标签的方法
    private void setTag() {
        int currentTagA = mediaPlayer.getCurrentPosition();
        int currentTagB = mediaPlayer.getDuration();
        float tagA = 0;
        float tagB = calculateNumb(currentTagB);
        if(currentTagA >0){
            tagA = currentTagA / calcTimes;
        }

        showSetTagDialog(tagA, tagB);
    }
    //计算分刻度时要用到的划分倍数,如果没有这个,那么划分刻度过多造成界面卡顿甚至ANR
    private float calculateNumb(int duration) {
        if (duration >= 100000000 && duration <= 999999999) {

            calcTimes = 100000;
            return duration / 100000;
        }
        if (duration >= 10000000 && duration <= 99999999) {

            calcTimes = 10000;
            return duration / 10000;

        }
        if (duration >= 1000000 && duration <= 9999999) {

            calcTimes = 1000;
            return duration / 1000;
        }
        if (duration >= 100000 && duration <= 999999) {

            calcTimes = 100;
            return duration / 100;
        }
        if (duration >= 10000 && duration <= 99999) {

            calcTimes = 10;
            return duration / 10;
        }
        return duration;
    }


    //弹出打标签的窗口
    private void showSetTagDialog(float tagA, float tagB) {
        View view = View.inflate(this, R.layout.dialog_two_btn_settag, null);
        roundCornerDialog = new RoundCornerDialog(this, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        final RangeBar rangeBar = (RangeBar) view.findViewById(R.id.rangebar);
        final EditText et_tagName = (EditText) view.findViewById(R.id.et_message);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_onfirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        rangeBar.setTickStart(0);
        rangeBar.setTickEnd(tagB);
        rangeBar.setRangePinsByValue(tagA, tagB);
        rangeBar.setConnectingLineWeight(8);
        rangeBar.setConnectingLineColor(0xffe01b2b);
        rangeBar.setPinColor(0xffe01b2b);
        rangeBar.setTickColor(0x00000000);
        rangeBar.setTickHeight(8);
        rangeBar.setBarWeight(16);
        rangeBar.setSelectorColor(0xffe01b2b);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                int leftTagValue = leftPinIndex;
                int rightTagValue = rightPinIndex;
                Log.e("leftTagValue",leftTagValue+"");
                Log.e("rightTagValue",rightTagValue+"");

            }
        });

        rangeBar.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String s) {

                return Util.formatTime(Integer.parseInt(s) * calcTimes);
            }
        });


        //
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_tagName.getText().toString().trim().equals("")){
                    int leftTagIndexValue = rangeBar.getLeftIndex() * calcTimes;
                    int rightTagIndexValue = rangeBar.getRightIndex() * calcTimes;
                    String leftTagValue = Util.formatTime(leftTagIndexValue);
                    String rightTagValue = Util.formatTime(rightTagIndexValue);
                    mEtLeftIndexValue.setText(leftTagValue);
                    mEtRightIndexValue.setText(rightTagValue);
                    roundCornerDialog.dismiss();
                    mLLresult.setVisibility(View.VISIBLE);
                    // TODO: 2018/11/20 做节点保存的操作

                }else {
                    Toast.makeText(getApplicationContext(),"请输入节点名",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    //处理dialog点击空白处
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    // 播放当前 position 指定的歌曲
    private void playItem(){
        try {
            createMediaPlayerIfNeeded();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //这个是我项目中的一个音频地址，我懒得找别的地址了就找了这个，如果不能使用了请更换可播放的音频地址
            mediaPlayer.setDataSource("http://cdn1.hxeduonline.com/data/upload/resources/audio/05887125946259102.mp3");
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            Log.e("Exception", "playing song:", e);
        }

    }
    private void createMediaPlayerIfNeeded() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

            // we want the media player to notify us when it's ready preparing,
            // and when it's done playing:
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
        } else {
            mediaPlayer.reset();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }
}
