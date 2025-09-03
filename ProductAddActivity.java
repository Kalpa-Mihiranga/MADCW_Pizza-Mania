// ...existing imports...
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.net.Uri;
// ...existing code...

public class ProductAddActivity extends AppCompatActivity {
    // ...existing code...

    private ImageView imageViewPhoto;
    private TextView textViewDetails;

    // ...existing code...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        // Initialize views
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        textViewDetails = findViewById(R.id.textViewDetails);

        // ...existing code...
    }

    // Call this method after adding a product
    private void showAddedProductDetails(String details, Uri photoUri) {
        textViewDetails.setText(details);
        imageViewPhoto.setImageURI(photoUri);
    }

    // Example: After adding product, call showAddedProductDetails
    private void onProductAdded(String details, Uri photoUri) {
        // ...existing code for adding product...
        showAddedProductDetails(details, photoUri);
    }

    // ...existing code...
}

