package com.homephonenew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.homephonenew.LightActivity.DownTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WetActivity extends Activity
{
	private Button refresh;
	public  static String re=null;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_wet);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
		 //�޸�Activity����
	    TextView title = (TextView) findViewById(R.id.title_text);
	    title.setText("��ʪ�ȼ��");
	    
	    /*
	     * ����ˢ�°�ť��Ӧ������ִ��ˢ�µƲ���
	     */
	    refresh = (Button) findViewById(R.id.button_refresh_wet);
		
		refresh.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v_refresh_wet)
			{
				// TODO Auto-generated method stub
				//ʹ��URL�������ݿ�
				DownTask task_wet = new DownTask(WetActivity.this);
				try
				{
					task_wet.execute(new URL(MainActivity.URL_Server));
				} 
				catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		});
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
				Toast toast2 = Toast.makeText(WetActivity.this, "ˢ��ʧ�ܣ��������ӳ���", Toast.LENGTH_LONG);
				toast2.show();
			}
			pdialog.dismiss();
			re=result;
			if(re!=null)
			{
				int out = update();
				if(out==101)
				{
					Toast toaster = Toast.makeText(WetActivity.this, "ˢ��ʧ�ܣ��õ���δע�ᣡ", Toast.LENGTH_LONG);
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



	public int update()
	{
		// TODO Auto-generated method stub
		System.out.println(re);
		if(re.indexOf("Data is null")!=-1)
		{
			return 101;
		}
		
		/*
		 * ������������ݣ���������IP��ַ
		 */
		int start,end;
		String environments;
		final String tvIp;
		String [] environmentArray = new String[50]; int count_environments = 0;
		
		start=re.indexOf("Environment_id")+17;
		end=re.indexOf(",", start)-1;
		environments=re.substring(start,end)+";";
		
		start=re.indexOf("ipAddress")+12;
		end=re.indexOf("}", start)-1;
		tvIp=re.substring(start,end);
		
		System.out.println(environments);
		System.out.println(tvIp);
		
		/*
		 *	����ӻ������ݴ�����ȡ�����ĵ�ID�浽������lightArray�У� 
		 */
		for(int a=0,b=0; b < environments.length(); a++,b++)
		{
			while(environments.charAt(b)!=';')b++;
			environmentArray[count_environments++]=environments.substring(a, b);
			a=b;
		}
		
		for(int i=count_environments;i<50;i++)
		{
			environmentArray[i]="";
		}
		final int count_environments_final = count_environments;
		final String [] environmentArray_final = environmentArray;
		
		/*
		 * ������BaseAdapter��ʾlistview����
		 */
		ListView list1 = (ListView) findViewById(R.id.ListViewWet);
		BaseAdapter adapter1 = new BaseAdapter()
		{
			public int getCount() 
			{
				// TODO Auto-generated method stub
				return count_environments_final/2;
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
				LinearLayout line = new LinearLayout(WetActivity.this);
				line.setOrientation(1);
				//�¶�
				TextView text_wendu = new TextView(WetActivity.this);
				text_wendu.setText("   �¶ȼƣ�"+environmentArray_final[position]+"   "+"�¶ȣ�"+getWendu(tvIp,environmentArray_final[position])+" ��");
				text_wendu.setTextSize(20);
				text_wendu.setTextColor(Color.BLACK);
				//ʪ��
				TextView text_shidu = new TextView(WetActivity.this);
				text_shidu.setText("   ʪ�ȼƣ�"+environmentArray_final[position+1]+"   "+" ʪ�ȣ�"+getShidu(tvIp,environmentArray_final[position+1])+" %RH");
				text_shidu.setTextSize(20);
				text_shidu.setTextColor(Color.BLACK);
				
				
				line.addView(text_wendu);
				line.addView(text_shidu);
				line.setGravity(Gravity.CENTER);
				return line;
			}	
		};
		list1.setAdapter(adapter1);
		return 0;
	}


	/*
	 * ��ȡ�¶�
	 */
	protected String getShidu(String tvIp, String wenduID)
	{
		// TODO Auto-generated method stub
		String wendu = "20";
		
		
		return wendu;
	}


	/*
	 * ��ȡʪ��
	 */
	protected String getWendu(String string, String shiduID)
	{
		// TODO Auto-generated method stub
		String shidu = "20";
		
		return shidu;
	}
	
}
