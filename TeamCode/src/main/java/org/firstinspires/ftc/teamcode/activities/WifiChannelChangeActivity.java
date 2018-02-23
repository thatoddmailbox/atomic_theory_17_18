package org.firstinspires.ftc.teamcode.activities;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.firstinspires.ftc.teamcode.R;

import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;

import java.util.ArrayList;

public class WifiChannelChangeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_channel_change);

        // https://en.wikipedia.org/wiki/List_of_WLAN_channels#5_GHz_(802.11a/h/j/n/ac/ax)
        // allowed channels in the US:
        // * 36-48
        // * 50-64 require DFS, not sure if phone supports this, STAY AWAY
        // * 100-144 require DFS, not sure if phone supports this, STAY AWAY
        // * 149-165

        ArrayList<Integer> channels = new ArrayList<>();
        for (int i = 36; i <= 48; i += 2) {
            channels.add(i);
        }
        for (int i = 149; i <= 165; i += 2) {
            if (i == 163) {
                continue;
            }
            channels.add(i);
        }

        final ListView channelView = (ListView) findViewById(R.id.wifi_channel_list);
        ArrayAdapter<Integer> itemsAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, channels);
        channelView.setAdapter(itemsAdapter);
        channelView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer channel = (Integer) channelView.getItemAtPosition(position);
                WifiDirectAgent agent = WifiDirectAgent.getInstance();
                agent.setWifiP2pChannels(0, channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Channel set successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e("WifiChannelChange", "reason: " + Integer.toString(reason));
                        Toast.makeText(getApplicationContext(), "Channel set failed! Try a different channel?", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
