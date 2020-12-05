package edu.wschina.b05;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonToken;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartProvider{
    private  SparseArray<ShoppingCart> date;
    private Context context;
    private final SharedPreferences cart_json;
    private final SharedPreferences.Editor edit;

    public CartProvider(Context context) {
        date = new SparseArray<>();
        this.context = context;
        cart_json = context.getSharedPreferences("CART_JSON", 0);
        edit = cart_json.edit();
        listToSparse();
    }

    public void listToSparse(){
        List<ShoppingCart> carts = getDataFromLocal();
        if(carts != null && carts.size()>0){
            for (ShoppingCart cart : carts) {
                date.put(cart.getId(),cart);
            }
        }
    }


    public void put(ShoppingCart cart){
            ShoppingCart shoppingCart = date.get(cart.getId());
            if (shoppingCart != null) {
                shoppingCart.setCount(shoppingCart.getCount()+1);
            } else {
                shoppingCart = cart;
                shoppingCart.setCount(1);
                date.put(cart.getId(), shoppingCart);
            }
            commit();
    }


    public void subput(ShoppingCart cart){
        ShoppingCart shoppingCart = date.get(cart.getId());
        if (shoppingCart != null) {
            shoppingCart.setCount(shoppingCart.getCount()-1);
        }
        date.put(cart.getId(), shoppingCart);
        commit();
    }

    private void commit() {
       List<ShoppingCart> carts = sparseToList();
       if (carts != null){
           JSONArray jsonArray = new JSONArray();
           JSONObject tmpObj = null;
           for (int i = 0; i < carts.size(); i++) {
               tmpObj = new JSONObject();
               try {
                   tmpObj.put("id" , carts.get(i).getId());
                   tmpObj.put("name", carts.get(i).getName());
                   tmpObj.put("count", carts.get(i).getCount());
                   tmpObj.put("price", carts.get(i).getPrice());
                   tmpObj.put("image_url", carts.get(i).getImage_url());
                   jsonArray.put(tmpObj);
                   tmpObj = null;
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           String cart = jsonArray.toString(); // 将JSONArray转换得到String
           edit.putString("carts",cart);
       }

        edit.commit();

    }

    public List<ShoppingCart> sparseToList(){
        List<ShoppingCart> list = new ArrayList<>();
        for (int i=0;i<date.size();i++){
            list.add(date.valueAt(i));
        }
        return list;
    }

    public void update(ShoppingCart cart){
        date.put(cart.getId(),cart);
        commit();
    }
    public void clear(){
        date.clear();
        edit.remove("cart");
        edit.commit();

    }

    public void delete(ShoppingCart cart){
        date.delete(cart.getId());
        commit();
    }
    public List<ShoppingCart> getAll(){
        return getDataFromLocal();
    }

    public List<ShoppingCart> getDataFromLocal(){
        String json = cart_json.getString("carts", null);
        List<ShoppingCart> carts = new ArrayList<>();
        if (json != null){
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    shoppingCart.setImage_url(jsonObject.getString("image_url"));
                    shoppingCart.setName(jsonObject.getString("name"));
                    shoppingCart.setId(jsonObject.getInt("id"));
                    shoppingCart.setCount(jsonObject.getInt("count"));
                    shoppingCart.setPrice(jsonObject.getInt("price"));
                    carts.add(shoppingCart);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return carts;
    }
}
