package com.example.carson_ho.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Runnable mRunnable;
    Handler mHandler;
    //主布局中定义了一个按钮和文本
    Button button,cancel;
    TextView text;
    ProgressBar progressBar;
    MyTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        cancel = (Button) findViewById(R.id.cancel);
        //耗时任务完成时在该TextView上显示文本
        text = (TextView) findViewById(R.id.text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        //Button按下时会开启一个新线程执行耗时任务
        //任务完成后更新TextView的文本
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //步骤3:创建AsyncTask子类的实例
                //注意每次需new一个实例,新建的任务只能执行一次,否则会出现异常
                mTask = new MyTask();
                cancel.setEnabled(true);

                //步骤4:调用AsyncTask子类的实例的execute(Params... params)方法执行异步任务
                mTask.execute();
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消一个正在执行的任务,onCancelled方法将会被调用
                mTask.cancel(true);
            }
        });




    }


    /*步骤1：创建AsyncTask的子类，并为三个泛型参数制定类型
    在特定场合下，并不是所有类型都被使用，如果没有被使用，可以用java.lang.Void类型代替
    //三种泛型类型分别代表
        启动任务执行的输入参数:String类型
        后台任务执行的进度：Integer类型
        后台计算结果的类型：String类型*/
    private class MyTask extends AsyncTask<String, Integer, String> {

        //步骤2. 根据需要，实现AsyncTask的方法
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            text.setText("加载中");
        }
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {

            try {
                int count = 0;
                int length = 1;
                while (count<88) {

                    count += length;
//                    调用publishProgress公布进度, 最后onProgressUpdate方法将被执行
                    publishProgress(count);
                    Thread.sleep(50);//模拟耗时任务
                }


            }catch (InterruptedException e) {
                e.printStackTrace();
            }


                    return null;
                }




        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {

            progressBar.setProgress(progresses[0]);
            text.setText("loading..." + progresses[0] + "%");

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            text.setText("加载完了");
            button.setEnabled(true);
            cancel.setEnabled(false);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

            text.setText("已取消");
            progressBar.setProgress(0);

            button.setEnabled(true);
            cancel.setEnabled(false);
        }
    }
}