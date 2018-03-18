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
import android.content.res.*;
import android.annotation.*;

public class l extends Activity implements Comparator<a>,OnClickListener,OnLongClickListener
{
	int w,h,p;
	float d,z;
	List<ResolveInfo> r;
	List<a> a=new ArrayList<a>();
	ll ll;
	ScrollView s;
	r ir;
	int n=5,im;
	
	LinearLayout l;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		//状态栏透明
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setNavigationBarColor(0);
			getWindow().setStatusBarColor(0);
		}else 
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		
		
		//获取屏幕参数
		DisplayMetrics dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		d = dm.density;
		p=w/120;
		
		im=w/6;
		z=w/d/32;
		if(w>h)
		{
			n=8;
			im=w/9;
			z=w/d/48;
		}
		if(w*h<=360*360)
		{
			n=3;
			im=w/4;
			z=w/d/16;
		}
		
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
		ll.set(n,p,p);
		//ll.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		for(int id=0;id<a.size();id++)
		{
			
			ImageView i  ;
			LinearLayout l,l0;
			TextView t;
			HorizontalScrollView hs;
			//主框
			l=new LinearLayout(getApplication());
			l.setOrientation(LinearLayout.VERTICAL);
			l.setGravity(Gravity.CENTER);
			l.setId(id);
			l.setOnClickListener(this);
			l.setOnLongClickListener(this);
			l.setFocusable(true);
			l.setBackgroundDrawable(d());
			//l.setBackgroundColor(0xffff5555);
			//桌面图标
			i = new ImageView(getApplication());
			i.setPadding(p,p,p,p);
			//i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new LayoutParams(im, im));
			i.setImageDrawable(a.get(id).i);
			l.addView(i);
			//应用名框
			l0=new LinearLayout(this);
			
			l0.setGravity(Gravity.CENTER);
			//处理应用名过长
			hs=new HorizontalScrollView(this);
			hs.setFocusable(false);
			hs.setHorizontalScrollBarEnabled(false);
			
			//应用名
			t=new TextView(getApplication());
			t.setShadowLayer(02,0.5f,0.5f,0xff000000);
			t.setTextColor(-1);
			t.setTextSize(z);
			t.setText(a.get(id).t);
			
			hs.addView(t);
			l0.addView(hs);
			l.addView(l0);
			//添加到基式布局
			ll.addView(l);
		}
		
		s=new ScrollView(this);
		s.addView(ll);
		if(Build.VERSION.SDK_INT>=14)
		s.setFitsSystemWindows(true);
		
		l=new LinearLayout(this);
		//if(Build.VERSION.SDK_INT>=19)l.setPadding(0,sb(),0,nb());
		l.addView(s);
		
		addContentView(l,new WindowManager.LayoutParams());
		
	}
	Drawable d()
	{
		GradientDrawable g = new GradientDrawable();
		g.setCornerRadius(12);
		g.setColor(0xdd333333);
		
		StateListDrawable s = new StateListDrawable();
		s.addState(new int[]{android.R.attr.state_focused}, g);
		s.addState(new int[]{android.R.attr.state_pressed}, g);
		//stateDrawable.addState(new int[]{android.R.attr.state_enabled}, g);
		return s;
	}
//	int sb() 
//	{
//		return getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen","android"));
//	}
//	
//	int nb() 
//	{
//		return getResources().getDimensionPixelSize(getResources().getIdentifier("navigation_bar_height", "dimen","android"));
//	}
//	
//	@Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) 
//		{
//			getWindow().getDecorView().setSystemUiVisibility
//			(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
//				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
//				View.SYSTEM_UI_FLAG_FULLSCREEN|
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//			);
//        }
//    }
	
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
		try{
		if (Build.VERSION.SDK_INT >= 9)
			startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
						  .setData(Uri.fromParts("package", a.get(v.getId()).p, null))
						  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}catch(Exception e){}
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
	
	@Override
	public void finish()
	{
	}
	
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

	@Override
	public void recreate()
	{
		if(Build.VERSION.SDK_INT>=11)
			super.recreate();
		else 
		{
			startActivity(new Intent(this,getClass()));
			super.finish();
		}
	}
	
	class r extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context c, Intent intent)
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
	int n=2,H,V,W;

	ll(Context c){super(c);}

	public
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
		W = maxWidth - getPaddingLeft() - getPaddingRight();
        int x = 0;
        int y = 0;
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
		y += (row + 2) * V+getPaddingTop() + getPaddingBottom();
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
                int width = (W - H) / n - H;
                int height = v.getMeasuredHeight();
                x += width + H;
                y = row * (height + V)+t;
                if (x > l + W)
				{
                    x = l + H + width;
                    row++;
                    y = row * (height + V)+t;
                }
				v.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
						  MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                v.layout(x - width, y - height, x, y);
            }
        }
	}
}


