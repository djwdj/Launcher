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
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.text.*;

public class l extends Activity implements Comparator<a>,OnClickListener,OnLongClickListener,OnFocusChangeListener,Runnable
{
	int w,h,p;
	float d,z;
	List<ResolveInfo> r;
	List<a> a=new ArrayList<a>();
	ll ll;
	ScrollView s;
	r ir;
	int n=5,im,ui;

	LinearLayout l,li;
	LayoutParams lp = new LayoutParams(-1, -2,1);
	Button b;
	EditText e,e1,e2,e3,e4;
	PopupWindow pw;
	Dialog dg;

	Drawable da;
	int p1,p2,p3,p4;
	SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		//setTheme(android.R.style.Theme_Material_Wallpaper_NoTitleBar);
		//状态栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setNavigationBarColor(0);
			getWindow().setStatusBarColor(0);
		}
		else 
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		ui = ((UiModeManager) getSystemService(UI_MODE_SERVICE)).getCurrentModeType();
		
		sp=getSharedPreferences("l", 0);
		
		//获取屏幕参数
		DisplayMetrics dm = getResources().getDisplayMetrics();
		w = dm.widthPixels;
		h = dm.heightPixels;
		d = dm.density;
		p = w / 120;

		im = w / 6;
		z = w / d / 32;
		if (w > h)
		{
			n = 8;
			im = w / 9;
			z = w / d / 48;
		}
		if (w == h && h <= 360)
		{
			n = 3;
			im = w / 4;
			z = w / d / 16;
		}

		ll = new ll(this);
		ll.set(n, p, p);
		ll.setPadding(sp("p1"), sp("p2"), sp("p3"), sp("p4"));
		//ll.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		//app();

		s = new ScrollView(this);
		s.addView(ll);
		if (Build.VERSION.SDK_INT >= 14)
			s.setFitsSystemWindows(true);

		l = new LinearLayout(this);
		//if(Build.VERSION.SDK_INT>=19)l.setPadding(0,sb(),0,nb());
		l.addView(s);

		addContentView(l, new WindowManager.LayoutParams());

		new Handler().postDelayed(this, 100);
	}

	@Override
	public void run()
	{
		app();
	}


	void app()
	{
		r = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);

		//提取桌面关键信息
		for (int i=0;i < r.size();i++)
		{
			ActivityInfo ai=r.get(i).activityInfo;
			if (!ai.packageName.equals("l.l"))
				a.add(new a(
						  ai.loadLabel(getPackageManager()),
						  ai.loadIcon(getPackageManager()),
						  ai.packageName,
						  ai.name));
		}

		//对桌面图标进行排序
		Collections.sort(a, this);

		for (int id=0;id < a.size();id++)
		{

			ImageView i  ;
			LinearLayout l,l0;
			TextView t;
			HorizontalScrollView hs;
			//主框
			l = new LinearLayout(this);
			l.setOrientation(LinearLayout.VERTICAL);
			l.setGravity(Gravity.CENTER);
			l.setId(id);
			l.setOnClickListener(this);
			l.setOnLongClickListener(this);
			l.setOnFocusChangeListener(this);
			l.setFocusable(true);

			l.setBackgroundDrawable(Build.VERSION.SDK_INT >= 21 ?r(0xff00ff00): sd());

			//桌面图标
			i = new ImageView(this);
			i.setPadding(p, p, p, p);
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new LayoutParams(im, im));
			i.setImageDrawable(a.get(id).i);
			l.addView(i);
			//应用名框
			l0 = new LinearLayout(this);
			l0.setGravity(Gravity.CENTER);
			//处理应用名过长
			hs = new HorizontalScrollView(this);
			hs.setFocusable(false);
			hs.setHorizontalScrollBarEnabled(false);

			//应用名
			t = new TextView(this);
			t.setShadowLayer(02, 0.5f, 0.5f, 0xff000000);
			t.setTextColor(-1);
			t.setTextSize(z);
			t.setText(a.get(id).t);

			hs.addView(t);
			l0.addView(hs);
			l.addView(l0);
			//添加到基式布局
			ll.addView(l);
		}
	}

	void l()
	{
		li = new LinearLayout(this);
		//li.setPadding(p,p,p,p);
		//li.setBackgroundDrawable(d(0xff222222,64));
		//li.setOrientation(l.VERTICAL);
		li.setGravity(Gravity.CENTER);
		l.addView(li);
	}
	void e(int i,String s)
	{
		
		e=new EditText(this);
		e.setId(i);
		e.setHint(s);
		e.setTextColor(-1);
		e.setInputType( InputType.TYPE_CLASS_NUMBER);
		e.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
		e.setLayoutParams(lp);
		e.setBackgroundDrawable(d(0xff333333,32,3));
		e.setGravity(Gravity.CENTER);
		li.addView(e);
		
		
	}
	int e(int i)
	{
		try
		{
			return Integer.parseInt(((EditText)dg.findViewById(i)).getText().toString());
		}
		catch(Exception ex)
		{
			return 0;
		}
	}
	void dg()
	{
		int p=w/20;
		l = new LinearLayout(this);
		l.setPadding(p,p,p,p);
		l.setBackgroundDrawable(d(0xff222222,64));
		l.setOrientation(l.VERTICAL);
		l.setGravity(Gravity.CENTER);

		l();
		e(5021,"左");
		e(5022,"上");
		e(5023,"右");
		e(5024,"下");
		b(5020, "修改间距");

		s = new ScrollView(this);
		s.setFocusable(false);
		s.setHorizontalScrollBarEnabled(false);
		s.addView(l);
		
		l = new LinearLayout(this);
		l.setPadding(p,p,p,p);
		//l.setBackgroundDrawable(d(0xff222222,64));
		l.setOrientation(l.VERTICAL);
		l.setGravity(Gravity.CENTER);
		l.addView(s);
		
		dg = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
		//dg.getWindow().setDimAmount(0);
		//dg.getWindow().setBackgroundDrawable(new BitmapDrawable());
		//dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dg.setCanceledOnTouchOutside(false);
		//d.setCancelable(false);
		dg.setContentView(l);
		dg.show();
	}

	int sp(String s)
	{
		return sp.getInt(s,0);
	}
	void sp(String s,int i)
	{
		sp.edit().putInt(s, i).commit();
	}
	Drawable sd()
	{
		GradientDrawable g = new GradientDrawable();
		g.setCornerRadius(12);
		g.setColor(0xdd333333);

		StateListDrawable s = new StateListDrawable();
		//s.addState(new int[]{android.R.attr.state_focused}, g);
		s.addState(new int[]{android.R.attr.state_pressed}, g);
		//s.addState(new int[]{android.R.attr.state_enabled}, g);

		return s;
	}

	Drawable r(int i)
	{
		return new RippleDrawable(ColorStateList.valueOf(i), null, null);
	}

	Drawable d(int i)
	{
		return d(i, 32, 1, 0xffeeeeee, 0, 0);
	}
	Drawable d(int i, int r)
	{
		return d(i, r, 2, 0xffeeeeee, 0, 0);
	}
	Drawable d(int i, int r,int w)
	{
		return d(i, r, w, 0xffeeeeee, 0, 0);
	}
	Drawable d(int i, int r, int w, int c, int dw, int dg)
	{
		GradientDrawable g = new GradientDrawable();
		g.setColor(i);
		g.setCornerRadius(r);
		g.setStroke(w, c, dw, dg);
		return g;
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
		int i=v.getId();
		switch (i / 1000)
		{
			case 0:
				try
				{
					startActivity(new Intent()
								  .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
								  .setComponent(new ComponentName(a.get(i).p, a.get(i).c)));
				}
				catch (Exception e)
				{
					recreate();
				}
				break;
			case 1:
				try
				{
					if (Build.VERSION.SDK_INT >= 9)
						startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
									  .setData(Uri.fromParts("package", a.get(i % 1000).p, null))
									  .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
				catch (Exception e)
				{}
				if (pw != null)
					pw.dismiss();
				break;
			case 2:
				try
				{
					startActivity(new Intent(Intent.ACTION_DELETE)
								  .setData(Uri.fromParts("package", a.get(v.getId() % 1000).p, null))
								  );
				}
				catch (Exception e)
				{}
				if (pw != null)
					pw.dismiss();
				break;
			case 5:
				if (pw != null)pw.dismiss();
				switch (i % 5000)
				{
					case 1:
						Toast("桌面刷新中...", 50);
						recreate();
						break;
					case 2:

						Toast(saveImage(vb(ll)), 500);
						break;
					case 3:
						startActivity(
							new Intent(Intent.ACTION_MAIN)
							.addCategory(Intent.CATEGORY_HOME)
							.addCategory(Intent.CATEGORY_DEFAULT)
							.setClassName("android", "com.android.internal.app.ResolverActivity"));
						break;
					case 4:
						cool(getPackageName());

						break;
					case 5:
						startActivity(new Intent(null, Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3DKY9473G7xhujBXgcUAjgizWzIS-GqFz2")));
						Toast("群昵称请加上[桌面]", 3000);
						break;
					case 6:
						try
						{
							String s;
							//s="alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https://qr.alipay.com/tsx03791nki4qabwu92vi97";
							s = "YWxpcGF5czovL3BsYXRmb3JtYXBpL3N0YXJ0YXBwP3NhSWQ9MTAwMDAwMDcmY2xpZW50VmVyc2lvbj0zLjcuMC4wNzE4JnFyY29kZT1odHRwczovL3FyLmFsaXBheS5jb20vdHN4MDM3OTFua2k0cWFid3U5MnZpOTc";
							s = new String(android.util.Base64.decode(s, android.util.Base64.DEFAULT));
							startActivity(new Intent(null, Uri.parse(s)));
							Toast("捐赠请留言，并加上[桌面]！", 3000);
						}
						catch (Exception ex)
						{
							Toast("打开支付宝失败！", 50);
						}
						break;
					case 7:
						dg();
						break;
					case 20:
						sp("p1",e(5021));
						sp("p2",e(5022));
						sp("p3",e(5023));
						sp("p4",e(5024));
						dg.dismiss();
						recreate();
						break;
				}

		}


	}

	@Override
	public boolean onLongClick(View v)
	{
		pw(v);
		return true;
	}

	void pw(View v)
	{
		int i = 0;

		l = new LinearLayout(this);
		l.setBackgroundDrawable(d(0xff222222));
		//l.setOrientation(l.VERTICAL);
		l.setGravity(Gravity.CENTER);
		//l.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));

		try
		{
			i = getPackageManager().getPackageInfo(a.get(v.getId()).p, 0).applicationInfo.flags;

		}
		catch (PackageManager.NameNotFoundException e)
		{}

		pb(v.getId() + 1000, "详细");
		if ((i & ApplicationInfo.FLAG_SYSTEM) == 0 || (i & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
			pb(v.getId() + 2000, "卸载");

		int[]location=new int[2];  
        v.getLocationOnScreen(location);  
        int x=location[0];
        int y=location[1];

		pw = new PopupWindow(l, -2, -2, true);
		pw.setBackgroundDrawable(new BitmapDrawable());//解决setFocusable在安卓2.3失效
		pw.showAsDropDown(v, 0, h / 2 < y ?-(v.getHeight() * 4 / 3): 0);


	}
	Bitmap vb(View v)
	{
		int w = v.getWidth();  
        int h = v.getHeight();  
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
		c.drawColor(0xff555555);
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;  
//		v.setDrawingCacheEnabled(true);
//		v.buildDrawingCache();
//		return Bitmap.createBitmap(v.getDf5555);rawingCache());
	}
	String saveImage(Bitmap bmp)
	{
		//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
		//Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        if (bmp == null)
            return "图象获取失败";
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (Build.VERSION.SDK_INT < 8)appDir = Environment.getExternalStorageDirectory();
		if (!appDir.exists())
			appDir.mkdir();
        String fileName = new SimpleDateFormat("MMdd_HHmmss").format(new Date()) + ".png";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try
		{
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
			fos.close();
            return "图片保存在：" + file.getPath();
		}
		catch (Exception e)
		{
        }
        return "保存失败";
    }
	void b(int i, String s, Drawable d, float f, int c,int m, int p)
	{
		lp.setMargins(m, m, m, m);
		b = new Button(this);
		b.setId(i);
		b.setText(s);
		b.setTextSize(f);
		b.setTextColor(c);
		b.setLayoutParams(lp);
		b.setPadding(p, p, p, p);
		b.setBackgroundDrawable(d);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setFocusable(true);
		l.addView(b);
	}

	void pb(int i, String s)
	{
		b(i, s, new BitmapDrawable(), z * 1.2f, -1, w / 60,w/60);
	}
	void b(int i, String s)
	{
		b(i, s, d(0, w / 36), z * 2f, -1, w / 60,w/60);
	}
	void b(String s, int i)
	{
		b(i, s, new BitmapDrawable(), z * 1.6f, -1, w / 60,0);
	}

	
	
	@Override
	public void onFocusChange(View v, boolean b)
	{
		
		if (b)
		{
			da=v.getBackground();
			v.setBackgroundColor(0xff2299ff);
		}
		else v.setBackgroundDrawable(da);
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if(keyCode==82)
//			Toast.makeText(this,((UiModeManager) getSystemService(UI_MODE_SERVICE)).getCurrentModeType()+"",50).show();
//		return super.onKeyDown(keyCode, event);
//	}

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus)
//	{
//		Toast.makeText(this,"焦点",50).show();
//		super.onWindowFocusChanged(hasFocus);
//	}

	void Toast(CharSequence s)
	{
		Toast(s,50);
	}
	void Toast(CharSequence s, int i)
	{
		Toast.makeText(this, s, i).show();
	}

	void cool(String s)
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://www.coolapk.com/apk/%s", s)));
		try
		{
			startActivity(i.setPackage("com.coolapk.market"));
		}
		catch (Exception e)
		{
			startActivity(i.setPackage(null));
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		switch (ui)
		{
			case Configuration.UI_MODE_TYPE_TELEVISION:
				if (getWindow().getDecorView().findFocus() != null)
					pw(getWindow().getDecorView().findFocus());
				break;
			default:
				l = new LinearLayout(this);
				l.setBackgroundDrawable(d(0xff222222));
				l.setOrientation(l.VERTICAL);
				l.setGravity(Gravity.CENTER);
				//l.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));

				b("设置中心", 5007);
				b("更新地址", 5004);
				b("基友交流", 5005);
				b("打赏作者", 5006);

				pw = new PopupWindow(l, w < 720 ?-2: w / 2, -2, true);
				pw.setBackgroundDrawable(new BitmapDrawable());
				pw.showAtLocation(getWindow().getDecorView(), Gravity.LEFT | Gravity.BOTTOM, 0, 0);

		}


		return false;
	}

	//排序规则
	@Override
	public int compare(a a, a b)
	{
		Collator c = Collator.getInstance(java.util.Locale.CHINA);
		if (c.compare(a.t, b.t) > 0)
		{  
			return 1;  
		}
		else if (c.compare(a.t, b.t) < 0)
		{
			return -1;
		}
		return 0;

	}

	@Override
	public void finish()
	{
		l = new LinearLayout(this);
		l.setBackgroundDrawable(d(0xff222222));
		l.setOrientation(l.VERTICAL);
		l.setGravity(Gravity.CENTER);
		//l.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));

		b("刷新桌面", 5001);
		b("保存长图", 5002);
		b("切换桌面", 5003);

		pw = new PopupWindow(l, w < 720 ?-2: w / 2, -2, true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);


	}

	@Override
	protected void onStop()
	{
		if (pw != null)pw.dismiss();
		super.onStop();
	}



	@Override
	public void onStart()
	{
		super.onStart();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
		{
            try
			{
				Method setNeedsMenuKey = Window.class.getDeclaredMethod("setNeedsMenuKey", int.class);
				setNeedsMenuKey.setAccessible(true);
				setNeedsMenuKey.invoke(getWindow(), WindowManager.LayoutParams.class.getField("NEEDS_MENU_SET_TRUE").getInt(null));
			}
			catch (Exception e)
			{
			}
        }
		else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
            try
			{
				getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
			}
			catch (Exception e)
			{
			} 
        }


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
		if (Build.VERSION.SDK_INT >= 11)
			super.recreate();
		else 
		{
			startActivity(new Intent(this, getClass()));
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
class a
{
	CharSequence t;
	Drawable i;
	String p,c;

	a(CharSequence t, Drawable i, String p, String c)
	{
		this.t = t;
		this.i = i;
		this.p = p;
		this.c = c;
	}
}

class ll extends ViewGroup
{
	int n=2,H,V,W;

	ll(Context c)
	{super(c);}

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
		y += (row + 2) * V + getPaddingTop() + getPaddingBottom();
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
                y = row * (height + V) + t;
                if (x > l + W)
				{
                    x = l + H + width;
                    row++;
                    y = row * (height + V) + t;
                }
				v.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
						  MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                v.layout(x - width, y - height, x, y);
            }
        }
	}
}


