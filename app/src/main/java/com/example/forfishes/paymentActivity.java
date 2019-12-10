package com.example.forfishes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forfishes.Fish.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class paymentActivity extends AppCompatActivity
{

    EditText name,id,amount;
    TextView trnID;
    Button paywithgl;
    private String names,phones,addresses,states,picodes;
    private String savecurrentdate,savecurrenttime,productrandomkey;
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String GOOGLE_PAY_PACKAGE_NAME= "com.google.android.apps.nbu.paisa.user";
    String TAG= "main";
    String eee="";
    final int UPI_PAYMENT=0;
    private DatabaseReference productRef,ProductRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=currentDate.format(date.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
        savecurrenttime=currentTime.format(date.getTime());

        productrandomkey=savecurrentdate + savecurrenttime;

        productRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentOnlineuser.getPhone()).child("Products");
        ProductRef2=FirebaseDatabase.getInstance().getReference().child("Mine").child(Prevalent.currentOnlineuser.getPhone());

       names= getIntent().getStringExtra("names");
       phones=getIntent().getStringExtra("phones");
       addresses=getIntent().getStringExtra("addresses");
       states=getIntent().getStringExtra("states");
       picodes=getIntent().getStringExtra("pincodes");

        Toast.makeText(this, names, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, phones, Toast.LENGTH_SHORT).show();

        trnID=(TextView)findViewById(R.id.transactionID);
        paywithgl=(Button)findViewById(R.id.paywithgl);
        name=(EditText) findViewById(R.id.payeename);
        id=(EditText)findViewById(R.id.payeeID);
        amount=(EditText)findViewById(R.id.payment);



        paywithgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(id.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter id", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(amount.getText()))
                {
                    Toast.makeText(paymentActivity.this, "Enter amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    payusingUPI("Aravind Kimonn", "pl.7904168617@icici",
                            amount.getText().toString());
                }
            }
        });

    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void payusingUPI(String name, String id, String amount)
    {
        Log.e("main", "name"+ name + "--up--"+id+"--"+amount);
      Uri uri=
              new Uri.Builder()
                      .scheme("upi")
                      .authority("pay")
              .appendQueryParameter("pa",id)
              .appendQueryParameter("pn",name)
              .appendQueryParameter("am",amount)
              .appendQueryParameter("cu","INR")
              .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);


      /*Intent opengpay= getPackageManager().getLaunchIntentForPackage(GOOGLE_PAY_PACKAGE_NAME);
        Intent choose = Intent.createChooser(opengpay, "Pay with");
        startActivityForResult(choose, UPI_PAYMENT);
      startActivity(opengpay);*/

        /*Intent upipayIntent = new Intent(Intent.ACTION_VIEW);
        upipayIntent.setData(uri);

        Intent choose = Intent.createChooser(upipayIntent, "Pay with");
        if (null!=choose.resolveActivity(getPackageManager()))
        {
            startActivityForResult(choose, UPI_PAYMENT);
        }else {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }*/
    }

    protected void onActivityResult(int requestCode, int resultcode, Intent data)
    {
        super.onActivityResult(requestCode, resultcode, data);
        String trnsID = data.getStringExtra("response");
        Log.e("UPI", "onActivityResultttt:" + trnsID);
        eee=trnsID;
        if(eee.contains("SUCCESS"))
        {

            moveGameRoom(productRef,ProductRef2);
            Toast.makeText(this, "IRUKU", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(paymentActivity.this,PlacedsuccessfullyActivity.class);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(this, "ILA", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, eee, Toast.LENGTH_SHORT).show();
        trnID.setText(trnsID);

        /*Log.e("main", "requestCode is" +requestCode+ "UPI_PAYMENT IS "+UPI_PAYMENT +" resultcode is "+RESULT_OK+ "data is" +data);

        switch (requestCode)
        {
            case UPI_PAYMENT:
                if((RESULT_OK==resultcode)|| (resultcode==11))
                {
                    if (data != null)
                    {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResultttt:" + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        Toast.makeText(this, "Transaction is success", Toast.LENGTH_SHORT).show();
                      // upiPaymentDataOperation(dataList);
                    } else

                        {
                        Log.e("UPI", "onActivityResult" + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                     // upiPaymentDataOperation(dataList);
                    }
                }
                else
                    {
                        Log.e("UPI", "OnActivityResult" + "Return data is null");
                        ArrayList<String>dataList = new ArrayList<>();
                        dataList.add("nothing");
                      //  upiPaymentDataOperation(dataList);

                }
                break;
        }*/


    }





}
