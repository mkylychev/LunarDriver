package kg.lunar.driver;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {

	private RelativeLayout mainView;
	private AdView bannerView;
	private ViewGroup bannerContainer;
	private RelativeLayout.LayoutParams bannerParams;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		mainView = new RelativeLayout(this);
		setContentView(mainView);

		View gameView= initializeForView(new LunarDriver(gameCallBack));
		mainView.addView(gameView);

		bannerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		bannerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		bannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		bannerContainer = new LinearLayout(this);

		mainView.addView(bannerContainer,bannerParams);
		bannerContainer.setVisibility(View.GONE);
	}
	private GameCallBack gameCallBack =  new GameCallBack() {
		@Override
		public void sendMassege(int massage) {
			if(massage ==LunarDriver.SHOW_BANNER){
				AndroidLauncher.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {

					}
				});

			}else if(massage ==LunarDriver.HIDE_BANNER){
				AndroidLauncher.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {

					}
				});

			}else if(massage == LunarDriver.LOAD_INTERSTITIAL){


			} else if(massage == LunarDriver.OPEN_MARKET){


			}else if(massage == LunarDriver.SHOW_INTERSTITIAL){
				AndroidLauncher.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {

					}
				});

			}else if(massage == LunarDriver.SHARE){

			}
		}
	};
}
