package com.example.pizzamania;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "PayHere Demo";

    private TextView textView;


    private final ActivityResultLauncher<Intent> peyherelauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult() ,
            result ->{
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Intent  data = result.getData();
                    if (data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
                        Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                        if (serializable instanceof PHResponse) {
                            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;
                            String msg = response.isSuccess() ? "Payment Success: " + response.getData() : "Payment Failed" +response;
                            Log.d(TAG, msg);
                            textView.setText(msg);
                        }
                    }
                }
                else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    textView.setText("User Cancelled the payment");
                }
            }
    );

    TextView txtCustomerName, txtCustomerPhone, txtCustomerAddress, txtTotalPrice;
    ListView listViewCheckout;
    Button btnPlaceOrder, btnCancel;


    CartAdapter adapter;
    SqliteHelper dbHelper;
    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Bind Views
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCustomerPhone = findViewById(R.id.txtCustomerPhone);
        txtCustomerAddress = findViewById(R.id.txtCustomerAddress);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        listViewCheckout = findViewById(R.id.listViewCheckout);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnCancel = findViewById(R.id.btnCancel);

        dbHelper = new SqliteHelper(this);

        // Get logged-in email from session
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        sessionEmail = sharedPreferences.getString("email", null);

        // Load customer info (read-only)
        loadCustomerInfo(sessionEmail);

        // Show cart items (read-only, no quantity change)
        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems()) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Hide increase/decrease/remove buttons in checkout
                View btnIncrease = view.findViewById(R.id.btnIncrease);
                View btnDecrease = view.findViewById(R.id.btnDecrease);
                View btnRemove = view.findViewById(R.id.btnRemove);
                if (btnIncrease != null) btnIncrease.setVisibility(View.GONE);
                if (btnDecrease != null) btnDecrease.setVisibility(View.GONE);
                if (btnRemove != null) btnRemove.setVisibility(View.GONE);
                return view;
            }
        };
        listViewCheckout.setAdapter(adapter);

        // Show total price
        txtTotalPrice.setText("Total: Rs. " + CartManager.getInstance().getTotalPrice());

        // Place Order
        btnPlaceOrder.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "✅ Order placed successfully!", Toast.LENGTH_LONG).show();

            // Clear cart
            CartManager.getInstance().clearCart();
            adapter.notifyDataSetChanged();

            // Go to payment gateway
            Intent intent = new Intent(CheckoutActivity.this, PaymentGatewayActivity.class);
            intent.putExtra("customerName", txtCustomerName.getText().toString());
            intent.putExtra("customerPhone", txtCustomerPhone.getText().toString());
            intent.putExtra("customerAddress", txtCustomerAddress.getText().toString());
            intent.putExtra("totalPrice", CartManager.getInstance().getTotalPrice());
            startActivity(intent);
            finish();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> {
            finish(); // just close checkout
        });
        Button pay_button = findViewById(R.id.btnPlaceOrder);
        //textView = findViewById(R.id.textView);

        pay_button.setOnClickListener(view -> initiatepayment());
    }

        private void initiatepayment() {

            InitRequest req = new InitRequest();
            req.setMerchantId("1231912");       // Merchant ID
            req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
            req.setAmount(1000.00);             // Final Amount to be charged
            req.setOrderId("230000123");        // Unique Reference ID
            req.setItemsDescription("Door bell wireless");  // Item description title
            req.setCustom1("This is the custom message 1");
            req.setCustom2("This is the custom message 2");
            req.getCustomer().setFirstName("Saman");
            req.getCustomer().setLastName("Perera");
            req.getCustomer().setEmail("kalpamihiranga957@gmail.com");
            req.getCustomer().setPhone("+94740662500");
            req.getCustomer().getAddress().setAddress("No.1, Galle Road");
            req.getCustomer().getAddress().setCity("Colombo");
            req.getCustomer().getAddress().setCountry("Sri Lanka");

//Optional Params
            // Notifiy Url
            req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
            req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
            req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
            req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));

            req.setNotifyUrl(" ");

            Intent intent = new Intent(this, PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            // startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID e.g. "11001"
            peyherelauncher.launch(intent);

        }


    private void loadCustomerInfo(String email) {
        Cursor cursor = dbHelper.getCustomerByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

            txtCustomerName.setText("Name: " + name);
            txtCustomerPhone.setText("Phone: " + phone);
            txtCustomerAddress.setText("Address: " + address);

            cursor.close();
        }
    }
}
