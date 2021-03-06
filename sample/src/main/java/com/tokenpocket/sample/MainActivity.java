package com.tokenpocket.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tokenpocket.opensdk.TPListener;
import com.tokenpocket.opensdk.TPManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TPManager.getInstance().transfer(MainActivity.this, getTransferData(), new TPListener() {
                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        tvTips = findViewById(R.id.tv_tips);
        tvTips.setText("");

    }

    /**
     * 按照协议构建json 字符串
     */
    private String getTransferData() {
        return "{\n" +
                "\t\"protocol\": \"SimpleWallet\",\n" +
                "\t\"version\": \"1.0\",\n" +
                "\t\"dappName\": \"Newdex\",\n" +
                "\t\"dappIcon\": \"https://newdex.io/static/logoicon.png\",\n" +
                "\t\"action\": \"transfer\",\n" +
                "\t\"from\": \"clement22222\",\n" +
                "\t\"to\": \"newdexpocket\",\n" +
                "\t\"amount\": 0.11,\n" +
                "\t\"contract\": \"eosio.token\",\n" +
                "\t\"symbol\": \"EOS\",\n" +
                "\t\"precision\": 4,\n" +
                "\t\"dappData\": \"{\\\"type\\\":\\\"buy-limit\\\",\\\"symbol\\\":\\\"IQ_EOS\\\",\\\"price\\\":\\\"0.00220\\\",\\\"count\\\":50,\\\"amount\\\":0.11}\",\n" +
                "\t\"desc\": \"\",\n" +
                "\t\"expired\": 1535944144181,\n" +
                "\t\"callback\": \"https://newdex.io/api/account/transferCallback?uuid=1-46e023fc-015b-4b76-3809-1cab3fd76d2c&type=SimpleWallet\"\n" +
                "}";
    }
}
