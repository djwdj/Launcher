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

public class l extends Activity implements OnItemClickListener
{
    GridView g;
    List<ResolveInfo> a;
	int w,h,p;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		p=w/60;
		
		a = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
		
		g = new GridView(this);
        g.setAdapter(new a());
		g.setNumColumns(5);
        g.setOnItemClickListener(this);

		addContentView(g,new WindowManager.LayoutParams());
    }
	
	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		ActivityInfo ai=a.get(i).activityInfo;
		startActivity(new Intent().setComponent(new ComponentName(ai.packageName, ai.name)));
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
