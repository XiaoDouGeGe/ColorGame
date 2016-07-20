package xiaodou.colorgame;

import android.content.Context;

public class Utils {

    public static String calculateResult(Context context, int score, int type) {

        String mLevel = "";
        String[] res = context.getResources().getStringArray(R.array.result_level);

        if (type == 0) {
            if (score <= 0) {
                mLevel = context.getString(R.string.result_info, res[0]);
            } else if (score > 48) {
                mLevel = context.getString(R.string.result_info_best);
            } else {
                mLevel = context.getString(R.string.result_info, res[score / 8]);
            }
        } else {
            if (score <= 0) {
                mLevel = context.getString(R.string.share_result_info, res[0]);
            } else if (score > 48) {
                mLevel = context.getString(R.string.share_result_info_best);
            } else {
                mLevel = context.getString(R.string.share_result_info, res[score / 8]);
            }
        }

        return mLevel;
    }

}

