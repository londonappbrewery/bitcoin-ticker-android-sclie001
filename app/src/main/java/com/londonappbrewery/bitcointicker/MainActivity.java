package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.lang.Throwable;

//import org.json.JSONException;




public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        /*make API call based on what currency user selected in spinner*/
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Bitcoin", "" + adapterView.getItemAtPosition(i));
                Log.d("Bitcoin", "Position is: " + i);

                String finalUrl = BASE_URL + adapterView.getItemAtPosition(i);
                Log.d("Bitcoin", "Final url is: " + finalUrl);
                letsDoSomeNetworking(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //first item selected by default, so this never happens
                Log.d("Bitcoin","Nothing selected");
            }
        });

    }

    /*make an API call to receive JSON response*/
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "Success! JSON: " + response.toString() + "Status code " + statusCode);
                try {
                    //parse JSON response
                    String price = response.getString("last");
                    updatePriceUI(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response " + errorResponse);
                Log.d("ERROR", throwable.toString());
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT ).show();
            }
        });



    }
    /*update the price UI with JSON respose*/
    public void updatePriceUI(String price){
        mPriceTextView.setText(price);
    }
}
