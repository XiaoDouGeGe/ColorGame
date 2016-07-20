package xiaodou.colorgame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int WHOLE_TIME = 60;
    private static LinearLayout mContainer;
    private static ColorLayout mColorLayout;
    private static Button mPauseBtn;
    private static Button mGoOnBtn; //在暂停界面，绑定continue()方法;在结束界面，绑定restart()方法;
    private static TextView mTimeTv;
    private static TextView mScoresTv;

    private static LinearLayout mResultPanel;
    private static TextView mResultScoresTv;
    private static TextView mResultInfoTv;

    private static Button mShareButton;
    private static int time = WHOLE_TIME;
    private static int scores = 0;
    private static Context context;


    private static View.OnClickListener goonListener = new View.OnClickListener() {
        public void onClick(View view) {
            goonGame();
        }
    };
    private static View.OnClickListener restartListener = new View.OnClickListener() {
        public void onClick(View view) {
            resetGame();
            goonGame();
        }
    };
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0 && time > 0) {
                time--;
                mTimeTv.setText(time + "");
                if (time <= 0) {
                    resultGame();
                } else {
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    };

    private static void pauseGame() {
        mPauseBtn.setVisibility(View.INVISIBLE);
        mColorLayout.pause();
        mContainer.setVisibility(View.VISIBLE);
        mHandler.removeMessages(0);
    }

    //继续游戏Continue
    private static void goonGame() {
        mPauseBtn.setVisibility(View.VISIBLE);
        mColorLayout.goon();
        mContainer.setVisibility(View.INVISIBLE);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private static void resultGame() {
        mShareButton.setText(R.string.share);
        mShareButton.setOnClickListener(shareListener);
        mContainer.setVisibility(View.VISIBLE);
        mResultPanel.setVisibility(View.VISIBLE);
        mPauseBtn.setVisibility(View.INVISIBLE);
        mResultScoresTv.setText(context.getString(R.string.result_scores, scores));
        mResultInfoTv.setText(Utils.calculateResult(context, scores, 0));
        mGoOnBtn.setText(R.string.restart);
        mHandler.removeMessages(0);
        mGoOnBtn.setOnClickListener(restartListener);
        mColorLayout.pause();
    }

    private static String mShareTitle = "";


    private static void resetGame() {
        mShareButton.setText(R.string.restart);
        mShareButton.setOnClickListener(restartListener);
        mResultPanel.setVisibility(View.INVISIBLE);
        scores = 0;
        time = WHOLE_TIME;
        mGoOnBtn.setText(R.string.goton);
        mScoresTv.setText(R.string.scores_0);
        mTimeTv.setText(time + "");
        mGoOnBtn.setOnClickListener(goonListener);
        mColorLayout.reset();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        mHandler.sendEmptyMessageDelayed(0, 1000);
        mColorLayout = (ColorLayout) findViewById(R.id.id_color_panel);

        mShareButton = (Button) findViewById(R.id.id_share_btn);

        mContainer = (LinearLayout) findViewById(R.id.id_ad);
        mPauseBtn = (Button) findViewById(R.id.id_pause_btn);
        mGoOnBtn = (Button) findViewById(R.id.id_goon_btn);
        mScoresTv = (TextView) findViewById(R.id.id_scores_tv);
        mTimeTv = (TextView) findViewById(R.id.id_time_tv);

        mResultPanel = (LinearLayout) findViewById(R.id.id_reslut_panel);
        mResultScoresTv = (TextView) findViewById(R.id.id_result_scores_tv);
        mResultInfoTv = (TextView) findViewById(R.id.id_result_info_tv);

        mColorLayout.setOnClickRightColorListener(new ColorLayout.OnClickRightColorListener() {
            @Override
            public void clickRightColor(int score) {
                scores = score;
                mScoresTv.setText(getString(R.string.scores, scores));
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pauseGame();
            }
        });
        mGoOnBtn.setOnClickListener(goonListener);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mContainer.setLayoutParams(params);
                mContainer.setVisibility(View.INVISIBLE);
            }
        });

        mShareButton.setText(R.string.restart);
        mShareButton.setOnClickListener(restartListener);
    }

    private static View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mHandler.sendEmptyMessage(1);
        }
    };


    public void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
        pauseGame();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetGame();
    }
}

