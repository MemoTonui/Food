package com.linda.food.UI;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.models.Food;
import com.linda.food.models.Orders;
import com.linda.food.models.PrefConfig;
import com.linda.food.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.linda.food.UI.App.CHANNEL_1_ID;
import static com.linda.food.UI.App.CHANNEL_2_ID;

public class Success extends AppCompatActivity {
    @BindView(R.id.success) Button successButton;
    @BindView(R.id.invoice) Button invoiceButton;
    Bitmap bmp, scaleBitMap;
    int pageWidth = 1200;
    DateFormat dateFormat;

    String orderId;
    String id;
    int subtotal;

    List<Food> cartFoodList;
    Orders orders;
    User user;
    int quantity;
    int deliveryCost;
    int mySubTotal;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.pizzahead);
        scaleBitMap = Bitmap.createScaledBitmap(bmp,1200, 518,false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        cartFoodList = new ArrayList<>();

        orderId = sharedPreferences.getString(Constants.PREFERENCES_ORDER_ID,null);
        id = sharedPreferences.getString(Constants.PREFERENCES_USER_ID,null);
        quantity = sharedPreferences.getInt(String.valueOf(Constants.PREFERENCES_QUANTITY),0);
        deliveryCost = sharedPreferences.getInt(String.valueOf(Constants.PREFERENCES_DELIVERY),0);

        getOrder();
        getUser();

        if((!(ContextCompat.checkSelfPermission(getApplicationContext(),"android.permission.WRITE_EXTERNAL_STORAGE") ==0))){
            ActivityCompat.requestPermissions(this,new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},1);
        }

        if((!(ContextCompat.checkSelfPermission(getApplicationContext(),"android.permission.READ_EXTERNAL_STORAGE") ==0))){
            ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},2);
        }


        cartFoodList = PrefConfig.readListFromPref(getApplicationContext());

        notificationManager = NotificationManagerCompat.from(this);

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PrefConfig.ClearInPref(getApplicationContext(),cartFoodList);
                startActivity(new Intent(Success.this,MainActivity.class));
                sendOnChannel1(v);
            }
        });

        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
    }

    private  void getUser() {
        FoodzillaService service = FoodzillaClient.getClient();
        Call<User> userCall = service.getUser(id);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private   void getOrder(){
        FoodzillaService service = FoodzillaClient.getClient();
        Call<Orders> ordersCall = service.getOrderById(orderId);
        ordersCall.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()){
                    orders = response.body();
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {

            }
        });
    }

    public void createPdf(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        PdfDocument myDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page page = myDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scaleBitMap,0,0,paint);
        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        title.setTextSize(70);
        canvas.drawText("Foodzilla",pageWidth/2,270,title);

        paint.setColor(Color.rgb(0,113,188));
        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Call: +2547 0485 8473",1160,40,paint);
        canvas.drawText("+2547 0485 8473",1160,80,paint);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        title.setTextSize(70);
        canvas.drawText("Invoice",pageWidth/2,500,title);

        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        canvas.drawText("Customer Name: "+user.getFullName(),20,590,paint);
        canvas.drawText("Phone Number: "+user.getPhoneNumber(),20,640,paint);


        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.BLACK);
        canvas.drawText("Invoice No.: "+Math.random(),pageWidth-20,590,paint);
        dateFormat = new SimpleDateFormat("dd/MM/yy");

        canvas.drawText("Date:"+dateFormat.format(new Date()),200,690,paint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Time:"+dateFormat.format(new Date()),200,740,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20,780,pageWidth-20,860,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Serial No.",40,830,paint);
        canvas.drawText("Food Name",200,830,paint);
        canvas.drawText("Price",700,830,paint);
        canvas.drawText("Quantity",900,830,paint);
        canvas.drawText("Total",1100,830,paint);

        canvas.drawLine(180,790,180,840,paint);
        canvas.drawLine(680,790,680,840,paint);
        canvas.drawLine(880,790,880,840,paint);
        canvas.drawLine(1030,790,1030,840,paint);

        for (Food food: cartFoodList) {
            mySubTotal = food.getFoodPrice()*quantity;
            canvas.drawText("1", 40, 950, paint);
            canvas.drawText(food.getFoodName(), 200, 950, paint);
            canvas.drawText(food.getFoodPrice().toString(), 700, 950, paint);
            canvas.drawText(String.valueOf(quantity), 900, 950, paint);
            canvas.drawText(String.valueOf(mySubTotal),1100 , 950, paint);
            //canvas.drawLine(30,1200,1100,1200,paint);
        }
        canvas.drawLine(680,1200,1100,1200,paint);
        canvas.drawText("Sub Total",700,1250,paint);
        canvas.drawText(":",900,1250,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(mySubTotal),pageWidth-40,1250,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Delivery Cost",700,1300,paint);
        canvas.drawText(":",900,1300,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(deliveryCost),pageWidth-40,1300,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.rgb(247,147,30));
        canvas.drawRect(680,1350,pageWidth-20,1450,paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total",700,1415,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(mySubTotal+deliveryCost),pageWidth-40,1415,paint);

        myDocument.finishPage(page);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"foodzilla.pdf");

        try {
            myDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Check in your downloads for your invoice!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myDocument.close();


    }

    public void sendOnChannel1(View v) {
        String notificationTitle = "Foodzilla! Your order has successfully been made!";
        String notificationMessage = "Your food will arrive at the stated location in 30 minutes";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    public void sendOnChannel2(View v) {
        String notificationTitle = "Foodzilla Order And Delivery";
        String notificationMessage = "Your food has arrived! Please pick it up";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTitle)
                // .setContentText(notificationMessage)
                .setContentInfo(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2, notification);
    }
}