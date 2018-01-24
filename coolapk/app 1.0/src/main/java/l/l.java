/*
 全部手机都能用的桌面 源码

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
import android.view.animation.*;

public class l extends Activity implements OnItemClickListener,OnItemLongClickListener,Comparator<a>
{
    GridView g;
	int w,h,p;
	List<a> a=new ArrayList<a>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		/*
		 在AndroidManifest.xml添加

		 android:theme="@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen"

		 再用setTheme，就型成一种启动效果
		 */
		setTheme(android.R.style.Theme_Wallpaper_NoTitleBar);
		
		//状态栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(0);
		}else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//获取屏幕参数
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		p=w/60;
		
		//获取桌面列表
		List<ResolveInfo>r = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
		
		//提取桌面关键信息
		for(int i=0;i<r.size();i++)
		{
			ActivityInfo ai=r.get(i).activityInfo;
			a.add(new a(
			ai.loadLabel(getPackageManager()),
			ai.loadIcon(getPackageManager()),
			ai.packageName,
			ai.name));
		}
		
		//对桌面图标进行排序
		Collections.sort(a, this);
		
		g = new GridView(this);
		g.setAdapter(new b());
		g.setNumColumns(5);
		g.setOnItemClickListener(this);
		g.setOnItemLongClickListener(this);
        
		addContentView(g,new WindowManager.LayoutParams());
    }
	
	//点击事件
	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		startActivity(new Intent().setComponent(new ComponentName(a.get(i).p(), a.get(i).c())));
	}
	
	//长按事件，可去除
	@Override
	public boolean onItemLongClick(AdapterView<?> av, View v, int i, long l)
	{
		String s=a.get(i).p();
		
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
	
	//排序规则
	@Override
	public int compare(a a, a b)
	{
		Collator c = Collator.getInstance(java.util.Locale.CHINA);
		if (c.compare(a.t(), b.t())>0){  
			return 1;  
		}else if (c.compare(a.t(), b.t())<0){  
			return -1;  
		}
		return 0;
	}
	
	//重写退出事件，本程序为主桌面时，可防止误退导致手机出错
	@Override
	public void finish(){}
	
	
	//桌面图标的主要内容
	class b extends BaseAdapter {

        @Override
        public int getCount(){return a.size();}

        @Override
        public Object getItem(int i){return a.get(i);}

        @Override
        public long getItemId(int i){return i;}

        @Override
        public View getView(int id, View v, ViewGroup vg) {

			c c;
			//生成桌面小格框架
			if(v == null){
				c = new c();
				
				ImageView i  ;
				LinearLayout l;
				TextView t;
				
				l=new LinearLayout(getApplication());
				l.setOrientation(LinearLayout.VERTICAL);
				l.setGravity(Gravity.CENTER);

				//桌面图标
				i = new ImageView(getApplication());
				i.setPadding(p,p,p,p);
				i.setScaleType(ImageView.ScaleType.FIT_CENTER);
				i.setLayoutParams(new GridView.LayoutParams(w/6, w/6));
				l.addView(i);

				//应用名
				t=new TextView(getApplication());
				t.setMaxLines(1);
				t.setGravity(Gravity.CENTER);
				l.addView(t);

				c.i=i;
				c.t=t;
				v=l;
				
				v.setTag(c);
				
            } else c=(c) v.getTag();
			
			//写入图标和应用名
			c.i.setImageDrawable(a.get(id).i());
			c.t.setText(a.get(id).t());
			
            return v;
        }
    }
}
//自定义列表
class a{
	CharSequence t;
	Drawable i;
	String p,c;

	a(CharSequence t,Drawable i, String p, String c)
	{
		this.t=t;
		this.i=i;
		this.p=p;
		this.c=c;
	}
	
	CharSequence t(){return t;}
	Drawable i(){return i;}
	String p(){return p;}
	String c(){return c;}
}

//桌面小格的框架
class c{ImageView i;TextView t;}

/*
	全部代码都完成了
	
	代码由【基哥科技】创作。
	嘻嘻，团队目前只有基哥一人。
	代码不多，但也很用心研究了半个月。
*/
