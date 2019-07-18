package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.Menu;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.MenuInflater;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ProgressDialog pDialog;
    ArrayList<Products> pro;
    ListAdapter adapt;




    String product_name, product_image, image_back, nutrition, unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        pro = new ArrayList<>();
        lv = findViewById(R.id.lst_products);
        new GetProductList().execute();
    }


    private class GetProductList extends AsyncTask<Void, Void, ArrayList<Products>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Products> doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(getResources().getString(R.string.api_link), getResources().getString(R.string.api_key));

            Log.e(TAG, "Response from url: " + jsonStr);

            JSONObject MainObj = null;
            try {
                MainObj = new JSONObject(jsonStr);
                JSONArray productArray = MainObj.getJSONArray("data");

                for (int i = 0; i < productArray.length(); i++) {

                    JSONObject childObj = productArray.getJSONObject(i);

                    JSONObject ProductName = childObj.getJSONObject("display_name_translations");
                    JSONArray ProductImage = childObj.getJSONArray("images");
                    JSONObject objingre = childObj.getJSONObject("ingredients_translations");
                    JSONObject objOrigin = childObj.getJSONObject("origin_translations");
                    JSONObject objNutri = childObj.getJSONObject("nutrients");

                    if (objNutri.has("protein")) {

                        JSONObject objProtein = objNutri.getJSONObject("protein");
                        JSONObject objProteinName = objProtein.getJSONObject("name_translations");

                        nutrition = objProteinName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("carbohydrates")) {

                        JSONObject objCarbo = objNutri.getJSONObject("carbohydrates");
                        JSONObject objCarboName = objCarbo.getJSONObject("name_translations");

                        nutrition += objCarboName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("fat")) {

                        JSONObject objFat = objNutri.getJSONObject("fat");
                        JSONObject objFatName = objFat.getJSONObject("name_translations");

                        nutrition += objFatName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("sodium")) {

                        JSONObject objSodium = objNutri.getJSONObject("sodium");
                        JSONObject objSodiumName = objSodium.getJSONObject("name_translations");

                        nutrition += objSodiumName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("fiber")) {

                        JSONObject objFiber = objNutri.getJSONObject("fiber");
                        JSONObject objFiberName = objFiber.getJSONObject("name_translations");

                        nutrition += objFiberName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("salt")) {

                        JSONObject objSalt = objNutri.getJSONObject("salt");
                        JSONObject objSaltName = objSalt.getJSONObject("name_translations");

                        nutrition += objSaltName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("sugars")) {

                        JSONObject objSugar = objNutri.getJSONObject("sugars");
                        JSONObject objSugarName = objSugar.getJSONObject("name_translations");

                        nutrition += objSugarName.getString("en");

                    }

                    nutrition += " ";

                    if (objNutri.has("energy_kcal")) {

                        JSONObject objEnergyCal = objNutri.getJSONObject("energy_kcal");
                        JSONObject objEnergyCalName = objEnergyCal.getJSONObject("name_translations");

                        nutrition += objEnergyCalName.getString("en");


                    }

                    nutrition += " ";

                    if (objNutri.has("energy")) {

                        JSONObject objEnergy = objNutri.getJSONObject("energy");
                        JSONObject objEnergyName = objEnergy.getJSONObject("name_translations");

                        nutrition += objEnergyName.getString("en");

                    }


                    product_name = ProductName.getString("en");
                    //nutrition = "Nutrition";
                    unit = childObj.getString("unit");

                    for (int j = 0; j < ProductImage.length(); j++) {

                        JSONObject ProImg = ProductImage.getJSONObject(j);
                        if (j == 1) {

                            product_image = ProImg.getString("large");

                        }

                        if (j == 2) {

                            image_back = ProImg.getString("large");

                        }

                    }

                    pro.add(new Products(product_name, product_image, image_back, nutrition, unit));

                }

                System.out.println("ArrayList Size : " + pro.size());


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return pro;
        }

        @Override
        protected void onPostExecute(ArrayList<Products> result) {
            super.onPostExecute(result);
            Listadapter listadapter=new Listadapter(MainActivity.this,result);
            lv.setAdapter(listadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this,ProductDetails.class);
                    intent.putExtra("data",pro.get(i));
                    startActivity(intent);
                }
            });
        }
    }
}