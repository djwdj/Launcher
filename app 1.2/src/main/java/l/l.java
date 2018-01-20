/*
 5k桌面 全部手机都能用 源码

 作者：基哥

 AndroidManifest.xml重要配置：
 <category android:name="android.intent.category.HOME" />
 <category android:name="android.intent.category.DEFAULT" />
 壁纸背景(三选一)：
 android:theme="@android:style/Theme.Wallpaper"
 android:theme="@android:style/Theme.Wallpaper.NoTitleBar"
 android:theme="@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen"
 */

package l;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;
import android.util.*;
import android.net.*;
import android.provider.*;
import android.graphics.drawable.*;
import java.text.*;

public class l extends Activity implements OnItemClickListener,OnItemLongClickListener,Comparator<ResolveInfo>
{
    GridView g;
    List<ResolveInfo> a;
	int w,h,p;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Wallpaper_NoTitleBar);
		/*
		 在AndroidManifest.xml添加

		 android:theme="@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen"

		 再用setTheme，就型成一种启动效果
		 */
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(0);
		}else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		p=w/60;

		a = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
		Collections.sort(a, this);
		
		g = new GridView(this);
        g.setAdapter(new a());
		g.setNumColumns(5);
		g.setOnItemClickListener(this);
		g.setOnItemLongClickListener(this);

		addContentView(g,new WindowManager.LayoutParams());
    }
	
	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		ActivityInfo ai=a.get(i).activityInfo;
		startActivity(new Intent().setComponent(new ComponentName(ai.packageName, ai.name)));
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> av, View v, int i, long l)
	{
		String s=a.get(i).activityInfo.packageName;
		Intent it = new Intent();
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 9) {
			it.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			it.setData(Uri.fromParts("package", s, null));
		} else if (Build.VERSION.SDK_INT <= 8) {
			it.setAction(Intent.ACTION_VIEW);
			it.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			it.putExtra("com.android.settings.ApplicationPkgName", s);
		}
		startActivity(it);

		return true;
	}
	
	@Override
	public int compare(ResolveInfo a, ResolveInfo b)
	{
		Collator c = Collator.getInstance(java.util.Locale.CHINA);
		if (c.compare(a.activityInfo.loadLabel(getPackageManager()), b.activityInfo.loadLabel(getPackageManager()))>0){  
			return 1;  
		}else if (c.compare(a.activityInfo.loadLabel(getPackageManager()), b.activityInfo.loadLabel(getPackageManager()))<0){  
			return -1;  
		}
		return 0;
	}
	@Override
	public void finish(){}
	
	class a extends BaseAdapter {

        @Override
        public int getCount(){return a.size();}

        @Override
        public Object getItem(int i){return a.get(i);}

        @Override
        public long getItemId(int i){return i;}

        @Override
        public View getView(int id, View v, ViewGroup vg) {

			b b;

			if(v == null){
				b = new b();

				ImageView i  ;
				LinearLayout l;
				TextView t;

				l=new LinearLayout(getApplication());
				l.setOrientation(LinearLayout.VERTICAL);
				l.setGravity(Gravity.CENTER);

				i = new ImageView(getApplication());
				i.setPadding(p,p,p,p);
				i.setScaleType(ImageView.ScaleType.FIT_CENTER);
				i.setLayoutParams(new GridView.LayoutParams(w/6, w/6));
				l.addView(i);

				t=new TextView(getApplication());
				t.setMaxLines(1);
				t.setGravity(Gravity.CENTER);
				l.addView(t);

				b.i=i;
				b.t=t;
				v=l;
				
				v.setTag(b);
				
            } else b=(b) v.getTag();
			
			ActivityInfo ai=a.get(id).activityInfo;
			b.i.setImageDrawable(ai.loadIcon(getPackageManager()));
			b.t.setText(ai.loadLabel(getPackageManager()));
			
            return v;
        }
    }
	static class b{ImageView i;TextView t;}
}
