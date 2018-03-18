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
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import android.net.*;
import android.widget.RemoteViews.*;

public class l extends Activity implements Comparator<a>,OnClickListener,OnLongClickListener
{
	int w,h,p;
	List<ResolveInfo> r;
	List<a> a=new ArrayList<a>();
	ll ll;
	ScrollView s;
	
	r ir;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		
		
		//状态栏透明
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setNavigationBarColor(0);
			getWindow().setStatusBarColor(0);}else 
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
								 WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			
		//获取屏幕参数
		DisplayMetrics dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		p=w/120;
		
		//获取桌面列表
		r = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
		
		//提取桌面关键信息
		for(int i=0;i<r.size();i++)
		{
			ActivityInfo ai=r.get(i).activityInfo;
			if(!ai.packageName.equals("l.l"))
			a.add(new a(
			ai.loadLabel(getPackageManager()),
			ai.loadIcon(getPackageManager()),
			ai.packageName,
			ai.name));
		}
		
		//对桌面图标进行排序
		Collections.sort(a, this);
		
		ll=new ll(this);
		ll.set(5,p,p);
		ll.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		for(int id=0;id<a.size();id++)
		{
			ImageView i  ;
			LinearLayout l,l0;
			TextView t;
			HorizontalScrollView hs;
			
			l=new LinearLayout(getApplication());
			l.setOrientation(LinearLayout.VERTICAL);
			l.setGravity(Gravity.CENTER);
			l.setId(id);
			l.setOnClickListener(this);
			l.setOnLongClickListener(this);
			//l.setBackgroundColor(0xaaff0000);
			//桌面图标
			i = new ImageView(getApplication());
			i.setPadding(p,p,p,p);
			//i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new LayoutParams(w/6, w/6));
			i.setImageDrawable(a.get(id).i);
			l.addView(i);

			l0=new LinearLayout(this);
			l0.setGravity(Gravity.CENTER);
			
			hs=new HorizontalScrollView(this);
			hs.setHorizontalScrollBarEnabled(false);
			
			//应用名
			t=new TextView(getApplication());
			t.setShadowLayer(02,0.5f,0.5f,0xff000000);
			t.setTextColor(-1);
			t.setText(a.get(id).t);
			
			hs.addView(t);
			l0.addView(hs);
			l.addView(l0);
			
			ll.addView(l);
		}
		
		s=new ScrollView(this);
		s.addView(ll);
		s.setFitsSystemWindows(true);
		addContentView(s,new WindowManager.LayoutParams());
		
		
	}
	
	@Override
	public void onClick(View v)
	{
		try
		{
			startActivity(new Intent()
						  .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						  .setComponent(new ComponentName(a.get(v.getId()).p, a.get(v.getId()).c)));
		}
		catch (Exception e)
		{
			recreate();
		}
					  
	}
	
	@Override
	public boolean onLongClick(View v)
	{
		if (Build.VERSION.SDK_INT >= 9)
			startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
						  .setData(Uri.fromParts("package", a.get(v.getId()).p, null))
						  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		
		return true;
	}
	
	//排序规则
	@Override
	public int compare(a a, a b)
	{
		Collator c = Collator.getInstance(java.util.Locale.CHINA);
		if (c.compare(a.t, b.t)>0){  
			return 1;  
		}else if (c.compare(a.t, b.t)<0){  
			return -1;  
		}
		return 0;
	}
	
	//重写退出事件，本程序为主桌面时，可防止误退导致手机出错
	@Override
	public void finish(){}
	
	
	@Override
	public void onStart()
	{
		super.onStart();

		ir = new r();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		
		registerReceiver(ir, filter);
	}

	@Override
	public void onDestroy()
	{
		if (ir != null) unregisterReceiver(ir);
		super.onDestroy();
	}
	
	class r extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_PACKAGE_ADDED))
			{
				recreate();
			}
			else if (action.equals(Intent.ACTION_PACKAGE_REMOVED))
			{
				recreate();
			}
		}

	}
	
}

//自定义列表
class a{
	CharSequence t;
	Drawable i;
	String p,c;
	
	a(CharSequence t,Drawable i,String p,String c)
	{
		this.t=t;
		this.i=i;
		this.p=p;
		this.c=c;
	}
}

class ll extends ViewGroup
{
	int n=2,H,V,max;

	ll(Context c){super(c);}

	void set(int n, int h, int v)
	{
		this.n = n;
		this.H = h;
		this.V = v;
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
		max = maxWidth - getPaddingLeft() - getPaddingRight();
        int x = 0;
        int y = getPaddingTop() + getPaddingBottom();
        int row = 0;
        for (int i = 0; i < getChildCount(); i++)
		{
			View v = getChildAt(i);
            if (v.getVisibility() != View.GONE)
			{
                v.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                int height = v.getMeasuredHeight();
                x += 1;
                y = row * height + height;
                if (x > n)
				{
                    x = 1;
                    row++;
                    y = row * height + height;
                }
            }
        }
		y += (row + 2) * V;
        setMeasuredDimension(maxWidth, y);
    }

	@Override
	protected void onLayout(boolean p1, int l, int t, int r, int b)
	{
		l = getPaddingLeft();
		t = getPaddingTop();
		int x = l;
        int y = 0;
        int row = 1;
        for (int i = 0; i < getChildCount(); i++)
		{
			View v = getChildAt(i);
            if (v.getVisibility() != View.GONE)
			{
                int width = (max - H) / n - H;
                int height = v.getMeasuredHeight();
                x += width + H;
                y = row * (height + V);
                if (x > l + max)
				{
                    x = l + H + width;
                    row++;
                    y = row * (height + V);
                }
				v.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
						  MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                v.layout(x - width, y - height, x, y);
            }
        }
	}
}



