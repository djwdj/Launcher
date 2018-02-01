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

public class l extends Activity implements Comparator<a>,OnClickListener,OnLongClickListener
{
	int w,h,p;
	List<ResolveInfo> r;
	List<a> a=new ArrayList<a>();
	ll ll;
	LinearLayout li;
	ScrollView s;
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
		p=w/60;
		
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
		
		ll=new ll(this,5);
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
			//桌面图标
			i = new ImageView(getApplication());
			i.setPadding(p,p,p,p);
			i.setLayoutParams(new LayoutParams(w/6, w/6));
			i.setImageDrawable(a.get(id).i);
			l.addView(i);

			l0=new LinearLayout(this);
			l0.setGravity(Gravity.CENTER);
			
			hs=new HorizontalScrollView(this);
			hs.setHorizontalScrollBarEnabled(false);
			
			//应用名
			t=new TextView(getApplication());
			t.setTextColor(-1);
			t.setText(a.get(id).t);
			
			hs.addView(t);
			l0.addView(hs);
			l.addView(l0);
			
			ll.addView(l);
		}
		
		li=new LinearLayout(this);
		li.setPadding(0,p,0,p);
		li.addView(ll);
		
		s=new ScrollView(this);
		s.addView(li);
		s.setFitsSystemWindows(true);
		addContentView(s,new WindowManager.LayoutParams());
    }
	
	@Override
	public void onClick(View v)
	{
		startActivity(new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setComponent(new ComponentName(a.get(v.getId()).p,a.get(v.getId()).c)));
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
	int n;
	
	ll(Context c, int n)
	{
		super(c);
		this.n = n;
	}

	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = maxWidth/n;
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }
	
	@Override
	protected void onLayout(boolean p1, int l, int t, int r, int b)
	{
		final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = maxWidth/n;
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                if (x > maxWidth) {
                    x = width;
                    row++;
                    y = row * height + height;
                }
                child.layout(x - width, y - height, x, y);
                child.measure(width, height);
            }
        }
	}
}

