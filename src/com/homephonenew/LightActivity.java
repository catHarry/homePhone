package com.homephonenew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class LightActivity extends Activity
{
	private Button refresh;
	public  static String re=null;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_light);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
		 //�޸�Activity����
	    TextView title = (TextView) findViewById(R.id.title_text);
	    title.setText("��Ƽ��");
	    
	    /*
	     * ����ˢ�°�ť��Ӧ������ִ��ˢ�µƲ���
	     */
	    refresh = (Button) findViewById(R.id.button_refresh_light);
		
		refresh.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v_refresh_light)
			{
				// TODO Auto-generated method stub
				//ʹ��URL�������ݿ�
				DownTask task = new DownTask(LightActivity.this);
				try {
					task.execute(new URL(MainActivity.URL_Server));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
		});
	}
	
	/*
	 * 	UI������·��������첽�������֮��ֱ�ӵ���
	 */
	
	private int update()
	{
		// TODO Auto-generated method stub
		
		System.out.println(re);
		if(re.indexOf("Data is null")!=-1)
		{
			return 101;
		}
		
		/*
		 * ����������ݣ���������IP��ַ
		 */
		int start,end;
		String environments,tvIp,lights;
		String [] lightArray = new String[50]; int count_lights = 0;
		//String [] environmentArray = new String[50];
		
		start=re.indexOf("Light_id")+11;
		end=re.indexOf(",", start)-1;
		lights=re.substring(start,end)+";";
		
		start=re.indexOf("Environment_id")+17;
		end=re.indexOf(",", start)-1;
		environments=re.substring(start,end)+";";
		
		start=re.indexOf("ipAddress")+12;
		end=re.indexOf("}", start)-1;
		tvIp=re.substring(start,end);
		
		System.out.println(environments);
		System.out.println(lights);
		System.out.println(tvIp);
		
		
		
		
		/*
		 *	����ӵ����ݴ�����ȡ�����ĵ�ID�浽������lightArray�У� 
		 */
		
		for(int a=0,b=0; b < lights.length(); a++,b++)
		{
			while(lights.charAt(b)!=';')b++;
			lightArray[count_lights++]=lights.substring(a, b);
			a=b;
		}
		
		System.out.printf("����:%d",count_lights);
		for(int i=count_lights;i<50;i++)
		{
			lightArray[i]="";
		}
		final int count_lights_final = count_lights;
		final String [] lightArray_final = lightArray;
		/*
		 * ������BaseAdapter��ʾlistview����
		 */
		ListView list1 = (ListView) findViewById(R.id.ListView1);
		
		//ArrayAdapter<String> adapter1 = new ArrayAdapter<String> (activity,R.layout.array_item,lightArray);
		
		BaseAdapter adapter1 = new BaseAdapter()
		{
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return count_lights_final;
			}

			public Object getItem(int arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}

			public long getItemId(int position)
			{
				// TODO Auto-generated method stub
				return position;
			}

			public View getView(final int position, View convertView, ViewGroup parent)
			{
				// TODO Auto-generated method stub
				LinearLayout line = new LinearLayout(LightActivity.this);
				line.setOrientation(0);
				//��Ʊ�ʶ
				TextView text = new TextView(LightActivity.this);
				text.setText("   ��ƣ�"+lightArray_final[position]+"   ");
				text.setTextSize(20);
				text.setTextColor(Color.BLACK);
				//���ͼƬ
				final ImageView image = new ImageView(LightActivity.this);
				image.setImageResource(R.drawable.lightoff);
				
				
				final Button button_open = new Button(LightActivity.this);
				button_open.setText("�򿪵��");
				
				final Button button_close = new Button(LightActivity.this);
				button_close.setText("�رյ��");
				
				//���ư�ť
				button_open.setOnClickListener(new OnClickListener()
				{

					public void onClick(View arg0)
					{
						// TODO Auto-generated method stub
						boolean re;
						re=openLight(lightArray_final[position]);
						if(re == true)
						{
							button_open.setEnabled(false);
							button_close.setEnabled(true);
							image.setImageResource(R.drawable.lighton);
							Toast toast_ok = Toast.makeText(LightActivity.this,"�򿪵�"+lightArray_final[position]+"�ɹ���",Toast.LENGTH_LONG);
							toast_ok.setGravity(Gravity.CENTER, 0, 0); 
							toast_ok.show();
						}
						else
						{
							Toast toast_er = Toast.makeText(LightActivity.this,"�򿪵�"+lightArray_final[position]+"ʧ�ܣ�",Toast.LENGTH_LONG);
							toast_er.setGravity(Gravity.CENTER, 0, 0); 
							toast_er.show();
						}
					}
				});
				
				//�صư�ť
				button_close.setOnClickListener(new OnClickListener()
				{

					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						boolean re;
						re=closeLight(lightArray_final[position]);
						if(re == true)
						{
							button_open.setEnabled(true);
							button_close.setEnabled(false);
							image.setImageResource(R.drawable.lightoff);
							Toast toast_ok = Toast.makeText(LightActivity.this,"�رյ�"+lightArray_final[position]+"�ɹ���",Toast.LENGTH_LONG);
							toast_ok.setGravity(Gravity.CENTER, 0, 0); 
							toast_ok.show();
						}
						else
						{
							Toast toast_er = Toast.makeText(LightActivity.this,"�رյ�"+lightArray_final[position]+"ʧ�ܣ�",Toast.LENGTH_LONG);
							toast_er.setGravity(Gravity.CENTER, 0, 0); 
							toast_er.show();
						}
					}
				});
				
				line.addView(text);
				line.addView(button_open);
				line.addView(button_close);
				line.addView(image);
				line.setGravity(Gravity.CENTER);
				return line;
			}	
		};
		list1.setAdapter(adapter1);
		
		return 0;
	}
	
	/*
	 * ����ֱ�Ϊ���ƺ͹صƵĿ���
	 */
	private boolean openLight(String lightId) 
	{
		// TODO Auto-generated method stub
		Toast toast12 = Toast.makeText(LightActivity.this,"���ڴ򿪵ƣ�"+lightId+"......", Toast.LENGTH_LONG);
		toast12.show();
		/*
		 * 
		 */
		
		
		//���ɹ��򿪵ƣ�����true,��ť��ң����򷵻�false
		return true;
	}
	
	private boolean closeLight(String lightId)
	{
		// TODO Auto-generated method stub
		Toast toast13 = Toast.makeText(LightActivity.this,"���ڹرյƣ�"+lightId+"......", Toast.LENGTH_LONG);
		toast13.show();
		/*
		 * 
		 */
		
		
		//�ɹ��򿪵ƣ�����true,��ť��ң����򷵻�false
		return true;
	}
	
	class DownTask extends AsyncTask<URL, Integer, String>
	{
		// �ɱ䳤�������������AsyncTask.exucute()��Ӧ
		ProgressDialog pdialog;
		// �����¼�Ѿ���ȡ�е�����
		int hasRead = 0;
		Context mContext;

		public DownTask(Context ctx)
		{
			mContext = ctx;
		}

		@Override
		protected String doInBackground(URL... params)
		{
			StringBuilder sb = new StringBuilder();
			try
			{
				URLConnection conn = params[0].openConnection();
				// ��conn���Ӷ�Ӧ������������������װ��BufferedReader
				BufferedReader br = new BufferedReader(	new InputStreamReader(conn.getInputStream()	, "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null)
				{
					sb.append(line + "\n");
					hasRead++;
					publishProgress(hasRead);
				}
				return sb.toString();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
		/*
		 * ������õ����������صĽ����
		 */
		@Override
		protected void onPostExecute(String result)
		{
			// ����HTMLҳ�������
			//show.setText(result);
			if(result!=null)
			{
				//Toast toast6 = Toast.makeText(activity, result, Toast.LENGTH_LONG);
				//toast6.show();
			}
			else
			{
				Toast toast2 = Toast.makeText(LightActivity.this, "ˢ��ʧ�ܣ��������ӳ���", Toast.LENGTH_LONG);
				toast2.show();
			}
			pdialog.dismiss();
			re=result;
			if(re!=null)
			{
				int out = update();
				if(out==101)
				{
					Toast toaster = Toast.makeText(LightActivity.this, "ˢ��ʧ�ܣ��õ���δע�ᣡ", Toast.LENGTH_LONG);
					toaster.show();
				}
			}
		}

		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(mContext);
			// ���öԻ���ı���
			pdialog.setTitle("��������ִ����");
			// ���öԻ��� ��ʾ������
			pdialog.setMessage("��������ִ���У���ȴ�...");
			// ���öԻ������á�ȡ������ť�ر�
			pdialog.setCancelable(false);
			// ���øý�������������ֵ
			pdialog.setMax(100);
			// ���öԻ���Ľ��������
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// ���öԻ���Ľ������Ƿ���ʾ����
			pdialog.setIndeterminate(false);
			pdialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// ���½���
			//show.setText("�Ѿ���ȡ�ˡ�" + values[0] + "���У�");
			pdialog.setProgress(values[0]);
		}
	}
	
}
